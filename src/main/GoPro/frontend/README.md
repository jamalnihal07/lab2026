# GoPro Frontend - Interactive Web Application

A comprehensive, responsive web-based frontend for the GoPro Ride-Hailing application built with HTML, CSS, and vanilla JavaScript.

## 📁 Project Structure

```
frontend/
├── index.html                 # Login & Registration page
├── rider-dashboard.html       # Rider dashboard & features
├── driver-dashboard.html      # Driver dashboard & earnings
├── admin-dashboard.html       # Admin panel & management
├── styles.css                 # Unified styling for all pages
├── app.js                     # Core authentication & shared functions
├── rider-dashboard.js         # Rider-specific functionality
├── driver-dashboard.js        # Driver-specific functionality
├── admin-dashboard.js         # Admin-specific functionality
└── README.md                  # This file
```

## 🚀 Getting Started

### Prerequisites
- Any modern web browser (Chrome, Firefox, Safari, Edge)
- No server or backend required for basic functionality
- All data is stored in session storage and local storage

### Installation

1. **Extract the frontend folder** to your desired location
2. **Open in browser**: Double-click `index.html` or right-click → Open with Browser
3. **Or run a local server** (recommended):
   ```bash
   # Using Python 3
   python -m http.server 8000
   
   # Using Node.js (with http-server)
   npx http-server
   ```
4. **Navigate to**: `http://localhost:8000`

## 👤 Demo Credentials

### Rider Login
- **Email**: `rahul@email.com`
- **Password**: `pass123`
- **Type**: Rider

### Driver Login
- **Email**: `anuj@email.com`
- **Password**: `driver123`
- **Type**: Driver

### Admin Login
- **Email**: `admin@gopro.com`
- **Password**: `admin123`
- **Type**: Admin

## 📱 Pages & Features

### 1. **Authentication Page** (`index.html`)
- User registration and login
- Support for Rider, Driver, and Admin accounts
- OTP verification (simulated)
- Password validation

#### Features:
- Toggle between Login and Sign Up forms
- Account type selection
- Email and password validation
- Secure session management

### 2. **Rider Dashboard** (`rider-dashboard.html`)

#### Key Sections:

**Dashboard**
- View total rides, wallet balance, and average rating
- Quick action buttons for booking rides
- Recent rides summary

**Book a Ride**
- Enter pickup and dropoff locations
- Select ride type (Economy, Comfort, Premium)
- View real-time fare estimation
- Apply promo codes
- Book rides with a single click

**Active Ride**
- Live driver details and ratings
- Real-time pickup location tracking
- Ride status updates
- SOS emergency feature
- Option to rate driver upon completion

**Ride History**
- View all past rides
- Filter by status (All, Completed, Cancelled)
- See ride details and ratings
- Search functionality

**Wallet Management**
- View current balance
- Add money via multiple payment methods
  - Credit/Debit Card
  - UPI
  - Net Banking
- Transaction history

**Profile Management**
- View personal information
- Edit name and phone number
- View membership since date
- Ride statistics

### 3. **Driver Dashboard** (`driver-dashboard.html`)

#### Key Sections:

**Dashboard**
- Today's earnings
- Total rides completed
- Current rating
- Acceptance rate
- Recent activity feed

**Available Ride Requests**
- Browse available ride requests
- View pickup/dropoff locations
- See estimated distance and fare
- Accept or reject ride requests
- Filter nearby rides

**Current Ride**
- Passenger details and contact info
- Live trip tracking
- Start/complete ride actions
- Cancel ride option
- Call passenger button

**Earnings & Analytics**
- Daily, weekly, monthly earnings
- Total lifetime earnings
- Detailed breakdown table
- Revenue trends

**Driver Profile**
- Personal information
- Verification status badges
- Vehicle information
- Edit profile functionality

**Online Status Toggle**
- Go online/offline
- Receive notifications when online

### 4. **Admin Dashboard** (`admin-dashboard.html`)

#### Key Sections:

**Overview**
- Key Performance Indicators (KPIs)
  - Total users count
  - Active drivers count
  - Today's revenue
  - Completed rides
- Revenue trend charts
- Ride status distribution
- Recent system activity

**User Management**
- View all users
- Search users by name/email/phone
- Filter by status (Active/Suspended)
- Edit user details
- Delete user accounts
- Add new users

**Driver Management**
- View all drivers
- Search and filter drivers
- Verification status tracking
- Edit driver information
- Suspend/unsuspend drivers
- Add new drivers

**Rides Management**
- View all rides
- Filter by status (Completed/Cancelled/Active)
- Filter by date
- View detailed ride information
- Track ride metrics

**Payment Management**
- Payment transaction history
- Filter by payment status
- View payment methods used
- Track completed and pending payments

**Reports & Analytics**
- Generate custom reports
  - Revenue reports
  - User reports
  - Driver reports
  - Rides reports
- Date range selection
- Downloadable reports (simulated)

**Support Tickets**
- View customer support tickets
- Filter by status (Open/In Progress/Resolved)
- Resolve tickets with notes
- Priority level tracking

**System Settings**
- Fare configuration
  - Base fare amount
  - Per KM rate
- Commission settings
  - Admin commission percentage
- App settings
  - Maintenance mode toggle
  - Enable/disable new bookings

## 🎨 Design Features

### Responsive Design
- Mobile-first approach
- Works on desktop, tablet, and mobile devices
- Flexible grid layouts
- Touch-friendly buttons and inputs

