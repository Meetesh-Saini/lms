package com.meetesh.LearningManagementSystem.controller;

import com.meetesh.LearningManagementSystem.entry.ApiResponse;
import com.meetesh.LearningManagementSystem.exception.UserAlreadyExistsException;
import com.meetesh.LearningManagementSystem.exception.UnauthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.JwtException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Handle validation errors (from @Valid annotations)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
    @Override
    protected org.springframework.http.ResponseEntity<java.lang.Object> handleMethodArgumentNotValid(org.springframework.web.bind.MethodArgumentNotValidException ex, org.springframework.http.HttpHeaders headers, org.springframework.http.HttpStatusCode status, org.springframework.web.context.request.WebRequest request){

//        public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        ApiResponse<Map<String, String>> response = new ApiResponse<>(errors, "Validation failed", false, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(response);
    }

    // Handle UserAlreadyExistsException
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        ApiResponse<String> response = new ApiResponse<>(null, ex.getMessage(), false, HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedAccess(UnauthorizedAccessException ex) {
        ApiResponse<Void> response = new ApiResponse<>(null, ex.getMessage(), false, HttpStatus.FORBIDDEN.value());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<String>> handleJwtExceptions(JwtException ex) {
        ApiResponse<String> response = new ApiResponse<>("jwt-error", ex.getMessage(), false, HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

//    @ExceptionHandler(IOException.class)
//    public ResponseEntity<ApiResponse<String>> handleUserAlreadyExists(IOException ex) {
//        ApiResponse<String> response = new ApiResponse<>(null, "Error while handling file", false, HttpStatus.BAD_REQUEST.value());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//    }

    // Handle other runtime exceptions
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ApiResponse<String>> handleRuntimeExceptions(RuntimeException ex) {
//        ApiResponse<String> response = new ApiResponse<>(null, ex.getMessage(), false, HttpStatus.INTERNAL_SERVER_ERROR.value());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }
}
