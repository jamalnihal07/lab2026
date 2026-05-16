// ============================================
// GoPro - Driver Dashboard JavaScript
// ============================================

document.addEventListener('DOMContentLoaded', function() {
    const user = checkAuthentication();
    if (!user || user.type !== 'driver') {
        window.location.href = 'index.html';
        return;
    }
    
    // Initialize dashboard
    loadDriverDashboardData();
    loadAvailableRides();
    loadDriverEarnings();
    setupEventListeners();
    
    // Set initial section
    switchSection('driver-dashboard');
});

function setupEventListeners() {
    // Set up filters
    const ridesFilter = document.getElementById('ridesFilter');
    if (ridesFilter) {
        ridesFilter.addEventListener('change', filterAvailableRides);
    }
    
    const driverStatusFilter = document.getElementById('driverStatusFilter');
    if (driverStatusFilter) {
        driverStatusFilter.addEventListener('change', filterDrivers);
    }
}

function toggleOnlineStatus() {
    const checkbox = document.getElementById('onlineStatus');
    const statusText = document.getElementById('statusText');
    
    if (checkbox.checked) {
        statusText.textContent = 'Online';
        statusText.style.color = 'var(--success)';
        showNotification('You are now online! Ready to receive ride requests.', 'success');
    } else {
        statusText.textContent = 'Offline';
        statusText.style.color = 'var(--text-dark)';
        showNotification('You are now offline', 'success');
    }
}

function loadDriverDashboardData() {
    const driver = getLoggedInUser();
    if (!driver || driver.type !== 'driver') return;
    
    // Update stats
    const todayEarnings = document.getElementById('todayEarnings');
    const driverTotalRides = document.getElementById('driverTotalRides');
    const driverRating = document.getElementById('driverRating');
    const acceptanceRate = document.getElementById('acceptanceRate');
    
    if (todayEarnings) todayEarnings.textContent = '₹' + (driver.earnings?.today || 0);
    if (driverTotalRides) driverTotalRides.textContent = driver.totalRides || 0;
    if (driverRating) driverRating.textContent = (driver.avgRating || 4.8) + ' ⭐';
    if (acceptanceRate) acceptanceRate.textContent = '95%'; // Placeholder
    
    // Load profile
    loadDriverProfile();
}

function loadDriverProfile() {
    const driver = getLoggedInUser();
    if (!driver) return;
    
    // Update profile display
    const driverName = document.getElementById('driverName');
    const driverEmail = document.getElementById('driverEmail');
    const driverPhone = document.getElementById('driverPhone');
    
    if (driverName) driverName.textContent = driver.name;
    if (driverEmail) driverEmail.textContent = driver.email;
    if (driverPhone) driverPhone.textContent = driver.phone;
    
    // Vehicle info
    if (driver.vehicle) {
        const vehicleType = document.getElementById('vehicleType');
        const vehicleReg = document.getElementById('vehicleReg');
        const vehicleColor = document.getElementById('vehicleColor');
        const vehicleModel = document.getElementById('vehicleModel');
        
        if (vehicleType) vehicleType.textContent = driver.vehicle.type;
        if (vehicleReg) vehicleReg.textContent = driver.vehicle.registration;
        if (vehicleColor) vehicleColor.textContent = driver.vehicle.color;
        if (vehicleModel) vehicleModel.textContent = driver.vehicle.model;
    }
    
    // Edit form
    const editDriverName = document.getElementById('editDriverName');
    const editDriverPhone = document.getElementById('editDriverPhone');
    if (editDriverName) editDriverName.value = driver.name;
    if (editDriverPhone) editDriverPhone.value = driver.phone;
}

function loadAvailableRides() {
    // Get all active rides without drivers
    const availableRides = appData.rides.filter(r => r.status === 'active' && !r.driverId);
    
    const availableRidesList = document.getElementById('availableRidesList');
    if (!availableRidesList) return;
    
    if (availableRides.length === 0) {
        availableRidesList.innerHTML = '<p class="empty-state">No available rides</p>';
        return;
    }
    
    availableRidesList.innerHTML = availableRides.map(ride => `
        <div class="ride-card">
            <div class="ride-info">
                <h4>📍 ${ride.pickupLocation}</h4>
                <p>→ ${ride.dropoffLocation}</p>
                <p class="time">${ride.distance} km | Fare: ₹${ride.fare.toFixed(2)}</p>
            </div>
            <div class="action-buttons-container" style="gap: 5px;">
                <button class="btn btn-primary" style="margin: 0;" onclick="acceptRide('${ride.id}')">Accept</button>
                <button class="btn btn-secondary" style="margin: 0;" onclick="rejectRide('${ride.id}')">Reject</button>
            </div>
        </div>
    `).join('');
}

