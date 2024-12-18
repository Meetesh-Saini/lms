package com.meetesh.LearningManagementSystem.repository;

import com.meetesh.LearningManagementSystem.entity.Course;
import com.meetesh.LearningManagementSystem.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByCourse(Course course);
}
