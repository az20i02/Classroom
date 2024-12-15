package tables;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentSubmission {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name="assessment_id")
    private Assessment assignment; // must be ASSIGNMENT type

    private String submittedFilePath;
    private Double grade;
    private String feedback;
}

