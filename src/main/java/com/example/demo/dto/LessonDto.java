package com.example.demo.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LessonDto {
    private String title;
    private String videoUrl;
    private String resourceFile;
}
