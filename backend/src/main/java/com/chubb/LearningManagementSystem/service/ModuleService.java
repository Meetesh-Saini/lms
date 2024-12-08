package com.chubb.LearningManagementSystem.service;

import com.chubb.LearningManagementSystem.entity.Course;
import com.chubb.LearningManagementSystem.entity.Module;
import com.chubb.LearningManagementSystem.entry.ModuleEntry;
import com.chubb.LearningManagementSystem.exception.UnauthorizedAccessException;
import com.chubb.LearningManagementSystem.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseService courseService;

    public List<Module> getModulesByCourse(Long courseId) {
        return moduleRepository.findByCourse(new Course(courseId, null, null, null, null, null, null));
    }

    public Module createModule(ModuleEntry moduleEntry) {
        Course course = courseService.getCourseById(moduleEntry.getCourseId());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Check if the logged-in user is the instructor of the course
        if (!course.getInstructor().getUsername().equals(username)) {
            throw new UnauthorizedAccessException("You are not authorized to add a module");
        }

        Module module = new Module();
        module.setModuleName(moduleEntry.getName());
        module.setCourse(course);
        return moduleRepository.save(module);
    }

    public void deleteModule(Long moduleId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!moduleRepository.getReferenceById(moduleId).getCourse().getInstructor().getUsername().equals(username)) {
            throw new UnauthorizedAccessException("You are not authorized to delete a module");
        }
        moduleRepository.deleteById(moduleId);
    }

    public Module getModuleById(Long moduleId) {
        return moduleRepository.getReferenceById(moduleId);
    }
}
