package com.courseselling.service;

import com.courseselling.model.Course;
import com.courseselling.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for Course operations.
 */
@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category);
    }

    public List<Course> getAvailableCourses() {
        return courseRepository.findByIsAvailableTrue();
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(Long courseId, Course course) {
        if (courseRepository.existsById(courseId)) {
            course.setCourseId(courseId);
            return courseRepository.save(course);
        }
        return null;
    }

    public void deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
    }
}
