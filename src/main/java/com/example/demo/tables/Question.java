package com.example.demo.tables;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="assessment_id")
    private Assessment assessment; // belongs to a quiz (Assessment with type QUIZ)

    private String questionText;
    private String questionType; // MCQ, TRUE_FALSE, SHORT_ANSWER
    private String options; // JSON or CSV string for MCQ options
    private String correctAnswer; // store correct answer
}

