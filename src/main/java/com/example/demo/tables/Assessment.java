package com.example.demo.tables;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="assessment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assessment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AssessmentType type; // QUIZ or ASSIGNMENT

    private String title;

    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;

    // For quizzes, questions handled separately
    // For assignments, instructions and possibly file attachments
    private String instructions;
}
