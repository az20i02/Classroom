package com.example.demo.services;


import com.example.demo.dto.CourseDto;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.tables.Course;
import com.example.demo.tables.Enrollment;
import com.example.demo.tables.Lesson;
import com.example.demo.tables.User;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;
    private final OtpService otpService;

    public CourseDto createCourse(CourseDto dto, User instructor) {
        Course c = Course.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .instructor(instructor)
                .build();
        c = courseRepository.save(c);
        dto.setId(c.getId());
        return dto;
    }

    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(course -> {
                    CourseDto dto = new CourseDto();
                    dto.setId(course.getId());
                    dto.setTitle(course.getTitle());
                    dto.setDescription(course.getDescription());
                    dto.setDuration(course.getDuration());
                    dto.setInstructorId(course.getInstructor().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    public void enrollStudent(Long courseId, User student) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        Enrollment enroll = Enrollment.builder().course(course).student(student).build();
        enrollmentRepository.save(enroll);
    }

    public List<Enrollment> getEnrolledStudents(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    public Lesson addLessonToCourse(Long courseId, String title, String videoUrl, String resourceFile) {
        Course c = courseRepository.findById(courseId).orElseThrow();
        Lesson lesson = Lesson.builder()
                .title(title)
                .videoUrl(videoUrl)
                .resourceFile(resourceFile)
                .course(c)
                .build();
        return lessonRepository.save(lesson);
    }

    public String generateOtpForLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        String otp = otpService.generateOtp();
        lesson.setAttendanceOtp(otp);
        lessonRepository.save(lesson);
        return otp;
    }

    public boolean markAttendance(Long lessonId, User student, String otp) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        if (lesson.getAttendanceOtp() != null && lesson.getAttendanceOtp().equals(otp)) {
            // create attendance record
            // This can be handled in AttendanceService or here
            return true;
        }
        return false;
    }

    public Course findById(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow();
    }
}