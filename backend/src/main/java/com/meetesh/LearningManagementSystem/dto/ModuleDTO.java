package com.meetesh.LearningManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.meetesh.LearningManagementSystem.entity.Module;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleDTO {

    private Long moduleId;
    private String moduleName;
    private CourseDTO course;
    private List<CourseContentDTO> contents;

    // Constructor to convert from Module entity to ModuleDTO
    public ModuleDTO(Module module) {
        this.moduleId = module.getModuleId();
        this.moduleName = module.getModuleName();
        // Map Course to CourseDTO
        this.course = new CourseDTO(module.getCourse());
        // Map CourseContent to CourseContentDTO
        if(module.getContents() == null) {
            this.contents = null;
        } else {
            this.contents = module.getContents().stream()
                    .map(CourseContentDTO::new)
                    .collect(Collectors.toList());
        }
    }
}
