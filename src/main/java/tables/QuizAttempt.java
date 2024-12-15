package tables;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="quiz_attempts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttempt {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name="assessment_id")
    private Assessment quiz; // must be a QUIZ type

    private double score;
}

