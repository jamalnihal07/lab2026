# Course Selling Website with CRM

A comprehensive web-based application for managing online course sales with integrated customer relationship management (CRM) functionality.

## Project Overview

The Course Selling Website with CRM is an end-to-end solution that enables:

- **Users**: Register, log in, browse courses, and purchase online with integrated payment gateway
- **Administrators**: Manage courses, users, track purchases, and monitor system activities
- **Employees**: Handle customer calls, assist with course selection, and directly sell courses through the CRM module

## Features

### User Features
- Secure registration and authentication
- Browse and search available courses by category
- Purchase courses with online payment integration
- Access purchased course materials
- Track learning progress
- View payment history

### Admin Dashboard
- Add, update, and delete courses
- Manage course details (descriptions, pricing, availability)
- Monitor user accounts and purchases
- Track system-wide activities
- Generate reports and analytics

### CRM Module - Employee Panel
- Manage incoming customer calls and inquiries
- Assist customers in course selection and purchase
- Sell courses directly to customers
- Maintain conversation and interaction history
- Provide follow-up support and personalized service
- Track sales performance and customer engagement

## Technology Stack

| Component | Technology |
|-----------|-----------|
| **Backend** | Spring Boot |
| **Frontend** | HTML, CSS, JavaScript |
| **Template Engine** | Thymeleaf |
| **Database** | MySQL |
| **Architecture** | MVC (Model-View-Controller) |
| **Payment** | Online Payment Gateway |
| **ORM** | JPA/Hibernate |

## Project Structure

```
CourseSellingCRM/
├── src/main/
│   ├── java/com/courseselling/
│   │   ├── controller/          # REST controllers and page controllers
│   │   ├── model/               # Entity classes
│   │   ├── service/             # Business logic layer
│   │   ├── repository/          # Data access layer
│   │   └── CourseSellingApplication.java
│   └── resources/
│       ├── templates/           # Thymeleaf HTML templates
│       ├── static/
│       │   ├── css/            # Stylesheets
│       │   └── js/             # JavaScript files
│       └── application.properties
├── pom.xml                      # Maven configuration
└── README.md
```

## Database Design

### Main Entities

1. **User** - Customer accounts with registration information
2. **Course** - Course details including title, description, pricing
3. **CourseEnrollment** - Tracks user course enrollments
4. **Payment** - Payment transactions and history
5. **Employee** - CRM employee/sales staff information
6. **CustomerInteraction** - Records of employee-customer interactions

## Setup Instructions

### Prerequisites
- Java 11 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

### Database Setup

1. Create a MySQL database:
```sql
CREATE DATABASE course_selling_crm;
```

2. Update database credentials in `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/course_selling_crm
spring.datasource.username=root
spring.datasource.password=your_password
```

### Build and Run

1. Clone or download the project
2. Navigate to the project directory
3. Run Maven build:
```bash
mvn clean install
```

4. Start the application:
```bash
mvn spring-boot:run
```

5. Access the application at `http://localhost:8080`

## API Endpoints

### Course Management
- `GET /courses` - List all courses
- `GET /courses/{id}` - Get course details
- `GET /courses/category/{category}` - Get courses by category
- `POST /courses/admin/save` - Create new course
- `POST /courses/admin/update/{id}` - Update course
- `POST /courses/admin/delete/{id}` - Delete course

### Authentication
- `GET /auth/login` - Login page
- `POST /auth/login` - Process login
- `GET /auth/register` - Registration page
- `POST /auth/register` - Process registration
- `GET /auth/logout` - Logout

### Dashboard
- `GET /` - Home page
- `GET /dashboard` - User dashboard
- `GET /admin` - Admin dashboard
- `GET /employee` - Employee dashboard

## Security Features

- Secure password encryption
- User authentication and authorization
- Role-based access control (Customer, Employee, Admin)
- Session management
- CSRF protection with Spring Security

## Future Enhancements

- Video streaming integration
- Advanced analytics and reporting
- Mobile app development
- Multi-language support
- Certification management
- Course recommendations engine
- Wishlist functionality
- Course reviews and ratings system

## Contributors

This project was developed as part of a comprehensive course management system.

## License

This project is confidential and for educational purposes only.

## Support

For issues, questions, or feature requests, please contact the development team.
