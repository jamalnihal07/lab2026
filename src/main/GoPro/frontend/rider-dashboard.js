// ============================================
// GoPro Rider Dashboard - Enhanced Interactive JavaScript
// ============================================

let map;
let pickupMarker, dropoffMarker;
let selectedVehicle = 'MINI';
let appliedCoupon = null;
let currentFare = 225;

// Initialize map on page load
function initMap() {
    if (!document.getElementById('map')) return;

    // Create map centered on Delhi
    map = L.map('map').setView([28.6139, 77.2090], 12);

    // Add tile layer
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors',
        maxZoom: 19
    }).addTo(map);

    // Add pickup marker
    pickupMarker = L.marker([28.6139, 77.2090], {
        icon: L.divIcon({
            html: '<div style="background: #FF6B6B; color: white; border-radius: 50%; width: 30px; height: 30px; display: flex; align-items: center; justify-content: center; font-weight: bold; border: 3px solid white; box-shadow: 0 2px 5px rgba(0,0,0,0.3);">📍</div>',
            iconSize: [30, 30]
        })
    }).addTo(map);

    // Add dropoff marker
    dropoffMarker = L.marker([28.5721, 77.3386], {
        icon: L.divIcon({
            html: '<div style="background: #4ECDC4; color: white; border-radius: 50%; width: 30px; height: 30px; display: flex; align-items: center; justify-content: center; font-weight: bold; border: 3px solid white; box-shadow: 0 2px 5px rgba(0,0,0,0.3);">🎯</div>',
            iconSize: [30, 30]
        })
    }).addTo(map);

    // Draw line between points
    drawRouteLine();
    updateFareEstimate();
}

// Draw route line
function drawRouteLine() {
    if (!map) return;
    
    const latlngs = [
        [parseFloat(document.getElementById('pickupLat')?.textContent || 28.6139), 
         parseFloat(document.getElementById('pickupLng')?.textContent || 77.2090)],
        [parseFloat(document.getElementById('dropoffLat')?.textContent || 28.5721), 
         parseFloat(document.getElementById('dropoffLng')?.textContent || 77.3386)]
    ];
    
    L.polyline(latlngs, {
        color: '#FF6B6B',
        weight: 3,
        opacity: 0.7,
        dashArray: '5, 5'
    }).addTo(map);
}

// Swap locations
function swapLocations() {
    const pickupInput = document.getElementById('pickupInput');
    const dropoffInput = document.getElementById('dropoffInput');
    const pickupLat = document.getElementById('pickupLat');
    const pickupLng = document.getElementById('pickupLng');
    const dropoffLat = document.getElementById('dropoffLat');
    const dropoffLng = document.getElementById('dropoffLng');

    if (!pickupInput || !dropoffInput) return;

    [pickupInput.value, dropoffInput.value] = [dropoffInput.value, pickupInput.value];
    [pickupLat.textContent, dropoffLat.textContent] = [dropoffLat.textContent, pickupLat.textContent];
    [pickupLng.textContent, dropoffLng.textContent] = [dropoffLng.textContent, pickupLng.textContent];

    if (map && pickupMarker && dropoffMarker) {
        pickupMarker.setLatLng([parseFloat(pickupLat.textContent), parseFloat(pickupLng.textContent)]);
        dropoffMarker.setLatLng([parseFloat(dropoffLat.textContent), parseFloat(dropoffLng.textContent)]);
    }

    updateFareEstimate();
    showNotification('Locations swapped!', 'success');
}

