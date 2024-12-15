package Controllers;


import Dto.AssessmentDto;
import Dto.QuizSubmissionDto;
import Services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import tables.User;

@RestController
@RequestMapping("/assessments")
public class AssessmentController {
    private final AssessmentService assessmentService;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final NotificationService notificationService;

    public AssessmentController(AssessmentService assessmentService, UserService userService, FileStorageService fileStorageService, NotificationService notificationService) {
        this.assessmentService = assessmentService;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.notificationService = notificationService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public AssessmentDto createAssessment(@RequestBody AssessmentDto dto, HttpServletRequest request) {
        Long userId = JwtUtil.extractUserId(request);
        User instructor = userService.findById(userId);
        AssessmentDto created = assessmentService.createAssessment(dto, instructor);
        return created;
    }

    @PostMapping("/{assessmentId}/questions")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public void addQuestion(@PathVariable Long assessmentId, @RequestParam String questionText, @RequestParam String questionType, @RequestParam String options, @RequestParam String correctAnswer, HttpServletRequest request) {
        Long userId = JwtUtil.extractUserId(request);
        userService.findById(userId); // ensure existence
        assessmentService.addQuestionToQuiz(assessmentId, questionText, questionType, options, correctAnswer);
    }

    @PostMapping("/quiz/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public double submitQuiz(@RequestBody QuizSubmissionDto submission, HttpServletRequest request) {
        Long userId = JwtUtil.extractUserId(request);
        User student = userService.findById(userId);
        double score = assessmentService.submitQuiz(submission, student);
        // notify student about quiz result
        notificationService.sendNotification(student.getId(), "Your quiz score is: " + score);
        return score;
    }

    @PostMapping("/{assessmentId}/submit-assignment")
    @PreAuthorize("hasRole('STUDENT')")
    public String submitAssignment(@PathVariable Long assessmentId, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        Long userId = JwtUtil.extractUserId(request);
        User student = userService.findById(userId);
        String path = fileStorageService.storeFile(file.getBytes(), file.getOriginalFilename());
        assessmentService.submitAssignment(assessmentId, student, path);
        notificationService.sendNotification(student.getId(), "Your assignment was submitted successfully.");
        return "Assignment submitted";
    }

    @PostMapping("/grade-assignment/{submissionId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public String gradeAssignment(@PathVariable Long submissionId, @RequestParam Double grade, @RequestParam String feedback, HttpServletRequest request) {
        Long userId = JwtUtil.extractUserId(request);
        User instructor = userService.findById(userId);
        var sub = assessmentService.gradeAssignment(submissionId, grade, feedback, instructor);
        notificationService.sendNotification(sub.getStudent().getId(), "Your assignment was graded: " + grade);
        return "Assignment graded";
    }
}