### Color Scheme
```css
Primary Color:    #FF6B6B (Red)
Secondary Color:  #4ECDC4 (Teal)
Accent Color:     #FFE66D (Yellow)
Dark Background:  #2C3E50
Light Background: #ECF0F1
```

### UI Components
- Dashboard cards with statistics
- Interactive data tables
- Filter and search functionality
- Modal forms
- Toast notifications
- Progress bars
- Status badges
- Navigation sidebars

## 💾 Data Storage

The application uses browser storage for data persistence:

### Session Storage
- Current logged-in user information
- Temporary session data

### Local Storage
- User preferences
- Application settings
- Wallet history (if implemented)

### In-Memory Storage
- Application data object containing:
  - Users list
  - Drivers list
  - Rides list
  - Payments
  - Support tickets

**Note**: Data is not persistent across browser sessions unless explicitly saved to local/session storage.

## 🔐 Security Features

- Session-based authentication
- Password validation on signup
- User type verification
- Role-based access control
- Secure logout functionality

**Note**: This is a frontend demo. For production, implement proper backend authentication with:
- JWT tokens
- Password hashing
- HTTPS
- Database encryption
- API rate limiting

## 📊 Sample Data

The application comes with pre-populated sample data:

### Users
- Rahul Sharma (U001) - 25 rides, 4.5★ rating
- Priya Patel (U002) - 15 rides, 4.3★ rating

### Drivers
- Anuj Singh (D001) - 156 rides, 4.8★ rating, Hyundai Creta 2022

### Sample Rides
- Multiple completed and active rides

## 🎯 Features to Implement (Backend Integration)

When connecting to the Java backend, implement:

1. **API Integration**
   - User registration/login endpoints
   - Ride booking API
   - Real-time ride tracking (WebSockets)
   - Payment processing API
   - Notification system

2. **Real-time Features**
   - Live driver location tracking
   - Push notifications
   - Real-time ride updates
   - Chat functionality

3. **Advanced Features**
   - GPS integration
   - Payment gateway integration
   - SMS/Email notifications
   - Analytics dashboard
   - Ride analytics and reports

## 🚀 Deployment

### Local Development
```bash
# Using Python's built-in server
python -m http.server 8000

# Using Node.js
npx http-server

# Using VS Code Live Server extension
# Right-click on index.html → Open with Live Server
```

### Production Deployment

#### Option 1: Static Hosting (Recommended)
- GitHub Pages
- Netlify
- Vercel
- AWS S3 + CloudFront

#### Option 2: Web Server
- Nginx
- Apache
- Node.js (Express)

#### Option 3: Docker
```dockerfile
FROM nginx:latest
COPY . /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

## 📚 Code Organization

### CSS Architecture
- CSS Custom Properties (Variables) for theming
- Mobile-first responsive design
- Organized sections with comments
- Consistent naming conventions

### JavaScript Architecture
- Module-based functions
- Event listener management
- Data persistence utilities
- Error handling and validation

### File Sizes
- `styles.css`: ~35KB (unminified)
- `app.js`: ~25KB (unminified)
- Individual dashboard files: ~15-20KB each

## 🐛 Browser Compatibility

- ✅ Chrome 90+
- ✅ Firefox 88+
- ✅ Safari 14+
- ✅ Edge 90+
- ✅ Mobile browsers (iOS Safari, Chrome Mobile)

## 📝 Notes

1. **This is a frontend-only application** - No backend server is required for basic functionality
2. **Data is not persistent** - Refreshing the page will reset data (unless connected to a backend)
3. **For production use** - Connect to the Java backend and implement proper authentication
4. **Real-time features** - GPS tracking and live updates require backend implementation with WebSockets

## 🔗 Integration with Java Backend

To integrate with the Java backend:

1. Set up REST API endpoints in your Java application
2. Update API calls in JavaScript files
3. Implement authentication tokens (JWT)
4. Connect WebSockets for real-time updates
5. Set up CORS properly

Example API endpoints to create:
```
POST   /api/auth/login
POST   /api/auth/register
POST   /api/rides/book
GET    /api/rides/active
GET    /api/rides/history
POST   /api/wallet/add-money
GET    /api/driver/available-rides
POST   /api/admin/reports
```

## 📞 Support

For issues or questions:
1. Check the browser console for error messages
2. Verify all HTML files are in the same directory
3. Ensure JavaScript files are properly linked
4. Test in a different browser
5. Clear browser cache and reload

## 📄 License

This frontend application is part of the GoPro Ride-Hailing System.

## 🎉 Features Checklist

- [x] User Authentication (Login/Signup)
- [x] Rider Dashboard with ride booking
- [x] Driver Dashboard with earnings tracking
- [x] Admin Dashboard with analytics
- [x] Responsive Design
- [x] Real-time fare estimation
- [x] Ride history tracking
- [x] Wallet management
- [x] Profile management
- [x] User search and filtering
- [x] Data visualization (charts)
- [x] Notification system
- [x] Mobile responsiveness
- [ ] Real GPS tracking (requires backend)
- [ ] Payment gateway (requires backend)
- [ ] Push notifications (requires backend)
- [ ] Chat feature (requires backend)

---

**Version**: 1.0.0  
**Last Updated**: January 2024  
**Built with**: HTML5, CSS3, Vanilla JavaScript
