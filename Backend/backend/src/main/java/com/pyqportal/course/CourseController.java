package com.pyqportal.course;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses")
@Tag(name = "Courses", description = "Course catalog and enrollment")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    @Operation(summary = "List all courses with enrollment status")
    public ResponseEntity<List<CourseResponse>> getAllCourses(Authentication auth) {
        String email = auth != null ? (String) auth.getPrincipal() : "";
        return ResponseEntity.ok(courseService.getAllCourses(email));
    }

    @GetMapping("/my")
    @Operation(summary = "Get courses the logged-in user is enrolled in")
    public ResponseEntity<List<CourseResponse>> getMyCourses(Authentication auth) {
        return ResponseEntity.ok(courseService.getMyCourses((String) auth.getPrincipal()));
    }

    @PostMapping("/{id}/enroll")
    @Operation(summary = "Enroll in a course")
    public ResponseEntity<CourseResponse> enroll(@PathVariable UUID id, Authentication auth) {
        return ResponseEntity.ok(courseService.enroll(id, (String) auth.getPrincipal()));
    }

    @DeleteMapping("/{id}/enroll")
    @Operation(summary = "Unenroll from a course")
    public ResponseEntity<Void> unenroll(@PathVariable UUID id, Authentication auth) {
        courseService.unenroll(id, (String) auth.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @Operation(summary = "Create a course (ADMIN only)")
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseRequest req) {
        return ResponseEntity.ok(courseService.createCourse(req));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a course (ADMIN only)")
    public ResponseEntity<Void> deleteCourse(@PathVariable UUID id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
