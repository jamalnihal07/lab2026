// ============================================
// GoPro - Ride Hailing App - Main JavaScript
// ============================================

// Session Storage Keys
const USER_KEY = 'gopro_user';
const WALLET_KEY = 'gopro_wallet';
const RIDES_KEY = 'gopro_rides';
const DRIVERS_KEY = 'gopro_drivers';

async function apiRequest(path, options = {}) {
    const response = await fetch(path, {
        headers: {
            'Content-Type': 'application/json',
            ...(options.headers || {})
        },
        ...options
    });

    const responseText = await response.text();
    let payload = {};
    if (responseText) {
        try {
            payload = JSON.parse(responseText);
        } catch (error) {
            payload = { message: responseText };
        }
    }

    if (!response.ok) {
        const error = new Error(payload.error || payload.message || `Request failed (${response.status})`);
        error.status = response.status;
        error.payload = payload;
        throw error;
    }

    return payload;
}

function loadRazorpayScript() {
    return new Promise((resolve, reject) => {
        if (window.Razorpay) {
            resolve(true);
            return;
        }

        const script = document.createElement('script');
        script.src = 'https://checkout.razorpay.com/v1/checkout.js';
        script.onload = () => resolve(true);
        script.onerror = () => reject(new Error('Unable to load Razorpay checkout.'));
        document.head.appendChild(script);
    });
}

async function runRazorpayTopUp({ user, amount, method }) {
    const orderResponse = await apiRequest('/api/payments/razorpay/order', {
        method: 'POST',
        body: JSON.stringify({
            userId: user.id,
            amount,
            method,
            purpose: 'wallet-topup'
        })
    });

    await loadRazorpayScript();

    return new Promise((resolve, reject) => {
        const checkout = new Razorpay({
            key: orderResponse.keyId,
            amount: Math.round(amount * 100),
            currency: orderResponse.currency || 'INR',
            name: 'GoPro Wallet Top-up',
            description: 'Add money to wallet',
            order_id: orderResponse.order.id,
            prefill: {
                name: user.name,
                email: user.email,
                contact: user.phone
            },
            theme: {
                color: '#FF6B6B'
            },
            handler: async function (response) {
                try {
                    const verify = await apiRequest('/api/payments/razorpay/verify', {
                        method: 'POST',
                        body: JSON.stringify({
                            userId: user.id,
                            amount,
                            method,
                            purpose: 'wallet-topup',
                            provider: 'razorpay',
                            orderId: response.razorpay_order_id,
                            paymentId: response.razorpay_payment_id,
                            signature: response.razorpay_signature
                        })
                    });
                    resolve(verify);
                } catch (verifyError) {
                    reject(verifyError);
                }
            }
        });

        checkout.on('payment.failed', function (response) {
            reject(new Error(response?.error?.description || 'Razorpay payment failed'));
        });

        checkout.open();
    });
}

function syncLocalAccount(account) {
    if (!account) return;

    const collections = [appData.users, appData.drivers, appData.admins];
    for (const collection of collections) {
        const index = collection.findIndex(item => item.id === account.id || item.email === account.email);
        if (index >= 0) {
            collection[index] = { ...collection[index], ...account };
            return;
        }
    }

    if (account.type === 'driver') {
        appData.drivers.push(account);
    } else if (account.type === 'admin') {
        appData.admins.push(account);
    } else {
        appData.users.push(account);
    }
}

// Initialize - Check for signup parameter
document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get('signup') === 'true') {
        toggleForms();
    }
});

// Sample Data Structure
const appData = {
    users: [
        {
            id: 'U001',
            name: 'Rahul Sharma',
            email: 'rahul@email.com',
            phone: '9876543210',
            password: 'pass123',
            type: 'rider',
            totalRides: 25,
            avgRating: 4.5,
            wallet: 2500,
            totalSpent: 5000,
            joinDate: '2024-01',
            verified: true
        },
        {
            id: 'U002',
            name: 'Priya Patel',
            email: 'priya@email.com',
            phone: '9876543211',
            password: 'pass456',
            type: 'rider',
            totalRides: 15,
            avgRating: 4.3,
            wallet: 1500,
            totalSpent: 3000,
            joinDate: '2024-02',
            verified: true
        }
    ],
    drivers: [
        {
            id: 'D001',
            name: 'Anuj Singh',
            email: 'anuj@email.com',
            phone: '9876543220',
            password: 'driver123',
            type: 'driver',
            totalRides: 156,
            avgRating: 4.8,
            vehicle: {
                type: 'Sedan',
                registration: 'DL-01-AA-0001',
                color: 'White',
                model: 'Hyundai Creta 2022'
            },
            earnings: {
                today: 1250,
                week: 8500,
                month: 35000,
                total: 250000
            },
            verified: true
        }
    ],
    rides: [
        {
            id: 'R001',
            userId: 'U001',
            driverId: 'D001',
            pickupLocation: 'Connaught Place, Delhi',
            dropoffLocation: 'Terminal 3, IGI Airport',
            distance: 18.5,
            fare: 245,
            status: 'completed',
            date: '2024-01-15',
            rating: 5
        }
    ],
    admins: [
        {
            id: 'ADMIN001',
            email: 'admin@gopro.com',
            password: 'admin123',
            name: 'Admin User',
            type: 'admin',
            verified: true
        }
    ]
};

