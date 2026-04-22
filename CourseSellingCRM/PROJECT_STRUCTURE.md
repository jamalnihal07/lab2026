# Course Selling CRM - Project Structure

## Complete Project Setup Summary

The **Course Selling Website with CRM** project has been successfully created with the following structure:

```
CourseSellingCRM/
│
├── pom.xml                                    # Maven configuration file
├── README.md                                  # Project documentation
│
├── src/
│   ├── main/
│   │   ├── java/com/courseselling/
│   │   │   ├── CourseSellingApplication.java  # Spring Boot main class
│   │   │   │
│   │   │   ├── controller/                    # Request handlers (Layer 1 - Web)
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── CourseController.java
│   │   │   │   └── EmployeeController.java
│   │   │   │
│   │   │   ├── model/                         # Entity classes (Layer 3 - Database)
│   │   │   │   ├── User.java
│   │   │   │   ├── Course.java
│   │   │   │   ├── CourseEnrollment.java
│   │   │   │   ├── Payment.java
│   │   │   │   ├── Employee.java
│   │   │   │   └── CustomerInteraction.java
│   │   │   │
│   │   │   ├── repository/                    # Data Access Layer (Layer 3)
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── CourseRepository.java
│   │   │   │   ├── CourseEnrollmentRepository.java
│   │   │   │   ├── PaymentRepository.java
│   │   │   │   ├── EmployeeRepository.java
│   │   │   │   └── CustomerInteractionRepository.java
│   │   │   │
│   │   │   └── service/                       # Business Logic Layer (Layer 2)
│   │   │       ├── UserService.java
│   │   │       ├── CourseService.java
│   │   │       ├── CourseEnrollmentService.java
│   │   │       ├── PaymentService.java
│   │   │       └── EmployeeService.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties          # Spring Boot configuration
│   │       │
│   │       ├── templates/                     # Thymeleaf HTML Templates
│   │       │   ├── index.html                 # Home page
│   │       │   ├── dashboard.html             # User dashboard
│   │       │   ├── auth/
│   │       │   │   ├── login.html
│   │       │   │   └── register.html
│   │       │   ├── courses/
│   │       │   │   └── list.html              # Course listing
│   │       │   ├── admin/
│   │       │   │   ├── dashboard.html         # Admin dashboard
│   │       │   │   └── course-form.html       # Add/edit course form
│   │       │   └── employee/
│   │       │       └── dashboard.html         # Employee CRM dashboard
│   │       │
│   │       └── static/
│   │           ├── css/
│   │           │   └── style.css              # Main stylesheet
│   │           └── js/
│   │               └── (JavaScript files)
│   │
│   └── test/
│       └── java/com/courseselling/            # Unit tests
│
```

## Architecture Overview

### Layers:
1. **Presentation Layer (Controller)**: Handles HTTP requests and responses
2. **Business Logic Layer (Service)**: Contains core business operations
3. **Data Access Layer (Repository)**: Database interactions via JPA
4. **Domain Layer (Model)**: Entity classes representing database tables

### Database Tables:
- `users` - Customer accounts
- `courses` - Available courses
- `course_enrollments` - User course enrollments
- `payments` - Payment transactions
- `employees` - CRM employees
- `customer_interactions` - Employee-customer interactions

## Feature Summary

### User Features
✓ Registration and login
✓ Browse courses by category
✓ View course details
✓ Purchase courses
✓ Access dashboard with enrollment history
✓ Payment history tracking

### Admin Features
✓ Admin dashboard
✓ Add/update/delete courses
✓ Manage course details
✓ User management
✓ Payment monitoring
✓ System analytics

### Employee CRM Panel
✓ Employee dashboard
✓ Customer interaction logging
✓ Sales management
✓ Order creation
✓ Performance tracking
✓ Customer management

## Technology Stack

| Technology | Purpose |
|-----------|---------|
| **Spring Boot 3.2.0** | Application framework |
| **Spring Data JPA** | Object-relational mapping |
| **Thymeleaf** | Server-side template engine |
| **Spring Security** | Authentication & authorization |
| **MySQL 8.0** | Database |
| **Lombok** | Reduce boilerplate code |
| **Maven 3.6+** | Build tool |
| **Java 11+** | Programming language |

## Key Models & Relationships

### User Model
- One User can have multiple CourseEnrollments
- One User can have multiple Payments
- User Role: CUSTOMER, EMPLOYEE, ADMIN

### Course Model
- One Course can have multiple CourseEnrollments
- Tracks: ratings, total students, availability

### Payment Model
- Tracks course purchases
- Status: PENDING, COMPLETED, FAILED, REFUNDED
- Integration-ready for payment gateway

### Employee Model
- Tracks sales performance
- Department-based organization
- Sales metrics and revenue tracking

### CustomerInteraction Model
- Records all employee-customer interactions
- Type: CALL, EMAIL, CHAT, IN_PERSON
- Maintains conversation history

## Configuration

### Database Setup
```sql
CREATE DATABASE course_selling_crm;
```

### Application Properties
- Database URL: `jdbc:mysql://localhost:3306/course_selling_crm`
- Default username: `root`
- Default password: `root` (change for production)
- Server port: `8080`

## Next Steps for Development

1. **Install Maven Dependencies**:
   ```bash
   mvn clean install
   ```

2. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

3. **Access the Application**:
   - Navigate to `http://localhost:8080`

4. **Complete Remaining Features**:
   - Payment gateway integration
   - Email notifications
   - Advanced reporting
   - Video streaming for courses
   - Mobile app development

## Project Files Created

**Configuration Files**: 3
- pom.xml
- application.properties
- README.md

**Controllers**: 4
- HomeController
- AuthController
- CourseController
- EmployeeController

**Services**: 5
- UserService
- CourseService
- PaymentService
- EmployeeService
- CourseEnrollmentService

**Repositories**: 6
- UserRepository
- CourseRepository
- CourseEnrollmentRepository
- PaymentRepository
- EmployeeRepository
- CustomerInteractionRepository

**Models**: 6
- User
- Course
- CourseEnrollment
- Payment
- Employee
- CustomerInteraction

**Templates**: 8
- index.html
- dashboard.html
- login.html
- register.html
- courses/list.html
- admin/dashboard.html
- admin/course-form.html
- employee/dashboard.html

**Static Resources**: 1
- css/style.css

**Total Files**: 33+ (including supporting files)

---

**Project Status**: ✅ Ready for Maven Build and Development

The project structure follows Spring Boot best practices with clear separation of concerns, making it easy to maintain, test, and scale.
