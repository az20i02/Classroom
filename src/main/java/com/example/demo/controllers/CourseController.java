package com.example.demo.controllers;

import com.example.demo.dto.CourseDto;
import com.example.demo.services.CourseService;
import com.example.demo.services.JwtUtil;
import com.example.demo.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import com.example.demo.tables.User;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;
    private final JwtUtil jwtUtil; // Inject JwtUtil as a dependency

    public CourseController(CourseService courseService, UserService userService, JwtUtil jwtUtil) {
        this.courseService = courseService;
        this.userService = userService;
        this.jwtUtil = jwtUtil; // Assign the injected JwtUtil
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public CourseDto createCourse(@RequestBody CourseDto dto, HttpServletRequest request) {
        Long userId = jwtUtil.extractUserId(request); // Use instance method of JwtUtil
        User instructor = userService.findById(userId);
        return courseService.createCourse(dto, instructor);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR', 'ROLE_STUDENT')")
    public List<CourseDto> getAllCourses() {
        return courseService.getAllCourses();
    }


    @PostMapping("/{courseId}/enroll")
    @PreAuthorize("hasRole('STUDENT')")
    public void enrollStudent(@PathVariable Long courseId, HttpServletRequest request) {
        Long userId = jwtUtil.extractUserId(request); // Use instance method of JwtUtil
        User student = userService.findById(userId);
        courseService.enrollStudent(courseId, student);
    }


    @GetMapping("/{courseId}/students")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public List<?> getEnrolledStudents(@PathVariable Long courseId) {
        return courseService.getEnrolledStudents(courseId);
    }

    @PostMapping("/{courseId}/lessons")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public void addLesson(@PathVariable Long courseId,
                          @RequestParam String title,
                          @RequestParam String videoUrl,
                          @RequestParam String resourceFile) {
        courseService.addLessonToCourse(courseId, title, videoUrl, resourceFile);
    }

    @PostMapping("/lessons/{lessonId}/generate-otp")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public String generateOtp(@PathVariable Long lessonId) {
        return courseService.generateOtpForLesson(lessonId);
    }

    @PostMapping("/lessons/{lessonId}/attend")
    @PreAuthorize("hasRole('STUDENT')")
    public boolean attendLesson(@PathVariable Long lessonId, @RequestParam String otp, HttpServletRequest request) {
        Long userId = jwtUtil.extractUserId(request); // Use instance method of JwtUtil
        User student = userService.findById(userId);
        return courseService.markAttendance(lessonId, student, otp);
    }
}
