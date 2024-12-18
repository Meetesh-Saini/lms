package com.meetesh.LearningManagementSystem.controller;

import com.meetesh.LearningManagementSystem.dto.EnrollmentDTO;
import com.meetesh.LearningManagementSystem.entry.ApiResponse;
import com.meetesh.LearningManagementSystem.entry.EnrollmentEntry;
import com.meetesh.LearningManagementSystem.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enroll")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EnrollmentDTO>>> getEnrolled() {
        return ResponseEntity.ok(new ApiResponse<>(enrollmentService.getAllUserEnrolled(), "Get all enrolled courses", true, HttpStatus.OK.value()));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<EnrollmentDTO>> enroll(@Valid @RequestBody EnrollmentEntry enrollmentEntry) {
        return ResponseEntity.ok(new ApiResponse<>(enrollmentService.enroll(enrollmentEntry), "Enrolled in course", true, HttpStatus.OK.value()));
    }

    @DeleteMapping()
    public ResponseEntity<ApiResponse<EnrollmentDTO>> unEnroll(@RequestParam Long courseId) {
        return ResponseEntity.ok(new ApiResponse<>(enrollmentService.unEnroll(courseId), "Unenrolled from course", true, HttpStatus.OK.value()));
    }
}
