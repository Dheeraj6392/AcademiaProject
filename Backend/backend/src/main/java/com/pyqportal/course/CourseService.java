package com.pyqportal.course;

import com.pyqportal.exception.ResourceNotFoundException;
import com.pyqportal.user.User;
import com.pyqportal.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    public List<CourseResponse> getAllCourses(String email) {
        return courseRepository.findAll().stream()
                .map(c -> CourseResponse.from(
                        c,
                        enrollmentRepository.countByCourseId(c.getId()),
                        enrollmentRepository.existsByUserEmailAndCourseId(email, c.getId())
                ))
                .toList();
    }

    public List<CourseResponse> getMyCourses(String email) {
        return enrollmentRepository.findAllByUserEmail(email).stream()
                .map(e -> CourseResponse.from(
                        e.getCourse(),
                        enrollmentRepository.countByCourseId(e.getCourse().getId()),
                        true
                ))
                .toList();
    }

    @Transactional
    public CourseResponse enroll(UUID courseId, String email) {
        if (enrollmentRepository.existsByUserEmailAndCourseId(email, courseId)) {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
            return CourseResponse.from(course, enrollmentRepository.countByCourseId(courseId), true);
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        enrollmentRepository.save(Enrollment.builder().user(user).course(course).build());
        return CourseResponse.from(course, enrollmentRepository.countByCourseId(courseId), true);
    }

    @Transactional
    public void unenroll(UUID courseId, String email) {
        enrollmentRepository.findByUserEmailAndCourseId(email, courseId)
                .ifPresent(enrollmentRepository::delete);
    }

    @Transactional
    public CourseResponse createCourse(CourseRequest req) {
        String imageUrl = (req.imageUrl() != null && !req.imageUrl().isBlank())
                ? req.imageUrl()
                : "https://picsum.photos/seed/" + req.title().replaceAll("\\s+", "-") + "/400/300";

        Course course = courseRepository.save(Course.builder()
                .title(req.title())
                .description(req.description())
                .weeks(req.weeks())
                .lessons(req.lessons())
                .price(req.price())
                .imageUrl(imageUrl)
                .build());
        return CourseResponse.from(course, 0, false);
    }

    @Transactional
    public void deleteCourse(UUID courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found");
        }
        enrollmentRepository.findAll().stream()
                .filter(e -> e.getCourse().getId().equals(courseId))
                .forEach(enrollmentRepository::delete);
        courseRepository.deleteById(courseId);
    }
}
