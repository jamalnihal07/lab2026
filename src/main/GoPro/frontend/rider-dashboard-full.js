// ============================================
// GoPro Rider Dashboard - Complete Enhanced JavaScript
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

// Select vehicle type
function selectVehicle(vehicleType, element) {
    selectedVehicle = vehicleType;

    // Update UI
    document.querySelectorAll('.vehicle-option').forEach(el => {
        el.classList.remove('selected');
    });
    element.classList.add('selected');

    updateFareEstimate();
    showNotification(`Selected ${vehicleType}`, 'info');
}

// Update fare estimate
function updateFareEstimate() {
    // Calculate distance (simplified)
    const distance = 18.5; // km
    const duration = 28; // minutes

    // Get base fare for selected vehicle
    const vehicleRates = {
        'BIKE': 5,
        'AUTO': 8,
        'MINI': 10,
        'SEDAN': 12,
        'SUV': 15
    };

    const ratePerKm = vehicleRates[selectedVehicle] || 10;
    const ratePerMin = 0.5;

    // Calculate fare
    const baseFare = Math.round((distance * ratePerKm + duration * ratePerMin) * 100) / 100;
    const surgePricing = 1.0;

    currentFare = Math.round((baseFare * surgePricing) * 100) / 100;

    // Update display
    const display = {
        distance: document.getElementById('distanceDisplay'),
        duration: document.getElementById('durationDisplay'),
        baseFare: document.getElementById('baseFareDisplay'),
        surge: document.getElementById('surgeDisplay'),
        total: document.getElementById('totalFareDisplay'),
        discount: document.getElementById('discountDisplay')
    };

    for (let key in display) {
        if (!display[key]) return;
    }

    display.distance.textContent = distance + ' km';
    display.duration.textContent = duration + ' mins';
    display.baseFare.textContent = 'Rs. ' + baseFare;
    display.surge.textContent = surgePricing.toFixed(1) + 'x';
    display.total.textContent = 'Rs. ' + currentFare;
    display.discount.textContent = 'Rs. 0';

    appliedCoupon = null;
    const couponInput = document.getElementById('couponCode');
    if (couponInput) couponInput.value = '';
}

// Apply coupon code
function applyCoupon() {
    const couponInput = document.getElementById('couponCode');
    if (!couponInput) return;

    const couponCode = couponInput.value.trim().toUpperCase();

    if (!couponCode) {
        showNotification('Please enter a coupon code', 'warning');
        return;
    }

    // Validate coupon
    const validCoupons = {
        'SAVE50': { discount: 50, percentage: 10, message: 'Save up to Rs.50!' },
        'RIDE25': { discount: 25, percentage: 5, message: 'Get 5% off this ride!' }
    };

    if (validCoupons[couponCode]) {
        const couponData = validCoupons[couponCode];
        const discountAmount = Math.min(couponData.discount, currentFare * (couponData.percentage / 100));
        appliedCoupon = couponCode;

        // Update fare display
        const newFare = Math.round((currentFare - discountAmount) * 100) / 100;
        const discountDisplay = document.getElementById('discountDisplay');
        const totalDisplay = document.getElementById('totalFareDisplay');
        
        if (discountDisplay) discountDisplay.textContent = '- Rs. ' + discountAmount;
        if (totalDisplay) totalDisplay.textContent = 'Rs. ' + newFare;

        // Show status
        const couponStatus = document.getElementById('couponStatus');
        if (couponStatus) {
            couponStatus.innerHTML = `
                <div style="background: #d4edda; color: #155724; padding: 10px; border-radius: 5px; border: 1px solid #c3e6cb;">
                    ✅ Coupon applied! You save Rs. ${discountAmount}
                </div>
            `;
        }

        showNotification(couponData.message, 'success');
    } else {
        const couponStatus = document.getElementById('couponStatus');
        if (couponStatus) {
            couponStatus.innerHTML = `
                <div style="background: #f8d7da; color: #721c24; padding: 10px; border-radius: 5px; border: 1px solid #f5c6cb;">
                    ❌ Invalid or expired coupon code
                </div>
            `;
        }
        showNotification('Invalid coupon code', 'error');
    }
}

// Apply coupon directly
function applyCouponDirect(code) {
    const couponInput = document.getElementById('couponCode');
    if (couponInput) couponInput.value = code;
    applyCoupon();
}