function acceptRide(rideId) {
    const driver = getLoggedInUser();
    if (!driver) return;
    
    const ride = appData.rides.find(r => r.id === rideId);
    if (ride) {
        ride.driverId = driver.id;
        ride.driver = {
            id: driver.id,
            name: driver.name,
            avgRating: driver.avgRating,
            phone: driver.phone
        };
        
        showNotification(`Ride ${rideId} accepted! Head to pickup location.`, 'success');
        loadAvailableRides();
        loadCurrentRide();
    }
}

function rejectRide(rideId) {
    showNotification('Ride rejected', 'success');
    loadAvailableRides();
}

function loadCurrentRide() {
    const driver = getLoggedInUser();
    if (!driver) return;
    
    const currentRide = appData.rides.find(r => r.driverId === driver.id && r.status === 'active');
    const currentRideInfo = document.getElementById('currentRideInfo');
    
    if (!currentRideInfo) return;
    
    if (!currentRide) {
        currentRideInfo.innerHTML = '<p class="empty-state">No active ride</p>';
        return;
    }
    
    currentRideInfo.innerHTML = `
        <div class="ride-details-card">
            <div class="detail-item">
                <h4>Pickup Location</h4>
                <p>${currentRide.pickupLocation}</p>
            </div>
            <div class="detail-item">
                <h4>Dropoff Location</h4>
                <p>${currentRide.dropoffLocation}</p>
            </div>
            <div class="detail-item">
                <h4>Distance</h4>
                <p>${currentRide.distance} km</p>
            </div>
            <div class="detail-item">
                <h4>Fare</h4>
                <p>₹${currentRide.fare.toFixed(2)}</p>
            </div>
            
            <div style="margin-top: 20px; padding-top: 20px; border-top: 1px solid var(--border-color);">
                <h3>Passenger Details</h3>
                <div class="detail-item" style="margin-top: 15px;">
                    <h4>Passenger</h4>
                    <p>${findUserName(currentRide.userId)}</p>
                </div>
            </div>
            
            <div class="map-placeholder">
                📍 Live tracking map would appear here
            </div>
            
            <div class="action-buttons-container">
                <button class="btn btn-primary" onclick="startRide('${currentRide.id}')">Start Ride</button>
                <button class="btn btn-secondary" onclick="cancelCurrentRide('${currentRide.id}')">Cancel</button>
                <button class="btn btn-secondary" onclick="callPassenger('${currentRide.id}')">📞 Call</button>
            </div>
        </div>
    `;
}

function startRide(rideId) {
    const ride = appData.rides.find(r => r.id === rideId);
    if (ride) {
        ride.status = 'in-progress';
        showNotification('Ride started!', 'success');
        loadCurrentRide();
    }
}

function cancelCurrentRide(rideId) {
    if (confirm('Are you sure you want to cancel this ride?')) {
        const ride = appData.rides.find(r => r.id === rideId);
        if (ride) {
            ride.status = 'cancelled';
            ride.driverId = null;
            showNotification('Ride cancelled', 'success');
            loadCurrentRide();
            loadAvailableRides();
        }
    }
}

function callPassenger(rideId) {
    showNotification('Calling passenger...', 'success');
}

function findUserName(userId) {
    const user = appData.users.find(u => u.id === userId);
    return user ? user.name : 'Unknown Passenger';
}

