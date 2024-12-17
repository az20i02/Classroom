package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnrollmentDto {
    private Long id;
    private String studentName;
    private String courseName;
}


