package com.example.demo.dto;

import lombok.Data;

@Data
public class CourseDto {
    private Long id;
    private String title;
    private String description;
    private String duration;
    private Long instructorId;
}
