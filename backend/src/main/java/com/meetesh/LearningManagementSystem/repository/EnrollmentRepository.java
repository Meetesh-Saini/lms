package com.meetesh.LearningManagementSystem.repository;

import com.meetesh.LearningManagementSystem.entity.Course;
import com.meetesh.LearningManagementSystem.entity.Enrollment;
import com.meetesh.LearningManagementSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Enrollment findByCourseAndStudent(Course course, User student);
    List<Enrollment> findAllByStudent(User student);
}
