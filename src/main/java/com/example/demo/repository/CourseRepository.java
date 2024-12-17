package com.example.demo.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.tables.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT u.id FROM User u JOIN Course c ON c.instructor.id = u.id WHERE c.id = :courseId")
    Long findInstructorByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT s.id FROM User s JOIN Course c  WHERE c.id = :courseId")
    List<Long> findEnrolledStudents(@Param("courseId") Long courseId);



}

