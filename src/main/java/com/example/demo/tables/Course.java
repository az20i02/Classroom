
package com.example.demo.tables;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name="courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String title;
    private String description;
    private String duration; // e.g. "10 weeks"

    @ManyToOne
    @JoinColumn(name="instructor_id")
    private User instructor;


    @OneToMany(mappedBy="course", cascade=CascadeType.ALL)
    private Set<Lesson> lessons;

    @OneToMany(mappedBy="course")
    private Set<Enrollment> enrollments;
}