// ============================================
// AUTHENTICATION FUNCTIONS
// ============================================

function toggleForms(evt = window.event) {
    const loginForm = document.getElementById('loginForm');
    const signupForm = document.getElementById('signupForm');
    loginForm.classList.toggle('active');
    signupForm.classList.toggle('active');
    if (evt) evt.preventDefault();
}

async function handleLogin(event) {
    event.preventDefault();
    
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;
    const userType = document.getElementById('userType').value;

    try {
        const user = await apiRequest('/api/login', {
            method: 'POST',
            body: JSON.stringify({ email, password, type: userType })
        });

        syncLocalAccount(user);
        sessionStorage.setItem(USER_KEY, JSON.stringify(user));

        if (userType === 'admin') {
            window.location.href = 'admin-dashboard.html';
        } else if (userType === 'driver') {
            window.location.href = 'driver-dashboard-enhanced.html';
        } else {
            window.location.href = 'rider-dashboard-new.html';
        }
        return;
    } catch (error) {
        if (error.status === 403 && error.payload?.verificationRequired) {
            const resend = confirm('Your account needs verification. Send a new code now?');
            if (resend) {
                try {
                    const refreshed = await apiRequest('/api/resend-verification', {
                        method: 'POST',
                        body: JSON.stringify({ email, type: userType })
                    });
                    const enteredCode = prompt('Enter the verification code you received:', refreshed.verificationCode || '');
                    if (enteredCode) {
                        await apiRequest('/api/verify-registration', {
                            method: 'POST',
                            body: JSON.stringify({ email, type: userType, code: enteredCode.trim() })
                        });
                        const verifiedUser = await apiRequest('/api/login', {
                            method: 'POST',
                            body: JSON.stringify({ email, password, type: userType })
                        });
                        syncLocalAccount(verifiedUser);
                        sessionStorage.setItem(USER_KEY, JSON.stringify(verifiedUser));

                        if (userType === 'admin') {
                            window.location.href = 'admin-dashboard.html';
                        } else if (userType === 'driver') {
                            window.location.href = 'driver-dashboard-enhanced.html';
                        } else {
                            window.location.href = 'rider-dashboard-new.html';
                        }
                        return;
                    }
                } catch (verificationError) {
                    alert(verificationError.message || 'Verification failed');
                    return;
                }
            }
            alert('Account verification is required before login.');
            return;
        }

        // Local fallback for offline/demo mode
        let user = null;
        if (userType === 'admin') {
            user = appData.admins.find(u => u.email === email && u.password === password);
            if (user) {
                sessionStorage.setItem(USER_KEY, JSON.stringify({ ...user, type: 'admin' }));
                window.location.href = 'admin-dashboard.html';
                return;
            }
        } else if (userType === 'driver') {
            user = appData.drivers.find(u => u.email === email && u.password === password && u.verified !== false);
            if (user) {
                sessionStorage.setItem(USER_KEY, JSON.stringify({ ...user, type: 'driver' }));
                window.location.href = 'driver-dashboard-enhanced.html';
                return;
            }
        } else {
            user = appData.users.find(u => u.email === email && u.password === password && u.type === 'rider' && u.verified !== false);
            if (user) {
                sessionStorage.setItem(USER_KEY, JSON.stringify({ ...user, type: 'rider' }));
                window.location.href = 'rider-dashboard-new.html';
                return;
            }
        }

        alert(error.message || 'Invalid credentials!');
    }
}