function loadDriverEarnings() {
    const driver = getLoggedInUser();
    if (!driver) return;
    
    const earningsToday = document.getElementById('earningsToday');
    const earningsWeek = document.getElementById('earningsWeek');
    const earningsMonth = document.getElementById('earningsMonth');
    const earningsTotal = document.getElementById('earningsTotal');
    
    if (earningsToday) earningsToday.textContent = '₹' + (driver.earnings?.today || 0);
    if (earningsWeek) earningsWeek.textContent = '₹' + (driver.earnings?.week || 0);
    if (earningsMonth) earningsMonth.textContent = '₹' + (driver.earnings?.month || 0);
    if (earningsTotal) earningsTotal.textContent = '₹' + (driver.earnings?.total || 0);
    
    // Populate earnings table
    const earningsTable = document.getElementById('earningsTable');
    if (earningsTable) {
        const earningsData = [
            { date: '2024-01-15', rides: 12, distance: 125, earnings: 1850 },
            { date: '2024-01-14', rides: 10, distance: 98, earnings: 1450 },
            { date: '2024-01-13', rides: 8, distance: 75, earnings: 1100 },
        ];
        
        earningsTable.innerHTML = earningsData.map(row => `
            <tr>
                <td>${row.date}</td>
                <td>${row.rides}</td>
                <td>${row.distance} km</td>
                <td>₹${row.earnings}</td>
            </tr>
        `).join('');
    }
}

function loadDriverActivityList() {
    const driverActivityList = document.getElementById('driverActivityList');
    if (!driverActivityList) return;
    
    const activities = [
        { icon: '✅', message: 'Ride completed successfully', time: '2 hours ago' },
        { icon: '⭐', message: 'Received 5 star rating from passenger', time: '3 hours ago' },
        { icon: '💰', message: 'Earned ₹245 from last ride', time: '3 hours ago' },
        { icon: '🚗', message: 'Accepted a new ride request', time: '5 hours ago' },
    ];
    
    driverActivityList.innerHTML = activities.map(activity => `
        <div class="activity-item">
            <span class="activity-icon">${activity.icon}</span>
            <div class="activity-details">
                <p>${activity.message}</p>
                <span class="time">${activity.time}</span>
            </div>
        </div>
    `).join('');
}

function filterAvailableRides() {
    const filter = document.getElementById('ridesFilter').value;
    let rides = appData.rides.filter(r => r.status === 'active' && !r.driverId);
    
    if (filter !== 'all') {
        // Apply additional filtering logic if needed
    }
    
    const availableRidesList = document.getElementById('availableRidesList');
    if (!availableRidesList) return;
    
    if (rides.length === 0) {
        availableRidesList.innerHTML = '<p class="empty-state">No available rides</p>';
        return;
    }
    
    availableRidesList.innerHTML = rides.map(ride => `
        <div class="ride-card">
            <div class="ride-info">
                <h4>📍 ${ride.pickupLocation}</h4>
                <p>→ ${ride.dropoffLocation}</p>
                <p class="time">${ride.distance} km | Fare: ₹${ride.fare.toFixed(2)}</p>
            </div>
            <div class="action-buttons-container" style="gap: 5px;">
                <button class="btn btn-primary" style="margin: 0;" onclick="acceptRide('${ride.id}')">Accept</button>
                <button class="btn btn-secondary" style="margin: 0;" onclick="rejectRide('${ride.id}')">Reject</button>
            </div>
        </div>
    `).join('');
}

function updateDriverProfile(event) {
    event.preventDefault();
    
    const driver = getLoggedInUser();
    if (!driver) return;
    
    const name = document.getElementById('editDriverName').value;
    const phone = document.getElementById('editDriverPhone').value;
    
    if (!name || !phone) {
        showNotification('Please fill all fields', 'error');
        return;
    }
    
    driver.name = name;
    driver.phone = phone;
    sessionStorage.setItem(USER_KEY, JSON.stringify(driver));
    
    showNotification('Profile updated successfully!', 'success');
    loadDriverProfile();
}

function filterDrivers() {
    // Placeholder for driver filtering logic
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    const driver = getLoggedInUser();
    if (!driver || driver.type !== 'driver') {
        window.location.href = 'index.html';
        return;
    }
    
    // Load all data
    loadDriverDashboardData();
    loadAvailableRides();
    loadCurrentRide();
    loadDriverEarnings();
    loadDriverActivityList();
    setupEventListeners();
    
    // Set initial section
    switchSection('driver-dashboard');
});