// Confirm and book ride
function confirmBooking() {
    const pickupInput = document.getElementById('pickupInput');
    const dropoffInput = document.getElementById('dropoffInput');
    const paymentMethod = document.getElementById('paymentMethod');

    if (!pickupInput || !dropoffInput || !paymentMethod) {
        showNotification('Please fill all fields', 'warning');
        return;
    }

    const pickupLocation = pickupInput.value;
    const dropoffLocation = dropoffInput.value;
    const payment = paymentMethod.value;

    if (!pickupLocation || !dropoffLocation) {
        showNotification('Please enter both pickup and dropoff locations', 'warning');
        return;
    }

    // Calculate final fare
    let finalFare = currentFare;
    if (appliedCoupon) {
        const discountText = document.getElementById('discountDisplay')?.textContent || 'Rs. 0';
        const discount = parseFloat(discountText.replace('- Rs. ', '').replace('Rs. ', ''));
        finalFare = currentFare - discount;
    }

    // Create booking object
    const booking = {
        rideId: 'RIDE' + Date.now(),
        vehicleType: selectedVehicle,
        pickupLocation: pickupLocation,
        dropoffLocation: dropoffLocation,
        fare: finalFare,
        paymentMethod: payment,
        couponApplied: appliedCoupon,
        timestamp: new Date().toLocaleString()
    };

    // Store booking
    localStorage.setItem('currentBooking', JSON.stringify(booking));

    // Show success modal
    showSuccessModal(booking);
}

// Show success modal
function showSuccessModal(booking) {
    const modal = document.getElementById('successModal');
    if (!modal) return;

    const rideInfo = document.getElementById('rideInfo');
    if (!rideInfo) return;

    rideInfo.innerHTML = `
        <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
            <span>Ride ID:</span>
            <strong>${booking.rideId}</strong>
        </div>
        <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
            <span>Vehicle:</span>
            <strong>${booking.vehicleType}</strong>
        </div>
        <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
            <span>From:</span>
            <strong>${booking.pickupLocation}</strong>
        </div>
        <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
            <span>To:</span>
            <strong>${booking.dropoffLocation}</strong>
        </div>
        <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
            <span>Fare:</span>
            <strong style="color: var(--primary-color); font-size: 1.1rem;">Rs. ${booking.fare}</strong>
        </div>
        <div style="display: flex; justify-content: space-between;">
            <span>Payment:</span>
            <strong>${booking.paymentMethod}</strong>
        </div>
    `;

    modal.style.display = 'flex';
    showNotification('Ride booked successfully!', 'success');

    // Animate entrance
    const content = modal.querySelector('.modal-content');
    if (content) content.style.animation = 'slideUp 0.5s ease';
}

// Close modal
function closeModal() {
    const modal = document.getElementById('successModal');
    if (modal) modal.style.display = 'none';
}

// Show notification
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        border-radius: 8px;
        z-index: 10000;
        animation: slideIn 0.3s ease;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
    `;

    const colors = {
        'success': '#27AE60',
        'error': '#E74C3C',
        'warning': '#F39C12',
        'info': '#3498DB'
    };

    notification.style.backgroundColor = colors[type] || colors['info'];
    notification.style.color = 'white';
    notification.style.fontWeight = '500';
    notification.textContent = message;

    document.body.appendChild(notification);

    // Auto remove
    setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => notification.remove(), 300);
    }, 3000);
}

// Logout function
function logout() {
    if (confirm('Are you sure you want to logout?')) {
        localStorage.removeItem('currentUser');
        window.location.href = 'index.html';
    }
}

// Close modal when clicking outside
document.addEventListener('click', function(event) {
    const modal = document.getElementById('successModal');
    if (modal && event.target === modal) {
        closeModal();
    }
});

// Add animation styles when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    const style = document.createElement('style');
    style.textContent = `
        @keyframes slideIn {
            from {
                transform: translateX(400px);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
            }
        }

        @keyframes slideOut {
            from {
                transform: translateX(0);
                opacity: 1;
            }
            to {
                transform: translateX(400px);
                opacity: 0;
            }
        }

        @keyframes slideUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            justify-content: center;
            align-items: center;
            z-index: 9999;
        }

        .modal-content {
            background: white;
            border-radius: 15px;
            padding: 40px;
            max-width: 500px;
            width: 90%;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
        }

        .btn-swap {
            background: var(--primary-color, #FF6B6B);
            color: white;
            border: none;
            width: 50px;
            height: 50px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 1.2rem;
            transition: all 0.3s ease;
            align-self: center;
        }

        .btn-swap:hover {
            background: var(--secondary-color, #4ECDC4);
            transform: scale(1.05);
        }

        .btn-small {
            padding: 8px 15px;
            font-size: 0.9rem;
        }

        .location-inputs {
            display: flex;
            gap: 10px;
            margin-top: 15px;
            align-items: flex-start;
        }

        .input-group {
            flex: 1;
        }

        .input-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: var(--text-dark, #2C3E50);
        }

        .input-group input {
            width: 100%;
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 8px;
            font-size: 1rem;
            transition: border-color 0.3s ease;
        }

        .input-group input:focus {
            outline: none;
            border-color: var(--primary-color, #FF6B6B);
        }

        .input-group small {
            display: block;
            margin-top: 5px;
            color: var(--text-light, #7F8C8D);
            font-size: 0.85rem;
        }

        @media (max-width: 768px) {
            .location-inputs {
                flex-wrap: wrap;
            }

            .btn-swap {
                width: 40px;
                height: 40px;
                font-size: 1rem;
            }
        }
    `;
    document.head.appendChild(style);
});
