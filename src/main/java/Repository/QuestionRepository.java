package Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tables.Question;

import java.util.List;
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByAssessmentId(Long assessmentId);
}

