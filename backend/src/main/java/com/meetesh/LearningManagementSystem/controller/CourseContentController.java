package com.meetesh.LearningManagementSystem.controller;

import com.meetesh.LearningManagementSystem.dto.CourseContentDTO;
import com.meetesh.LearningManagementSystem.entity.CourseContent;
import com.meetesh.LearningManagementSystem.entry.CourseContentEntry;
import com.meetesh.LearningManagementSystem.service.CourseContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class CourseContentController {

    @Autowired
    private final CourseContentService courseContentService;

    private final String uploadDir = "uploads/course-contents";

    @GetMapping("/module/{moduleId}")
    public List<CourseContentDTO> getContentByModule(@PathVariable Long moduleId) {
        return courseContentService.getContentByModule(moduleId).stream().map(CourseContentDTO::new).toList();
    }

    @PostMapping
    public CourseContentDTO createContent(
            @ModelAttribute CourseContentEntry courseContentEntry,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        // Save the uploaded file
        String filePath = courseContentService.saveFile(file, uploadDir);

        // Create and save the CourseContent entity
        return new CourseContentDTO(courseContentService.createContent(courseContentEntry, filePath));
    }

    @DeleteMapping("/{contentId}")
    public void deleteContent(@PathVariable Long contentId) throws IOException {
        courseContentService.deleteContent(contentId, uploadDir);
    }

    @GetMapping("/{contentId}")
    public CourseContentDTO getContent(@PathVariable Long contentId) {
        CourseContent content = courseContentService.getContent(contentId);
        if(content != null){
            return new CourseContentDTO(content);
        }
        return null;
    }

    @GetMapping("/files/{fileName}")
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) throws MalformedURLException {
        Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new RuntimeException("File not found: " + fileName);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}


