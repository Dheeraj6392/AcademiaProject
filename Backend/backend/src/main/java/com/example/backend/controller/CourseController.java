package com.example.backend.controller;

import com.example.backend.entity.Course;
import com.example.backend.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> listAll() {
        return courseService.listAllCourses();
    }

    @GetMapping("/{id}")
    public Course getOne(@PathVariable Long id) {
        return courseService.getCourse(id);
    }

    @PostMapping
    public Course create(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @GetMapping("/me")
    public List<Course> myCourses() {
        String username = getCurrentUsername();
        return courseService.listEnrolledCourses(username);
    }

    @PostMapping("/{id}/enroll")
    public ResponseEntity<?> enroll(@PathVariable Long id) {
        String username = getCurrentUsername();
        courseService.enroll(username, id);
        return ResponseEntity.ok(Map.of("status", "enrolled", "courseId", id));
    }

    @DeleteMapping("/{id}/enroll")
    public ResponseEntity<?> unenroll(@PathVariable Long id) {
        String username = getCurrentUsername();
        courseService.unenroll(username, id);
        return ResponseEntity.ok(Map.of("status", "unenrolled", "courseId", id));
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}