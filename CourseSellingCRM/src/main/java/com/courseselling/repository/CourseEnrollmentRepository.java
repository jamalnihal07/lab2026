package com.courseselling.repository;

import com.courseselling.model.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for CourseEnrollment entity.
 */
@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {
    List<CourseEnrollment> findByUserId(Long userId);
    List<CourseEnrollment> findByCourseId(Long courseId);
    CourseEnrollment findByUserIdAndCourseId(Long userId, Long courseId);
}
