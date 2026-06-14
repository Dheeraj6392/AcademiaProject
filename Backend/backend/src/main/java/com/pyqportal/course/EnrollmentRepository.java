package com.pyqportal.course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    boolean existsByUserEmailAndCourseId(String email, UUID courseId);

    Optional<Enrollment> findByUserEmailAndCourseId(String email, UUID courseId);

    List<Enrollment> findAllByUserEmail(String email);

    long countByCourseId(UUID courseId);
}
