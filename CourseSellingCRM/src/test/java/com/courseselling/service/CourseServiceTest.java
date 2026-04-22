package com.courseselling.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.courseselling.model.Course;
import com.courseselling.repository.CourseRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseService Tests")
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setCourseId(1L);
        testCourse.setCourseName("Java Basics");
        testCourse.setCourseDescription("Learn Java programming");
        testCourse.setCategory("Programming");
        testCourse.setPrice(new BigDecimal("29.99"));
        testCourse.setDuration("6 weeks");
        testCourse.setInstructor("John Smith");
        testCourse.setSkillLevel("Beginner");
        testCourse.setIsAvailable(true);
        testCourse.setCreatedAt(LocalDateTime.now());
        testCourse.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should get all courses successfully")
    void testGetAllCourses() {
        // Arrange
        Course course2 = new Course();
        course2.setCourseId(2L);
        course2.setCourseName("Python Basics");
        course2.setCategory("Programming");

        List<Course> courseList = Arrays.asList(testCourse, course2);
        when(courseRepository.findAll()).thenReturn(courseList);

        // Act
        List<Course> result = courseService.getAllCourses();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Java Basics", result.get(0).getCourseName());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get course by ID successfully")
    void testGetCourseById() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        // Act
        Optional<Course> result = courseService.getCourseById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Java Basics", result.get().getCourseName());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty optional when course not found by ID")
    void testGetCourseByIdNotFound() {
        // Arrange
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Course> result = courseService.getCourseById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(courseRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should get courses by category successfully")
    void testGetCoursesByCategory() {
        // Arrange
        List<Course> courseList = Arrays.asList(testCourse);
        when(courseRepository.findByCategory("Programming")).thenReturn(courseList);

        // Act
        List<Course> result = courseService.getCoursesByCategory("Programming");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Programming", result.get(0).getCategory());
        verify(courseRepository, times(1)).findByCategory("Programming");
    }

    @Test
    @DisplayName("Should get available courses successfully")
    void testGetAvailableCourses() {
        // Arrange
        List<Course> courseList = Arrays.asList(testCourse);
        when(courseRepository.findByIsAvailableTrue()).thenReturn(courseList);

        // Act
        List<Course> result = courseService.getAvailableCourses();

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0).getIsAvailable());
        verify(courseRepository, times(1)).findByIsAvailableTrue();
    }

    @Test
    @DisplayName("Should create course successfully")
    void testCreateCourse() {
        // Arrange
        Course newCourse = new Course();
        newCourse.setCourseName("Advanced Java");
        newCourse.setCategory("Programming");

        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        // Act
        Course result = courseService.createCourse(newCourse);

        // Assert
        assertNotNull(result);
        assertEquals("Java Basics", result.getCourseName());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Should update existing course successfully")
    void testUpdateCourseSuccess() {
        // Arrange
        Course updatedCourse = new Course();
        updatedCourse.setCourseName("Java Advanced");
        updatedCourse.setPrice(new BigDecimal("49.99"));

        when(courseRepository.existsById(1L)).thenReturn(true);
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        // Act
        Course result = courseService.updateCourse(1L, updatedCourse);

        // Assert
        assertNotNull(result);
        verify(courseRepository, times(1)).existsById(1L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Should return null when updating non-existent course")
    void testUpdateCourseNotFound() {
        // Arrange
        Course updatedCourse = new Course();
        updatedCourse.setCourseName("Java Advanced");

        when(courseRepository.existsById(999L)).thenReturn(false);

        // Act
        Course result = courseService.updateCourse(999L, updatedCourse);

        // Assert
        assertNull(result);
        verify(courseRepository, times(1)).existsById(999L);
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    @DisplayName("Should delete course successfully")
    void testDeleteCourse() {
        // Arrange & Act
        courseService.deleteCourse(1L);

        // Assert
        verify(courseRepository, times(1)).deleteById(1L);
    }
}
