package com.meetesh.LearningManagementSystem.repository;

import com.meetesh.LearningManagementSystem.entity.CourseContent;
import com.meetesh.LearningManagementSystem.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseContentRepository extends JpaRepository<CourseContent, Long> {
    List<CourseContent> findByModule(Module module);
}
