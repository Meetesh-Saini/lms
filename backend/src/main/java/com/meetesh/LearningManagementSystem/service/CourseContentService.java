package com.meetesh.LearningManagementSystem.service;

import com.meetesh.LearningManagementSystem.entity.CourseContent;
import com.meetesh.LearningManagementSystem.entity.Module;
import com.meetesh.LearningManagementSystem.entry.CourseContentEntry;
import com.meetesh.LearningManagementSystem.exception.UnauthorizedAccessException;
import com.meetesh.LearningManagementSystem.repository.CourseContentRepository;
import com.meetesh.LearningManagementSystem.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseContentService {

    @Autowired
    private final CourseContentRepository courseContentRepository;
    @Autowired
    private final CourseService courseService;
    @Autowired
    private final ModuleService moduleService;
    @Autowired
    private final ModuleRepository moduleRepository;

    public List<CourseContent> getContentByModule(Long moduleId) {
        return courseContentRepository.findByModule(
                moduleService.getModuleById(moduleId)
        );
    }

    public CourseContent createContent(CourseContentEntry entry, String filePath) {
        Module module = moduleService.getModuleById(entry.getModuleId());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!module.getCourse().getInstructor().getUsername().equals(username)) {
            throw new UnauthorizedAccessException("You are not authorized to add the content");
        }

        CourseContent content = new CourseContent();
        content.setModule(module);
        content.setType(entry.getType());
        content.setFilePath(filePath);
        content.setUploadDate(new Date());

        return courseContentRepository.save(content);
    }

    public CourseContent getContent(Long contentId) {
        Optional<CourseContent> contentOpt = courseContentRepository.findById(contentId);
        if (contentOpt.isPresent()) {
            return contentOpt.get();
        }
        return null;
    }

    public void deleteContent(Long contentId, String uploadDir) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<CourseContent> contentOpt = courseContentRepository.findById(contentId);

        if (contentOpt.isPresent()) {
            CourseContent content = contentOpt.get();
            Module module = content.getModule();

            if(!module.getCourse().getInstructor().getUsername().equals(username)) {
                throw new UnauthorizedAccessException("You are not authorized to delete the content");
            }

            removeFile(content.getFilePath(), uploadDir);
            module.getContents().remove(content);
            moduleRepository.save(module);
            courseContentRepository.deleteById(contentId);
        }
    }

    public String saveFile(MultipartFile file, String uploadDir) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    public void removeFile(String fileName, String uploadDir) throws IOException {
        Path pathToDelete = Paths.get(uploadDir, fileName);
        if (Files.exists(pathToDelete)) {
            Files.delete(pathToDelete);
        }
    }
}
