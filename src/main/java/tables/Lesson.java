package tables;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String videoUrl;
    private String resourceFile; // e.g. path to PDF

    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;

    private String attendanceOtp; // For the OTP attendance
}

