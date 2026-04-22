package com.courseselling.controller;

import com.courseselling.model.Course;
import com.courseselling.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for handling course-related requests.
 */
@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String getAllCourses(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "courses/list";
    }

    @GetMapping("/{courseId}")
    public String getCourseDetail(@PathVariable Long courseId, Model model) {
        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            return "courses/detail";
        }
        return "error";
    }

    @GetMapping("/category/{category}")
    public String getCoursesByCategory(@PathVariable String category, Model model) {
        List<Course> courses = courseService.getCoursesByCategory(category);
        model.addAttribute("courses", courses);
        model.addAttribute("category", category);
        return "courses/category";
    }

    @GetMapping("/admin/new")
    public String showCreateCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "admin/course-form";
    }

    @PostMapping("/admin/save")
    public String createCourse(@ModelAttribute Course course) {
        courseService.createCourse(course);
        return "redirect:/courses";
    }

    @GetMapping("/admin/edit/{courseId}")
    public String showEditCourseForm(@PathVariable Long courseId, Model model) {
        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            return "admin/course-form";
        }
        return "error";
    }

    @PostMapping("/admin/update/{courseId}")
    public String updateCourse(@PathVariable Long courseId, @ModelAttribute Course course) {
        courseService.updateCourse(courseId, course);
        return "redirect:/courses";
    }

    @PostMapping("/admin/delete/{courseId}")
    public String deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return "redirect:/courses";
    }
}
