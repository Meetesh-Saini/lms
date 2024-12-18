package com.meetesh.LearningManagementSystem.service;

import com.meetesh.LearningManagementSystem.dto.EnrollmentDTO;
import com.meetesh.LearningManagementSystem.entity.Course;
import com.meetesh.LearningManagementSystem.entity.Enrollment;
import com.meetesh.LearningManagementSystem.entity.User;
import com.meetesh.LearningManagementSystem.entry.EnrollmentEntry;
import com.meetesh.LearningManagementSystem.repository.CourseRepository;
import com.meetesh.LearningManagementSystem.repository.EnrollmentRepository;
import com.meetesh.LearningManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    public final EnrollmentRepository enrollmentRepository;
    public final CourseRepository courseRepository;
    public final UserRepository userRepository;

    public List<EnrollmentDTO> getAllUserEnrolled() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        return enrollmentRepository.findAllByStudent(user).stream().map(EnrollmentDTO::new).toList();
    }

    public EnrollmentDTO enroll(EnrollmentEntry enrollmentEntry) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        Long courseId = enrollmentEntry.getCourseId();
        Course course = courseRepository.getReferenceById(courseId);

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(user);
        enrollment.setEnrollmentDate(new Date());
        return new EnrollmentDTO(enrollmentRepository.save(enrollment));
    }

    public EnrollmentDTO unEnroll(Long courseId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        Course course = courseRepository.getReferenceById(courseId);

        Enrollment enrollment = enrollmentRepository.findByCourseAndStudent(course, user);
        enrollmentRepository.delete(enrollment);
        return new EnrollmentDTO(enrollment);
    }
}
