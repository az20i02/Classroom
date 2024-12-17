package com.example.demo.controllers;

import com.example.demo.dto.CourseDto;
import com.example.demo.services.CourseService;
import com.example.demo.services.JwtUtil;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import com.example.demo.tables.User;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public CourseController(CourseService courseService, UserService userService, JwtUtil jwtUtil) {
        this.courseService = courseService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public CourseDto createCourse(@Valid @RequestBody CourseDto dto, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated user roles: " + auth.getAuthorities());

        // Check if the user has 'ROLE_INSTRUCTOR' or 'ROLE_ADMIN'
        boolean isAuthorized = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_INSTRUCTOR") ||
                                grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAuthorized) {
            throw new RuntimeException("Only instructors or admins can create courses.");
        }

        // Proceed with course creation
        Long userId = jwtUtil.extractUserId(request);
        User instructor = userService.findById(userId);
        return courseService.createCourse(dto, instructor);
    }

    @GetMapping
    public List<CourseDto> getAllCourses() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated user roles: " + auth.getAuthorities());

        // Check if the user has 'ROLE_ADMIN', 'ROLE_INSTRUCTOR', or 'ROLE_STUDENT'
        boolean isAuthorized = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_ADMIN") ||
                                grantedAuthority.getAuthority().equals("ROLE_INSTRUCTOR") ||
                                grantedAuthority.getAuthority().equals("ROLE_STUDENT"));

        if (!isAuthorized) {
            throw new RuntimeException("Access denied.");
        }

        return courseService.getAllCourses();
    }

    @PostMapping("/{courseId}/enroll")
    public void enrollStudent(@PathVariable Long courseId, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated user roles: " + auth.getAuthorities());

        boolean isStudent = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_STUDENT"));

        if (!isStudent) {
            throw new RuntimeException("Only students can enroll in courses.");
        }

        Long userId = jwtUtil.extractUserId(request);
        User student = userService.findById(userId);
        courseService.enrollStudent(courseId, student);
    }

    @GetMapping("/{courseId}/students")
    public List<?> getEnrolledStudents(@PathVariable Long courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated user roles: " + auth.getAuthorities());

        boolean isAuthorized = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_INSTRUCTOR") ||
                                grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAuthorized) {
            throw new RuntimeException("Only instructors or admins can view enrolled students.");
        }

        return courseService.getEnrolledStudents(courseId);
    }

    @PostMapping("/{courseId}/lessons")
    public void addLesson(@PathVariable Long courseId,
                          @RequestParam String title,
                          @RequestParam String videoUrl,
                          @RequestParam String resourceFile) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated user roles: " + auth.getAuthorities());

        boolean isAuthorized = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_INSTRUCTOR") ||
                                grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAuthorized) {
            throw new RuntimeException("Only instructors or admins can add lessons.");
        }

        courseService.addLessonToCourse(courseId, title, videoUrl, resourceFile);
    }

    @PostMapping("/lessons/{lessonId}/generate-otp")
    public String generateOtp(@PathVariable Long lessonId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated user roles: " + auth.getAuthorities());

        boolean isAuthorized = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_INSTRUCTOR") ||
                                grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAuthorized) {
            throw new RuntimeException("Only instructors or admins can generate OTP.");
        }

        return courseService.generateOtpForLesson(lessonId);
    }

    @PostMapping("/lessons/{lessonId}/attend")
    public boolean attendLesson(@PathVariable Long lessonId, @RequestParam String otp, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated user roles: " + auth.getAuthorities());

        boolean isStudent = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_STUDENT"));

        if (!isStudent) {
            throw new RuntimeException("Only students can attend lessons.");
        }

        Long userId = jwtUtil.extractUserId(request);
        User student = userService.findById(userId);
        return courseService.markAttendance(lessonId, student, otp);
    }
}