async function handleSignup(event) {
    event.preventDefault();
    
    const name = document.getElementById('signupName').value;
    const email = document.getElementById('signupEmail').value;
    const phone = document.getElementById('signupPhone').value;
    const password = document.getElementById('signupPassword').value;
    const confirmPassword = document.getElementById('signupConfirmPassword').value;
    const accountType = document.getElementById('signupAccountType').value;
    
    if (password !== confirmPassword) {
        alert('Passwords do not match!');
        return;
    }

    try {
        const registration = await apiRequest('/api/register', {
            method: 'POST',
            body: JSON.stringify({
                name,
                email,
                phone,
                password,
                type: accountType
            })
        });

        const enteredCode = prompt(`Enter the verification code to activate ${email}:`, registration.verificationCode || '');
        if (!enteredCode) {
            alert('Verification cancelled. Your account was created but not activated yet.');
            return;
        }

        const verifiedUser = await apiRequest('/api/verify-registration', {
            method: 'POST',
            body: JSON.stringify({
                email,
                type: accountType,
                code: enteredCode.trim()
            })
        });

        syncLocalAccount(verifiedUser.user);
        alert('Account verified successfully! Please login.');
        toggleForms();
        return;
    } catch (error) {
        // Local fallback for offline/demo mode
        const id = accountType === 'driver'
            ? 'D' + Math.floor(Math.random() * 10000).toString().padStart(3, '0')
            : 'U' + Math.floor(Math.random() * 10000).toString().padStart(3, '0');

        const newUser = {
            id,
            name,
            email,
            phone,
            password,
            type: accountType,
            totalRides: 0,
            avgRating: 0,
            wallet: 0,
            totalSpent: 0,
            joinDate: new Date().toISOString().split('-').slice(0, 2).join('-'),
            verified: true
        };

        if (accountType === 'driver') {
            newUser.vehicle = {
                type: 'Sedan',
                registration: '',
                color: '',
                model: ''
            };
            newUser.earnings = {
                today: 0,
                week: 0,
                month: 0,
                total: 0
            };
            appData.drivers.push(newUser);
        } else {
            appData.users.push(newUser);
        }

        alert(error.message ? `${error.message} — using offline signup fallback.` : 'Account created successfully! Please login.');
        toggleForms();
    }
}

function logout() {
    sessionStorage.removeItem(USER_KEY);
    window.location.href = 'index.html';
}

// ============================================
// USER SESSION MANAGEMENT
// ============================================

function getLoggedInUser() {
    const user = sessionStorage.getItem(USER_KEY);
    return user ? JSON.parse(user) : null;
}

function checkAuthentication() {
    const user = getLoggedInUser();
    if (!user) {
        window.location.href = 'index.html';
        return null;
    }
    return user;
}

// ============================================
// COMMON UI FUNCTIONS
// ============================================

function switchSection(sectionId, event = null) {
    // Hide all sections
    const sections = document.querySelectorAll('.content-section');
    sections.forEach(section => {
        section.classList.remove('active');
    });
    
    // Show target section
    const targetSection = document.getElementById(sectionId + 'Section');
    if (targetSection) {
        targetSection.classList.add('active');
        
        // Update page title
        const titles = {
            'dashboard': 'Dashboard',
            'book-ride': 'Book Your Ride',
            'active-ride': 'Active Ride',
            'ride-history': 'Ride History',
            'wallet': 'Wallet Management',
            'profile': 'My Profile',
            'driver-dashboard': 'Driver Dashboard',
            'available-rides': 'Available Ride Requests',
            'current-ride': 'Current Ride',
            'earnings': 'Earnings & Analytics',
            'driver-profile': 'Driver Profile',
            'overview': 'Overview',
            'users': 'User Management',
            'drivers': 'Driver Management',
            'rides': 'Rides Management',
            'payments': 'Payment Management',
            'reports': 'Reports & Analytics',
            'support': 'Support Tickets',
            'settings': 'System Settings'
        };
        
        const pageTitle = document.getElementById('pageTitle') || document.getElementById('adminPageTitle');
        if (pageTitle && titles[sectionId]) {
            pageTitle.textContent = titles[sectionId];
        }
    }
    
    if (event) {
        event.preventDefault();
    }
}

function switchAdminSection(sectionId) {
    switchSection(sectionId);
}

// ============================================
// NOTIFICATION MANAGEMENT
// ============================================

