package Services;


import Dto.AssessmentDto;
import Dto.QuizSubmissionDto;
import Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tables.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentService {
    private final AssessmentRepository assessmentRepository;
    private final CourseRepository courseRepository;
    private final QuestionRepository questionRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final SubmissionRepository submissionRepository;

    public AssessmentDto createAssessment(AssessmentDto dto, User instructor) {
        Course c = courseRepository.findById(dto.getCourseId()).orElseThrow();
        // verify instructor is course instructor
        if (!c.getInstructor().getId().equals(instructor.getId()) && instructor.getRole()!=UserRole.ADMIN) {
            throw new RuntimeException("Not Authorized");
        }
        AssessmentType type = AssessmentType.valueOf(dto.getType().toUpperCase());
        Assessment a = Assessment.builder()
                .title(dto.getTitle())
                .type(type)
                .course(c)
                .instructions(dto.getInstructions())
                .build();
        a = assessmentRepository.save(a);
        dto.setId(a.getId());
        return dto;
    }

    // For quiz creation, we would have a method to add questions
    public Question addQuestionToQuiz(Long assessmentId, String questionText, String questionType, String options, String correctAnswer) {
        Assessment a = assessmentRepository.findById(assessmentId).orElseThrow();
        if (a.getType()!=AssessmentType.QUIZ) throw new RuntimeException("Not a quiz");
        Question q = Question.builder()
                .assessment(a)
                .questionText(questionText)
                .questionType(questionType)
                .options(options)
                .correctAnswer(correctAnswer)
                .build();
        return questionRepository.save(q);
    }

    public double submitQuiz(QuizSubmissionDto submission, User student) {
        Assessment quiz = assessmentRepository.findById(submission.getQuizId()).orElseThrow();
        if (quiz.getType()!=AssessmentType.QUIZ) {
            throw new RuntimeException("Not a quiz");
        }
        // Randomized question selection could be done at attempt start.
        // For simplicity, assume all quiz questions are answered.
        List<Question> questions = questionRepository.findByAssessmentId(quiz.getId());
        double score=0.0;
        for (Question q: questions) {
            String studentAnswer = submission.getAnswers().get(q.getId());
            if (studentAnswer!=null && studentAnswer.equalsIgnoreCase(q.getCorrectAnswer())) {
                score+=1.0;
            }
        }
        double finalScore = (questions.size()>0) ? (score/questions.size())*100.0 : 0.0;

        QuizAttempt attempt = QuizAttempt.builder()
                .quiz(quiz)
                .student(student)
                .score(finalScore)
                .build();
        quizAttemptRepository.save(attempt);
        return finalScore;
    }

    public AssignmentSubmission submitAssignment(Long assignmentId, User student, String filePath) {
        Assessment assignment = assessmentRepository.findById(assignmentId).orElseThrow();
        if (assignment.getType()!=AssessmentType.ASSIGNMENT) {
            throw new RuntimeException("Not an assignment");
        }
        AssignmentSubmission submission = AssignmentSubmission.builder()
                .assignment(assignment)
                .student(student)
                .submittedFilePath(filePath)
                .build();
        return submissionRepository.save(submission);
    }

    public AssignmentSubmission gradeAssignment(Long submissionId, Double grade, String feedback, User instructor) {
        AssignmentSubmission sub = submissionRepository.findById(submissionId).orElseThrow();
        // verify instructor is the assignment's course instructor
        if (!sub.getAssignment().getCourse().getInstructor().getId().equals(instructor.getId()) && instructor.getRole()!=UserRole.ADMIN) {
            throw new RuntimeException("Not Authorized");
        }
        sub.setGrade(grade);
        sub.setFeedback(feedback);
        return submissionRepository.save(sub);
    }
}