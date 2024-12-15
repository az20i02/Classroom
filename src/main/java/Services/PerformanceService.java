package Services;

import Dto.PerformanceChartDto;
import Repository.EnrollmentRepository;
import Repository.QuizAttemptRepository;
import Repository.AssignmentSubmissionRepository; // Ensure this matches your package structure
import tables.Enrollment;
import tables.QuizAttempt; // Ensure this matches your package structure

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Apache POI imports
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// Java IO import
import java.io.ByteArrayOutputStream;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceService {
    private final EnrollmentRepository enrollmentRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final AssignmentSubmissionRepository submissionRepository;

    public PerformanceChartDto getCoursePerformanceChart(Long courseId) {
        // Retrieve data from DB and populate chart DTO
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        HashMap<String, Double> dataPoints = new HashMap<>();
        for (Enrollment e : enrollments) {
            // Example: average quiz scores for a student
            double avgScore = quizAttemptRepository.findByStudentIdAndQuizId(e.getStudent().getId(), null)
                    .stream().mapToDouble(QuizAttempt::getScore).average().orElse(0.0);
            dataPoints.put(e.getStudent().getFullName(), avgScore);
        }

        PerformanceChartDto dto = new PerformanceChartDto();
        dto.setDataPoints(dataPoints);
        return dto;
    }

    public byte[] generateExcelReport(Long courseId) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Performance");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Student");
        header.createCell(1).setCellValue("Avg Quiz Score");
        header.createCell(2).setCellValue("Avg Assignment Grade");

        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        int rowIdx = 1;
        for (Enrollment e : enrollments) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(e.getStudent().getFullName());

            double avgQuiz = quizAttemptRepository.findByStudentIdAndQuizId(e.getStudent().getId(), null)
                    .stream().mapToDouble(QuizAttempt::getScore).average().orElse(0.0);

            double avgAssign = submissionRepository.findByStudentId(e.getStudent().getId())
                    .stream().mapToDouble(s->s.getGrade()==null?0.0:s.getGrade()).average().orElse(0.0);

            row.createCell(1).setCellValue(avgQuiz);
            row.createCell(2).setCellValue(avgAssign);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }
}
