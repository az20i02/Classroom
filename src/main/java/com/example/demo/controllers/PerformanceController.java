package com.example.demo.controllers;

import com.example.demo.dto.PerformanceChartDto;
import com.example.demo.services.PerformanceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/performance")
public class PerformanceController {
    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @GetMapping("/course/{courseId}/chart")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public PerformanceChartDto getCourseChart(@PathVariable Long courseId) {
        return performanceService.getCoursePerformanceChart(courseId);
    }

    @GetMapping("/course/{courseId}/report")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public byte[] getExcelReport(@PathVariable Long courseId) throws Exception {
        return performanceService.generateExcelReport(courseId);
    }
}