function showNotification(message, type = 'info') {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        background-color: ${type === 'success' ? '#27AE60' : type === 'error' ? '#E74C3C' : '#3498DB'};
        color: white;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        z-index: 9999;
        animation: slideInRight 0.3s ease;
    `;
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.style.animation = 'slideOutRight 0.3s ease';
        setTimeout(() => notification.remove(), 300);
    }, 3000);
}

// ============================================
// RIDE FUNCTIONS
// ============================================

function updateFareEstimate() {
    const distance = parseFloat(document.getElementById('distance').value) || 0;
    const rideType = document.getElementById('rideType').value;
    
    const rateMap = {
        'economy': 10,
        'comfort': 15,
        'premium': 20
    };
    
    const baseFare = 50;
    const perKmRate = rateMap[rideType];
    const estimatedFare = baseFare + (distance * perKmRate);
    
    const fareEstimate = document.getElementById('fareEstimate');
    fareEstimate.innerHTML = `<p>Estimated Fare: <strong>₹${estimatedFare.toFixed(2)}</strong></p>`;
    
    return estimatedFare;
}

function bookRide(event) {
    event.preventDefault();
    
    const user = getLoggedInUser();
    if (!user) return;
    
    const pickupLocation = document.getElementById('pickupLocation').value;
    const dropoffLocation = document.getElementById('dropoffLocation').value;
    const distance = parseFloat(document.getElementById('distance').value);
    const rideType = document.getElementById('rideType').value;
    const promoCode = document.getElementById('promoCode').value;
    
    const estimatedFare = updateFareEstimate();
    
    // Create new ride
    const newRide = {
        id: 'R' + Math.floor(Math.random() * 10000).toString().padStart(4, '0'),
        userId: user.id,
        driverId: appData.drivers[0].id,
        pickupLocation: pickupLocation,
        dropoffLocation: dropoffLocation,
        distance: distance,
        fare: estimatedFare,
        status: 'active',
        date: new Date().toISOString().split('T')[0],
        rating: 0,
        promoCode: promoCode
    };
    
    appData.rides.push(newRide);
    showNotification('Ride booked successfully!', 'success');
    
    // Reset form
    document.querySelector('.ride-form').reset();
    
    // Switch to active ride section
    setTimeout(() => {
        switchSection('active-ride');
    }, 500);
}

function cancelRide(rideId) {
    const ride = appData.rides.find(r => r.id === rideId);
    if (ride) {
        ride.status = 'cancelled';
        showNotification('Ride cancelled successfully!', 'success');
        loadRideHistory();
    }
}

function rateRide(rideId, rating) {
    const ride = appData.rides.find(r => r.id === rideId);
    if (ride) {
        ride.rating = rating;
        showNotification(`Ride rated ${rating} stars!`, 'success');
    }
}

// ============================================
// WALLET FUNCTIONS
// ============================================

async function addMoneyToWallet(event) {
    event.preventDefault();
    
    const user = getLoggedInUser();
    if (!user) return;
    
    const amount = parseFloat(document.getElementById('addAmount').value);
    const paymentMethod = document.getElementById('paymentMethod').value;
    
    if (amount < 100) {
        alert('Minimum amount is ₹100');
        return;
    }

    try {
        let response;

        if (paymentMethod === 'wallet') {
            response = await apiRequest('/api/payments/checkout', {
                method: 'POST',
                body: JSON.stringify({
                    userId: user.id,
                    amount,
                    method: paymentMethod,
                    purpose: 'wallet-topup',
                    provider: paymentMethod
                })
            });
        } else {
            try {
                response = await runRazorpayTopUp({ user, amount, method: paymentMethod });
            } catch (gatewayError) {
                console.warn('Razorpay wallet top-up unavailable, falling back to demo payment:', gatewayError);
                response = await apiRequest('/api/payments/checkout', {
                    method: 'POST',
                    body: JSON.stringify({
                        userId: user.id,
                        amount,
                        method: paymentMethod,
                        purpose: 'wallet-topup',
                        provider: paymentMethod
                    })
                });
            }
        }

        user.wallet = response.walletBalance ?? ((user.wallet || 0) + amount);
        sessionStorage.setItem(USER_KEY, JSON.stringify(user));
        syncLocalAccount(user);

        const walletBalance = document.getElementById('walletBalance');
        if (walletBalance) walletBalance.textContent = '₹' + user.wallet;

        const currentBalance = document.getElementById('currentBalance');
        if (currentBalance) currentBalance.textContent = '₹' + user.wallet;

        showNotification(`₹${amount} added to wallet via ${paymentMethod}!`, 'success');
        document.querySelector('.add-money-form').reset();
    } catch (error) {
        user.wallet = (user.wallet || 0) + amount;
        sessionStorage.setItem(USER_KEY, JSON.stringify(user));
        syncLocalAccount(user);

        const walletBalance = document.getElementById('walletBalance');
        if (walletBalance) walletBalance.textContent = '₹' + user.wallet;

        const currentBalance = document.getElementById('currentBalance');
        if (currentBalance) currentBalance.textContent = '₹' + user.wallet;

        showNotification(`₹${amount} added to wallet via ${paymentMethod}! (offline fallback)`, 'success');
        document.querySelector('.add-money-form').reset();
    }
}

// ============================================
// PROFILE FUNCTIONS
// ============================================

function updateProfile(event) {
    event.preventDefault();
    
    const user = getLoggedInUser();
    if (!user) return;
    
    const name = document.getElementById('editName').value;
    const phone = document.getElementById('editPhone').value;
    
    user.name = name;
    user.phone = phone;
    sessionStorage.setItem(USER_KEY, JSON.stringify(user));
    
    showNotification('Profile updated successfully!', 'success');
    loadProfile();
}

function loadProfile() {
    const user = getLoggedInUser();
    if (!user) return;
    
    const profileName = document.getElementById('profileName');
    const profileEmail = document.getElementById('profileEmail');
    const profilePhone = document.getElementById('profilePhone');
    const profileTotalRides = document.getElementById('profileTotalRides');
    const profileAvgRating = document.getElementById('profileAvgRating');
    const memberSince = document.getElementById('memberSince');
    
    if (profileName) profileName.textContent = user.name;
    if (profileEmail) profileEmail.textContent = user.email;
    if (profilePhone) profilePhone.textContent = user.phone;
    if (profileTotalRides) profileTotalRides.textContent = user.totalRides;
    if (profileAvgRating) profileAvgRating.textContent = (user.avgRating || 0) + ' ⭐';
    if (memberSince) memberSince.textContent = user.joinDate;
    
    // Fill edit form
    const editName = document.getElementById('editName');
    const editPhone = document.getElementById('editPhone');
    if (editName) editName.value = user.name;
    if (editPhone) editPhone.value = user.phone;
}

// ============================================
// RIDE HISTORY FUNCTIONS
// ============================================

function loadRideHistory() {
    const user = getLoggedInUser();
    if (!user) return;
    
    const userRides = appData.rides.filter(r => r.userId === user.id);
    const rideHistoryList = document.getElementById('rideHistoryList');
    
    if (!rideHistoryList) return;
    
    if (userRides.length === 0) {
        rideHistoryList.innerHTML = '<p class="empty-state">No ride history</p>';
        return;
    }
    
    rideHistoryList.innerHTML = userRides.map(ride => `
        <div class="ride-card">
            <div class="ride-info">
                <h4>${ride.pickupLocation} → ${ride.dropoffLocation}</h4>
                <p>${ride.distance} km | ${ride.date}</p>
            </div>
            <div class="ride-status">
                <span class="badge ${ride.status === 'completed' ? 'badge-success' : 'badge-warning'}">${ride.status}</span>
            </div>
            <div class="ride-fare">
                <strong>₹${ride.fare.toFixed(2)}</strong>
            </div>
        </div>
    `).join('');
}

function filterRideHistory() {
    const filter = document.getElementById('historyFilter').value;
    const user = getLoggedInUser();
    if (!user) return;
    
    let userRides = appData.rides.filter(r => r.userId === user.id);
    
    if (filter !== 'all') {
        userRides = userRides.filter(r => r.status === filter);
    }
    
    const rideHistoryList = document.getElementById('rideHistoryList');
    if (rideHistoryList) {
        rideHistoryList.innerHTML = userRides.map(ride => `
            <div class="ride-card">
                <div class="ride-info">
                    <h4>${ride.pickupLocation} → ${ride.dropoffLocation}</h4>
                    <p>${ride.distance} km | ${ride.date}</p>
                </div>
                <div class="ride-status">
                    <span class="badge ${ride.status === 'completed' ? 'badge-success' : 'badge-warning'}">${ride.status}</span>
                </div>
                <div class="ride-fare">
                    <strong>₹${ride.fare.toFixed(2)}</strong>
                </div>
            </div>
        `).join('') || '<p class="empty-state">No rides found</p>';
    }
}

// ============================================
// INITIALIZATION
// ============================================

document.addEventListener('DOMContentLoaded', function() {
    // Check if this is a dashboard page
    if (window.location.pathname.includes('dashboard')) {
        checkAuthentication();
    }
});
