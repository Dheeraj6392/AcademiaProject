package com.example.backend.repository;

import com.example.backend.entity.Course;
import com.example.backend.entity.Enrollment;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByUserAndCourse(User user, Course course);
    Optional<Enrollment> findByUserAndCourse(User user, Course course);
    List<Enrollment> findByUser(User user);
}