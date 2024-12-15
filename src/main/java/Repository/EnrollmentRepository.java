package Repository;



import org.springframework.data.jpa.repository.JpaRepository;
import tables.Enrollment;


import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByCourseId(Long courseId);
}

