// ============================================
// GoPro - Admin Dashboard JavaScript
// ============================================

document.addEventListener('DOMContentLoaded', function() {
    const user = checkAuthentication();
    if (!user || user.type !== 'admin') {
        window.location.href = 'index.html';
        return;
    }
    
    // Initialize admin dashboard
    loadAdminDashboard();
    setupAdminEventListeners();
    
    // Set initial section
    switchAdminSection('overview');
});

function setupAdminEventListeners() {
    // Users section
    const userSearchBox = document.getElementById('userSearchBox');
    if (userSearchBox) {
        userSearchBox.addEventListener('keyup', searchUsers);
    }
    
    const userStatusFilter = document.getElementById('userStatusFilter');
    if (userStatusFilter) {
        userStatusFilter.addEventListener('change', filterUsers);
    }
    
    // Drivers section
    const driverSearchBox = document.getElementById('driverSearchBox');
    if (driverSearchBox) {
        driverSearchBox.addEventListener('keyup', searchDrivers);
    }
    
    const driverStatusFilter = document.getElementById('driverStatusFilter');
    if (driverStatusFilter) {
        driverStatusFilter.addEventListener('change', filterDrivers);
    }
}

function loadAdminDashboard() {
    // Update KPI cards
    const totalUsers = document.getElementById('totalUsers');
    const activeDrivers = document.getElementById('activeDrivers');
    const todayRevenue = document.getElementById('todayRevenue');
    const completedRides = document.getElementById('completedRides');
    
    if (totalUsers) totalUsers.textContent = appData.users.length;
    if (activeDrivers) activeDrivers.textContent = appData.drivers.length;
    if (todayRevenue) todayRevenue.textContent = '₹45,320';
    if (completedRides) completedRides.textContent = appData.rides.filter(r => r.status === 'completed').length;
    
    // Load users table
    loadUsersTable();
    loadDriversTable();
    loadRidesTable();
    loadPaymentsTable();
    loadAdminActivityList();
}

function loadUsersTable() {
    const usersTable = document.getElementById('usersTable');
    if (!usersTable) return;
    
    usersTable.innerHTML = appData.users.map(user => `
        <tr>
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.phone}</td>
            <td>${user.totalRides}</td>
            <td><span class="badge badge-success">Active</span></td>
            <td>
                <button class="btn-action" onclick="editUser('${user.id}')">Edit</button>
                <button class="btn-action danger" onclick="deleteUser('${user.id}')">Delete</button>
            </td>
        </tr>
    `).join('');
}

function loadDriversTable() {
    const driversTable = document.getElementById('driversTable');
    if (!driversTable) return;
    
    driversTable.innerHTML = appData.drivers.map(driver => `
        <tr>
            <td>${driver.id}</td>
            <td>${driver.name}</td>
            <td>${driver.vehicle?.registration || 'N/A'}</td>
            <td>${driver.avgRating} ⭐</td>
            <td>${driver.totalRides}</td>
            <td><span class="badge ${driver.verified ? 'badge-success' : 'badge-warning'}">${driver.verified ? 'Verified' : 'Pending'}</span></td>
            <td>
                <button class="btn-action" onclick="editDriver('${driver.id}')">Edit</button>
                <button class="btn-action warning" onclick="suspendDriver('${driver.id}')">Suspend</button>
            </td>
        </tr>
    `).join('');
}

function loadRidesTable() {
    const ridesTable = document.getElementById('ridesTable');
    if (!ridesTable) return;
    
    const rides = appData.rides.slice(0, 10);
    
    ridesTable.innerHTML = rides.map(ride => {
        const user = appData.users.find(u => u.id === ride.userId);
        const driver = appData.drivers.find(d => d.id === ride.driverId);
        
        return `
            <tr>
                <td>${ride.id}</td>
                <td>${user?.name || 'N/A'}</td>
                <td>${driver?.name || 'N/A'}</td>
                <td>₹${ride.fare.toFixed(2)}</td>
                <td><span class="badge ${ride.status === 'completed' ? 'badge-success' : 'badge-warning'}">${ride.status}</span></td>
                <td>${ride.date}</td>
                <td>
                    <button class="btn-action" onclick="viewRideDetails('${ride.id}')">View</button>
                </td>
            </tr>
        `;
    }).join('');
}

