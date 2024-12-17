package com.example.demo.tables;
import jakarta.persistence.*;
import lombok.*;


import java.util.Set;

@Entity
@Table(name="users")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role; // ADMIN, INSTRUCTOR, STUDENT

    private String email;

    private String fullName;

    // For example, a student can enroll in many courses
    @OneToMany(mappedBy = "student")
    private Set<Enrollment> enrollments;

    // ...
}
