package com.example.demo.tables;



import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "enrollments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false) // Foreign key for student
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false) // Foreign key for course
    private Course course;


    public Course getCourse() {
        return course;
    }
}