function loadPaymentsTable() {
    const paymentsTable = document.getElementById('paymentsTable');
    if (!paymentsTable) return;
    
    const payments = [
        { id: 'P001', userId: 'U001', userName: 'Rahul Sharma', amount: 245, method: 'Card', status: 'completed', date: '2024-01-15 14:30' },
        { id: 'P002', userId: 'U002', userName: 'Priya Patel', amount: 350, method: 'UPI', status: 'completed', date: '2024-01-15 13:45' },
        { id: 'P003', userId: 'U001', userName: 'Rahul Sharma', amount: 500, method: 'NetBanking', status: 'pending', date: '2024-01-15 12:00' },
    ];
    
    paymentsTable.innerHTML = payments.map(payment => `
        <tr>
            <td>${payment.id}</td>
            <td>${payment.userName}</td>
            <td>₹${payment.amount}</td>
            <td>${payment.method}</td>
            <td><span class="badge ${payment.status === 'completed' ? 'badge-success' : 'badge-warning'}">${payment.status}</span></td>
            <td>${payment.date}</td>
        </tr>
    `).join('');
}

function loadAdminActivityList() {
    const adminActivityList = document.getElementById('adminActivityList');
    if (!adminActivityList) return;
    
    const activities = [
        { icon: '🚗', message: 'New ride booked - U001 to U002', time: '2 minutes ago' },
        { icon: '💳', message: 'Payment processed - ₹245', time: '5 minutes ago' },
        { icon: '📝', message: 'Driver D002 verified', time: '10 minutes ago' },
        { icon: '⚠️', message: 'Support ticket opened - Issue with payment', time: '15 minutes ago' },
    ];
    
    adminActivityList.innerHTML = activities.map(activity => `
        <div class="activity-item">
            <span class="activity-icon">${activity.icon}</span>
            <div class="activity-details">
                <p>${activity.message}</p>
                <span class="time">${activity.time}</span>
            </div>
        </div>
    `).join('');
}

// ============================================
// USER MANAGEMENT
// ============================================

function searchUsers() {
    const searchTerm = document.getElementById('userSearchBox').value.toLowerCase();
    const usersTable = document.getElementById('usersTable');
    if (!usersTable) return;
    
    const rows = usersTable.querySelectorAll('tr');
    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(searchTerm) ? '' : 'none';
    });
}

function filterUsers() {
    const filter = document.getElementById('userStatusFilter').value;
    // Implement filtering logic
    loadUsersTable();
}

function editUser(userId) {
    const user = appData.users.find(u => u.id === userId);
    if (user) {
        const newName = prompt('Enter new name:', user.name);
        if (newName) {
            user.name = newName;
            showNotification('User updated successfully!', 'success');
            loadUsersTable();
        }
    }
}

function deleteUser(userId) {
    if (confirm('Are you sure you want to delete this user?')) {
        const index = appData.users.findIndex(u => u.id === userId);
        if (index > -1) {
            appData.users.splice(index, 1);
            showNotification('User deleted successfully!', 'success');
            loadUsersTable();
        }
    }
}

function openUserForm() {
    alert('User creation form would open here');
}

// ============================================
// DRIVER MANAGEMENT
// ============================================

function searchDrivers() {
    const searchTerm = document.getElementById('driverSearchBox').value.toLowerCase();
    const driversTable = document.getElementById('driversTable');
    if (!driversTable) return;
    
    const rows = driversTable.querySelectorAll('tr');
    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(searchTerm) ? '' : 'none';
    });
}

function filterDrivers() {
    const filter = document.getElementById('driverStatusFilter').value;
    // Implement filtering logic
    loadDriversTable();
}

