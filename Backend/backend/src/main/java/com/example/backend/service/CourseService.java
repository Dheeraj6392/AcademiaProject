package com.example.backend.service;

import com.example.backend.entity.Course;
import com.example.backend.entity.Enrollment;
import com.example.backend.entity.User;
import com.example.backend.repository.CourseRepository;
import com.example.backend.repository.EnrollmentRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
    }

    public List<Course> listAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourse(Long id) {
        return courseRepository.findById(id).orElseThrow();
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Transactional
    public void enroll(String username, Long courseId) {
        User user = userRepository.findByUsername(username).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        if (!enrollmentRepository.existsByUserAndCourse(user, course)) {
            Enrollment enrollment = Enrollment.builder()
                    .user(user)
                    .course(course)
                    .build();
            enrollmentRepository.save(enrollment);
        }
    }

    @Transactional
    public void unenroll(String username, Long courseId) {
        User user = userRepository.findByUsername(username).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        enrollmentRepository.findByUserAndCourse(user, course)
                .ifPresent(enrollmentRepository::delete);
    }

    public List<Course> listEnrolledCourses(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return enrollmentRepository.findByUser(user)
                .stream()
                .map(Enrollment::getCourse)
                .collect(Collectors.toList());
    }
}