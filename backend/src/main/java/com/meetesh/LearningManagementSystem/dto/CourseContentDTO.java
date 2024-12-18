package com.meetesh.LearningManagementSystem.dto;

import com.meetesh.LearningManagementSystem.entity.CourseContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseContentDTO {

    private Long contentId;
    private String type;
    private String filePath;
    private Date uploadDate;
    private Long moduleId;

    // Constructor to convert from CourseContent entity to CourseContentDTO
    public CourseContentDTO(CourseContent content) {
        this.contentId = content.getContentId();
        this.type = content.getType();
        this.filePath = "/api/contents/files/"+content.getFilePath();
        this.uploadDate = content.getUploadDate();
        // Map Module to ModuleDTO
        this.moduleId = content.getModule().getModuleId();
    }
}
