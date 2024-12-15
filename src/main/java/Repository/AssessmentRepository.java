package Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tables.Assessment;


public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
}

