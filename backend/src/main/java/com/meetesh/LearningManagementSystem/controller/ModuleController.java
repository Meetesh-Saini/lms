package com.meetesh.LearningManagementSystem.controller;

import com.meetesh.LearningManagementSystem.dto.ModuleDTO;
import com.meetesh.LearningManagementSystem.entry.ApiResponse;
import com.meetesh.LearningManagementSystem.entry.ModuleEntry;
import com.meetesh.LearningManagementSystem.service.ModuleService;
import com.meetesh.LearningManagementSystem.entity.Module;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<ModuleDTO>>> getModulesByCourse(@PathVariable Long courseId) {
        List<Module> modules = moduleService.getModulesByCourse(courseId);
        List<ModuleDTO> moduleDTOS = modules.stream().map(ModuleDTO::new).toList();
        ApiResponse<List<ModuleDTO>> response = new ApiResponse<>(moduleDTOS, "Modules retrieved successfully", true, HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<ApiResponse<ModuleDTO>> getModuleById(@PathVariable Long moduleId) {
        Module module = moduleService.getModuleById(moduleId);
        ModuleDTO moduleDTO = new ModuleDTO(module);
        ApiResponse<ModuleDTO> response = new ApiResponse<>(moduleDTO, "Module retrieved successfully", true, HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ModuleDTO>> createModule(@Valid @RequestBody ModuleEntry moduleEntry) {
        Module createdModule = moduleService.createModule(moduleEntry);
        ModuleDTO moduleDTO = new ModuleDTO(createdModule);
        ApiResponse<ModuleDTO> response = new ApiResponse<>(moduleDTO, "Module created successfully", true, HttpStatus.CREATED.value());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{moduleId}")
    public ResponseEntity<ApiResponse<Void>> deleteModule(@PathVariable Long moduleId) {
        moduleService.deleteModule(moduleId);
        ApiResponse<Void> response = new ApiResponse<>(null, "Module deleted successfully", true, HttpStatus.NO_CONTENT.value());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