function editDriver(driverId) {
    const driver = appData.drivers.find(d => d.id === driverId);
    if (driver) {
        const newName = prompt('Enter new name:', driver.name);
        if (newName) {
            driver.name = newName;
            showNotification('Driver updated successfully!', 'success');
            loadDriversTable();
        }
    }
}

function suspendDriver(driverId) {
    if (confirm('Are you sure you want to suspend this driver?')) {
        const driver = appData.drivers.find(d => d.id === driverId);
        if (driver) {
            driver.verified = false;
            showNotification('Driver suspended!', 'success');
            loadDriversTable();
        }
    }
}

function openDriverForm() {
    alert('Driver creation form would open here');
}

// ============================================
// RIDES MANAGEMENT
// ============================================

function filterRides() {
    const filter = document.getElementById('ridesStatusFilter').value;
    // Implement filtering logic
    loadRidesTable();
}

function viewRideDetails(rideId) {
    const ride = appData.rides.find(r => r.id === rideId);
    if (ride) {
        const user = appData.users.find(u => u.id === ride.userId);
        const driver = appData.drivers.find(d => d.id === ride.driverId);
        
        alert(`
Ride Details:
ID: ${ride.id}
User: ${user?.name}
Driver: ${driver?.name}
From: ${ride.pickupLocation}
To: ${ride.dropoffLocation}
Distance: ${ride.distance} km
Fare: ₹${ride.fare.toFixed(2)}
Status: ${ride.status}
        `);
    }
}

// ============================================
// PAYMENT MANAGEMENT
// ============================================

function filterPayments() {
    const filter = document.getElementById('paymentStatusFilter').value;
    // Implement filtering logic
    loadPaymentsTable();
}

// ============================================
// REPORTS
// ============================================

function generateReport(event) {
    event.preventDefault();
    
    const reportType = document.getElementById('reportType').value;
    const dateFrom = document.getElementById('reportDateFrom').value;
    const dateTo = document.getElementById('reportDateTo').value;
    
    if (!dateFrom || !dateTo) {
        alert('Please select both dates');
        return;
    }
    
    showNotification(`${reportType} report generated from ${dateFrom} to ${dateTo}!`, 'success');
    
    // Here you would typically generate and download the report
    console.log(`Generating ${reportType} report...`);
}

// ============================================
// SUPPORT TICKETS
// ============================================

function filterTickets() {
    const filter = document.getElementById('ticketStatusFilter').value;
    // Implement filtering logic
    const ticketsTable = document.getElementById('ticketsTable');
    if (!ticketsTable) return;
    
    // Show/hide rows based on filter
}

function resolveTicket(ticketId) {
    const response = prompt('Enter resolution notes:');
    if (response) {
        showNotification('Ticket resolved and customer notified!', 'success');
    }
}

// ============================================
// SETTINGS
// ============================================

function toggleMaintenance() {
    const checkbox = document.getElementById('maintenanceMode');
    if (checkbox.checked) {
        showNotification('Maintenance mode enabled!', 'success');
    } else {
        showNotification('Maintenance mode disabled!', 'success');
    }
}

function saveSettings(event) {
    event.preventDefault();
    
    const baseFare = document.getElementById('baseFare').value;
    const perKmRate = document.getElementById('perKmRate').value;
    const adminCommission = document.getElementById('adminCommission').value;
    
    const settings = {
        baseFare: parseFloat(baseFare),
        perKmRate: parseFloat(perKmRate),
        adminCommission: parseFloat(adminCommission)
    };
    
    // Save to localStorage
    localStorage.setItem('gopro_settings', JSON.stringify(settings));
    
    showNotification('Settings saved successfully!', 'success');
}

// ============================================
// INITIALIZATION
// ============================================

document.addEventListener('DOMContentLoaded', function() {
    const admin = getLoggedInUser();
    if (!admin || admin.type !== 'admin') {
        window.location.href = 'index.html';
        return;
    }
    
    // Load all admin data
    loadAdminDashboard();
    setupAdminEventListeners();
    
    // Set initial section
    switchAdminSection('overview');
});
