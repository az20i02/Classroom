package tables;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String otp; // generated OTP for lesson attendance

    @ManyToOne
    @JoinColumn(name="student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name="lesson_id")
    private Lesson lesson;

    private boolean attended;
}
