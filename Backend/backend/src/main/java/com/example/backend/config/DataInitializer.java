package com.example.backend.config;

import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.entity.Course;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                userRepository.save(User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .build());
            }
            if (!userRepository.existsByUsername("user")) {
                userRepository.save(User.builder()
                        .username("user")
                        .password(passwordEncoder.encode("user123"))
                        .role(Role.USER)
                        .build());
            }
        };
    }

    @Bean
    CommandLineRunner initCourses(CourseRepository courseRepository) {
        return args -> {
            if (courseRepository.count() == 0) {
                courseRepository.save(Course.builder()
                        .title("Introduction to Programming")
                        .description("Learn the fundamentals of programming with hands-on exercises.")
                        .imageUrl("https://demo.templatesjungle.com/worldcourse/images/item1.jpg")
                        .price(180.0)
                        .rating(4.8)
                        .weeks(12)
                        .lessons(10)
                        .students(200)
                        .build());
                courseRepository.save(Course.builder()
                        .title("Data Structures")
                        .description("Master core data structures and algorithms for efficient coding.")
                        .imageUrl("https://demo.templatesjungle.com/worldcourse/images/item3.jpg")
                        .price(240.0)
                        .rating(4.6)
                        .weeks(10)
                        .lessons(12)
                        .students(160)
                        .build());
                courseRepository.save(Course.builder()
                        .title("Digital System Design")
                        .description("Design and analyze digital circuits and systems.")
                        .imageUrl("https://demo.templatesjungle.com/worldcourse/images/item2.jpg")
                        .price(150.0)
                        .rating(4.4)
                        .weeks(8)
                        .lessons(9)
                        .students(120)
                        .build());
                courseRepository.save(Course.builder()
                        .title("Probability and Statistics")
                        .description("Statistics essentials for data analysis and ML foundations.")
                        .imageUrl("https://demo.templatesjungle.com/worldcourse/images/item4.jpg")
                        .price(170.0)
                        .rating(4.7)
                        .weeks(6)
                        .lessons(8)
                        .students(180)
                        .build());
            }
        };
    }
}