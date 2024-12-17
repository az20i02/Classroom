package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseDto {
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private String description;
    private String duration;
    private Long instructorId;
}
