package com.courseselling.repository;

import com.courseselling.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Course entity.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCategory(String category);
    List<Course> findByIsAvailableTrue();
    List<Course> findByInstructor(String instructor);
}