function loadRecentRides() {
    const user = getLoggedInUser();
    if (!user) return;
    
    const userRides = appData.rides
        .filter(r => r.userId === user.id)
        .sort((a, b) => new Date(b.date) - new Date(a.date))
        .slice(0, 5);
    
    const recentRidesList = document.getElementById('recentRidesList');
    if (!recentRidesList) return;
    
    if (userRides.length === 0) {
        recentRidesList.innerHTML = '<p class="empty-state">No recent rides</p>';
        return;
    }
    
    recentRidesList.innerHTML = userRides.map(ride => `
        <div class="ride-card">
            <div class="ride-info">
                <h4>${ride.pickupLocation}</h4>
                <p>To: ${ride.dropoffLocation}</p>
                <p class="time">${ride.date} | ${ride.distance} km</p>
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

function bookRide(event) {
    event.preventDefault();
    
    const user = getLoggedInUser();
    if (!user) {
        showNotification('Please login first', 'error');
        return;
    }
    
    const pickupLocation = document.getElementById('pickupLocation').value;
    const dropoffLocation = document.getElementById('dropoffLocation').value;
    const distance = parseFloat(document.getElementById('distance').value);
    const rideType = document.getElementById('rideType').value;
    const promoCode = document.getElementById('promoCode').value;
    
    if (!pickupLocation || !dropoffLocation || !distance) {
        showNotification('Please fill all fields', 'error');
        return;
    }
    
    // Calculate fare
    const rateMap = {
        'economy': 10,
        'comfort': 15,
        'premium': 20
    };
    
    const baseFare = 50;
    const perKmRate = rateMap[rideType];
    const estimatedFare = baseFare + (distance * perKmRate);
    
    // Create new ride
    const newRide = {
        id: 'R' + Math.floor(Math.random() * 10000).toString().padStart(4, '0'),
        userId: user.id,
        driverId: appData.drivers[0]?.id || 'D001',
        pickupLocation: pickupLocation,
        dropoffLocation: dropoffLocation,
        distance: distance,
        fare: estimatedFare,
        rideType: rideType,
        status: 'active',
        date: new Date().toISOString().split('T')[0],
        rating: 0,
        promoCode: promoCode,
        driver: appData.drivers[0] || {
            id: 'D001',
            name: 'Available Driver',
            avgRating: 4.8,
            phone: '9876543220'
        }
    };
    
    appData.rides.push(newRide);
    
    // Update user stats
    user.totalRides = (user.totalRides || 0) + 1;
    user.totalSpent = (user.totalSpent || 0) + estimatedFare;
    sessionStorage.setItem(USER_KEY, JSON.stringify(user));
    
    showNotification('Ride booked successfully! Driver on the way!', 'success');
    
    // Reset form
    document.querySelector('.ride-form').reset();
    
    // Load active ride
    setTimeout(() => {
        loadActiveRide(newRide);
        switchSection('active-ride');
    }, 500);
}

function loadActiveRide(ride = null) {
    const user = getLoggedInUser();
    if (!user) return;
    
    // Get active ride
    if (!ride) {
        ride = appData.rides.find(r => r.userId === user.id && r.status === 'active');
    }
    
    const activeRideInfo = document.getElementById('activeRideInfo');
    if (!activeRideInfo) return;
    
    if (!ride) {
        activeRideInfo.innerHTML = '<p class="empty-state">No active ride</p>';
        return;
    }
    
    const driver = ride.driver || {
        name: 'Your Driver',
        avgRating: 4.8,
        phone: '9876543220'
    };
    
    activeRideInfo.innerHTML = `
        <div class="ride-details-card">
            <div class="detail-item">
                <h4>Pickup Location</h4>
                <p>${ride.pickupLocation}</p>
            </div>
            <div class="detail-item">
                <h4>Dropoff Location</h4>
                <p>${ride.dropoffLocation}</p>
            </div>
            <div class="detail-item">
                <h4>Distance</h4>
                <p>${ride.distance} km</p>
            </div>
            <div class="detail-item">
                <h4>Estimated Fare</h4>
                <p>₹${ride.fare.toFixed(2)}</p>
            </div>
            
            <div style="margin-top: 20px; padding-top: 20px; border-top: 1px solid var(--border-color);">
                <h3>Driver Details</h3>
                <div class="detail-item" style="margin-top: 15px;">
                    <h4>Driver Name</h4>
                    <p>${driver.name}</p>
                </div>
                <div class="detail-item">
                    <h4>Rating</h4>
                    <p>${driver.avgRating || 4.8} ⭐</p>
                </div>
                <div class="detail-item">
                    <h4>Phone</h4>
                    <p>${driver.phone}</p>
                </div>
            </div>
            
            <div class="map-placeholder">
                📍 Live tracking map would appear here
            </div>
            
            <div class="action-buttons-container">
                <button class="btn btn-primary" onclick="completeRide('${ride.id}')">Complete Ride</button>
                <button class="btn btn-secondary" onclick="cancelRideAction('${ride.id}')">Cancel Ride</button>
                <button class="btn btn-secondary" onclick="sosEmergency('${ride.id}')">🚨 SOS</button>
            </div>
        </div>
    `;
}

function completeRide(rideId) {
    const ride = appData.rides.find(r => r.id === rideId);
    if (ride) {
        ride.status = 'completed';
        showNotification('Ride completed! Please rate your driver.', 'success');
        
        // Show rating prompt
        const rating = prompt('Rate your driver (1-5 stars):');
        if (rating && rating >= 1 && rating <= 5) {
            ride.rating = parseInt(rating);
            showNotification(`Thank you for rating ${rating} stars!`, 'success');
        }
        
        loadActiveRide();
    }
}

function cancelRideAction(rideId) {
    if (confirm('Are you sure you want to cancel this ride?')) {
        const ride = appData.rides.find(r => r.id === rideId);
        if (ride) {
            ride.status = 'cancelled';
            showNotification('Ride cancelled', 'success');
            loadActiveRide();
        }
    }
}

function sosEmergency(rideId) {
    const ride = appData.rides.find(r => r.id === rideId);
    if (ride) {
        showNotification('🚨 Emergency alert sent! Support team notified.', 'success');
        console.log('SOS activated for ride:', rideId);
    }
}

async function addMoneyToWallet(event) {
    event.preventDefault();
    
    const user = getLoggedInUser();
    if (!user) return;
    
    const amount = parseFloat(document.getElementById('addAmount').value);
    const paymentMethod = document.getElementById('paymentMethod').value;
    
    if (amount < 100) {
        showNotification('Minimum amount is ₹100', 'error');
        return;
    }

    try {
        const response = await apiRequest('/api/payments/checkout', {
            method: 'POST',
            body: JSON.stringify({
                userId: user.id,
                amount,
                method: paymentMethod,
                purpose: 'wallet-topup',
                provider: paymentMethod
            })
        });

        user.wallet = response.walletBalance ?? ((user.wallet || 0) + amount);
        sessionStorage.setItem(USER_KEY, JSON.stringify(user));

        const walletBalance = document.getElementById('walletBalance');
        if (walletBalance) walletBalance.textContent = '₹' + user.wallet;

        const currentBalance = document.getElementById('currentBalance');
        if (currentBalance) currentBalance.textContent = '₹' + user.wallet;

        showNotification(`₹${amount} added to wallet via ${paymentMethod}!`, 'success');
        document.querySelector('.add-money-form').reset();
    } catch (error) {
        user.wallet = (user.wallet || 0) + amount;
        sessionStorage.setItem(USER_KEY, JSON.stringify(user));

        const walletBalance = document.getElementById('walletBalance');
        if (walletBalance) walletBalance.textContent = '₹' + user.wallet;

        const currentBalance = document.getElementById('currentBalance');
        if (currentBalance) currentBalance.textContent = '₹' + user.wallet;

        showNotification(`₹${amount} added to wallet via ${paymentMethod}! (offline fallback)`, 'success');
        document.querySelector('.add-money-form').reset();
    }
}

function updateProfile(event) {
    event.preventDefault();
    
    const user = getLoggedInUser();
    if (!user) return;
    
    const name = document.getElementById('editName').value;
    const phone = document.getElementById('editPhone').value;
    
    if (!name || !phone) {
        showNotification('Please fill all fields', 'error');
        return;
    }
    
    user.name = name;
    user.phone = phone;
    sessionStorage.setItem(USER_KEY, JSON.stringify(user));
    
    showNotification('Profile updated successfully!', 'success');
    loadProfile();
}

function filterRideHistory() {
    const filter = document.getElementById('historyFilter').value;
    const user = getLoggedInUser();
    if (!user) return;
    
    let userRides = appData.rides
        .filter(r => r.userId === user.id)
        .sort((a, b) => new Date(b.date) - new Date(a.date));
    
    if (filter !== 'all') {
        userRides = userRides.filter(r => r.status === filter);
    }
    
    const rideHistoryList = document.getElementById('rideHistoryList');
    if (!rideHistoryList) return;
    
    if (userRides.length === 0) {
        rideHistoryList.innerHTML = '<p class="empty-state">No rides found</p>';
        return;
    }
    
    rideHistoryList.innerHTML = userRides.map(ride => `
        <div class="ride-card">
            <div class="ride-info">
                <h4>${ride.pickupLocation}</h4>
                <p>To: ${ride.dropoffLocation}</p>
                <p class="time">${ride.date} | ${ride.distance} km</p>
            </div>
            <div class="ride-status">
                <span class="badge ${ride.status === 'completed' ? 'badge-success' : 'badge-warning'}">${ride.status}</span>
            </div>
            <div class="ride-fare">
                <strong>₹${ride.fare.toFixed(2)}</strong>
                ${ride.rating ? `<p>Rating: ${ride.rating} ⭐</p>` : ''}
            </div>
        </div>
    `).join('');
}

function loadProfile() {
    const user = getLoggedInUser();
    if (!user) return;
    
    // Update profile display
    const profileName = document.getElementById('profileName');
    const profileEmail = document.getElementById('profileEmail');
    const profilePhone = document.getElementById('profilePhone');
    const profileTotalRides = document.getElementById('profileTotalRides');
    const profileAvgRating = document.getElementById('profileAvgRating');
    const memberSince = document.getElementById('memberSince');
    
    if (profileName) profileName.textContent = user.name;
    if (profileEmail) profileEmail.textContent = user.email;
    if (profilePhone) profilePhone.textContent = user.phone;
    if (profileTotalRides) profileTotalRides.textContent = user.totalRides || 0;
    if (profileAvgRating) profileAvgRating.textContent = (user.avgRating || 0) + ' ⭐';
    if (memberSince) memberSince.textContent = user.joinDate || '2024';
    
    // Update edit form
    const editName = document.getElementById('editName');
    const editPhone = document.getElementById('editPhone');
    if (editName) editName.value = user.name;
    if (editPhone) editPhone.value = user.phone;
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    // Check if user is logged in
    const user = getLoggedInUser();
    if (!user || user.type !== 'rider') {
        window.location.href = 'index.html';
        return;
    }
    
    // Load all data
    loadDashboardData();
    loadRecentRides();
    loadProfile();
    setupEventListeners();
    
    // Set initial page as dashboard
    switchSection('dashboard');
});
