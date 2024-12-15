package Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tables.QuizAttempt;

import java.util.List;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByStudentIdAndQuizId(Long studentId, Long quizId);
}

