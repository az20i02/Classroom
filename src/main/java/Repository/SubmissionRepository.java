package Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tables.AssignmentSubmission;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {
    List<AssignmentSubmission> findByAssignmentId(Long assignmentId);
    List<AssignmentSubmission> findByStudentId(Long studentId);
}

