package com.example.demo.controllers;

import com.example.demo.dto.AssessmentDto;
import com.example.demo.dto.QuizSubmissionDto;
import com.example.demo.services.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.tables.User;

@RestController
@RequestMapping("/assessments")
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final NotificationService notificationService;
    private final JwtUtil jwtUtil;

    public AssessmentController(AssessmentService assessmentService,
                                UserService userService,
                                FileStorageService fileStorageService,
                                NotificationService notificationService,
                                JwtUtil jwtUtil) {
        this.assessmentService = assessmentService;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.notificationService = notificationService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public AssessmentDto createAssessment(@RequestBody AssessmentDto dto, HttpServletRequest request) {
        Long userId = jwtUtil.extractUserId(request);
        User instructor = userService.findById(userId);
        return assessmentService.createAssessment(dto, instructor);
    }

    // Other methods remain the same, with jwtUtil called to extract userId.
}
