package com.courseselling.service;

import com.courseselling.model.CourseEnrollment;
import com.courseselling.repository.CourseEnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for CourseEnrollment operations.
 */
@Service
public class CourseEnrollmentService {

    @Autowired
    private CourseEnrollmentRepository courseEnrollmentRepository;

    public List<CourseEnrollment> getAllEnrollments() {
        return courseEnrollmentRepository.findAll();
    }

    public Optional<CourseEnrollment> getEnrollmentById(Long enrollmentId) {
        return courseEnrollmentRepository.findById(enrollmentId);
    }

    public List<CourseEnrollment> getEnrollmentsByUserId(Long userId) {
        return courseEnrollmentRepository.findByUserId(userId);
    }

    public List<CourseEnrollment> getEnrollmentsByCourseId(Long courseId) {
        return courseEnrollmentRepository.findByCourseId(courseId);
    }

    public CourseEnrollment enrollUserInCourse(Long userId, Long courseId) {
        CourseEnrollment existing = courseEnrollmentRepository.findByUserIdAndCourseId(userId, courseId);
        if (existing == null) {
            CourseEnrollment enrollment = new CourseEnrollment();
            enrollment.setStatus(CourseEnrollment.EnrollmentStatus.ENROLLED);
            return courseEnrollmentRepository.save(enrollment);
        }
        return existing;
    }

    public CourseEnrollment updateEnrollment(Long enrollmentId, CourseEnrollment enrollment) {
        if (courseEnrollmentRepository.existsById(enrollmentId)) {
            enrollment.setEnrollmentId(enrollmentId);
            return courseEnrollmentRepository.save(enrollment);
        }
        return null;
    }

    public void deleteEnrollment(Long enrollmentId) {
        courseEnrollmentRepository.deleteById(enrollmentId);
    }
}
