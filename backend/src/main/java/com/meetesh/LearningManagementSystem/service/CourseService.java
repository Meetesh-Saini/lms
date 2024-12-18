package com.meetesh.LearningManagementSystem.service;

import com.meetesh.LearningManagementSystem.entity.Course;
import com.meetesh.LearningManagementSystem.entity.User;
import com.meetesh.LearningManagementSystem.entry.CourseEntry;
import com.meetesh.LearningManagementSystem.exception.CourseNotFoundException;
import com.meetesh.LearningManagementSystem.exception.UnauthorizedAccessException;
import com.meetesh.LearningManagementSystem.repository.CourseRepository;
import com.meetesh.LearningManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    // Create or Update a Course
    public Course saveCourse(CourseEntry courseEntry) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!userRepository.existsByUsername(username)){
            throw new UsernameNotFoundException("User not found");
        }
        User loggedInUser = userRepository.findByUsername(username);
        Course course = new Course();
        course.setTitle(courseEntry.getTitle());
        course.setDescription(courseEntry.getDescription());
        course.setCategory(courseEntry.getCategory());
        course.setInstructor(loggedInUser);
        course.setStatus("active");
        course.setCreatedDate(new Date());
        return courseRepository.save(course);
    }

    public void updateCourse(Long courseId, CourseEntry updatedCourse) {
        Course course = getCourseById(courseId);

        // Get the logged-in user's username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Check if the logged-in user is the instructor of the course
        if (!course.getInstructor().getUsername().equals(username)) {
            throw new UnauthorizedAccessException("You are not authorized to update this course");
        }

        course.setTitle(updatedCourse.getTitle());
        course.setDescription(updatedCourse.getDescription());
        course.setCategory(updatedCourse.getCategory());
        course.setStatus(updatedCourse.getStatus());
        courseRepository.save(course);
    }

    // Get all Courses
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Get Course by Id
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new CourseNotFoundException("Course not found"));
    }

    // Delete a Course
    public void deleteCourse(Long courseId) {
        Course course = getCourseById(courseId);

        // Get the logged-in user's username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Check if the logged-in user is the instructor of the course
        if (!course.getInstructor().getUsername().equals(username)) {
            throw new UnauthorizedAccessException("You are not authorized to delete this course");
        }

        courseRepository.delete(course);
    }
}
