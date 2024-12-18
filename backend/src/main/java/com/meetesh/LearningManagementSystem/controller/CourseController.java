package com.meetesh.LearningManagementSystem.controller;

import com.meetesh.LearningManagementSystem.dto.CourseDTO;
import com.meetesh.LearningManagementSystem.entity.Course;
import com.meetesh.LearningManagementSystem.entry.ApiResponse;
import com.meetesh.LearningManagementSystem.entry.CourseEntry;
import com.meetesh.LearningManagementSystem.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseDTO>>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        List<CourseDTO> courseDTOS = courses.stream().map(CourseDTO::new).toList();
        ApiResponse<List<CourseDTO>> response = new ApiResponse<>(courseDTOS, "Courses retrieved successfully", true, HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<ApiResponse<CourseDTO>> getCourseById(@PathVariable Long courseId) {
        Course course = courseService.getCourseById(courseId);
        CourseDTO courseDTO = new CourseDTO(course);
        ApiResponse<CourseDTO> response = new ApiResponse<>(courseDTO, "Course retrieved successfully", true, HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createCourse(@Valid @RequestBody CourseEntry courseEntry, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Validation failed
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            ApiResponse<Map<String, String>> response = new ApiResponse<>(errors, "Validation failed", false, HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(response);
        }

        Course createdCourse = courseService.saveCourse(courseEntry);
        CourseDTO courseDTO = new CourseDTO(createdCourse);
        ApiResponse<CourseDTO> response = new ApiResponse<>(courseDTO, "Course created successfully", true, HttpStatus.CREATED.value());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<ApiResponse<CourseDTO>> updateCourse(@PathVariable Long courseId, @RequestBody CourseEntry updatedCourse) {
        courseService.updateCourse(courseId, updatedCourse);
        ApiResponse<CourseDTO> response = new ApiResponse<>(null, "Course updated successfully", true, HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        ApiResponse<Void> response = new ApiResponse<>(null, "Course deleted successfully", true, HttpStatus.NO_CONTENT.value());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
