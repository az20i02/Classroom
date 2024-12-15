package Controllers;


import Dto.CourseDto;
import Services.CourseService;
import Services.JwtUtil;
import Services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import tables.User;

import java.util.List;


@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;
    private final UserService userService;

    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public CourseDto createCourse(@RequestBody CourseDto dto, HttpServletRequest request) {
        Long userId = JwtUtil.extractUserId(request);
        User instructor = userService.findById(userId);
        return courseService.createCourse(dto, instructor);
    }

    @PostMapping("/{courseId}/enroll")
    @PreAuthorize("hasRole('STUDENT')")
    public void enrollStudent(@PathVariable Long courseId, HttpServletRequest request) {
        Long userId = JwtUtil.extractUserId(request);
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
    public void addLesson(@PathVariable Long courseId, @RequestParam String title, @RequestParam String videoUrl, @RequestParam String resourceFile) {
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
        User student = userService.findById(JwtUtil.extractUserId(request));
        return courseService.markAttendance(lessonId, student, otp);
    }
}