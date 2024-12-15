package Dto;



import lombok.Data;
import java.util.Map;

@Data
public class QuizSubmissionDto {
    private Long quizId;
    // map<questionId, answer>
    private Map<Long, String> answers;
}
