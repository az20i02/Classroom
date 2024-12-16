package com.example.demo.dto;



import lombok.Data;

@Data
public class AssessmentDto {
    private Long id;
    private String type; // QUIZ or ASSIGNMENT
    private String title;
    private Long courseId;
    private String instructions;
}

