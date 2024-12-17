package com.example.demo.services;

import com.example.demo.tables.UserRole;
import com.example.demo.dto.CourseDto;
import com.example.demo.dto.EnrollmentDto;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private NotificationService notificationService;

    // CourseService.java
    public CourseDto createCourse(CourseDto dto, User instructor) {
        Course course = Course.builder()
                .name(dto.getName()) // Use `name` field
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .instructor(instructor)
                .build();

        course = courseRepository.save(course);
        dto.setId(course.getId());
        return dto;
    }



    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(course -> {
                    CourseDto dto = new CourseDto();
                    dto.setId(course.getId());
                    dto.setName(course.getName());
                    dto.setDescription(course.getDescription());
                    dto.setDuration(course.getDuration());
                    dto.setInstructorId(course.getInstructor().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    public void enrollStudent(Long courseId, User student) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));


        System.out.println("User Role : "+student.getRole());
        if (!student.getRole().equals(UserRole.ADMIN)) { // Use equals() for enums
            // Create Enrollment
            Enrollment enrollment = Enrollment.builder()
                    .course(course)
                    .student(student)
                    .build();

            enrollmentRepository.save(enrollment);
        }

        // Notify the student
        notificationService.sendNotification(student.getId(), "You have successfully enrolled in the course!");

        // Notify the instructor
        Long instructorId = courseRepository.findInstructorByCourseId(courseId);
        notificationService.sendNotification(instructorId, "A new student has enrolled in your course!");
    }



    public List<EnrollmentDto> getEnrolledStudents(Long courseId) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);

        return enrollments.stream()
                .map(e -> new EnrollmentDto(
                        e.getId(),
                        e.getStudent().getUsername(),  // Assuming `getUsername()` exists in User
                        e.getCourse().getName()        // Assuming `getName()` exists in Course
                ))
                .collect(Collectors.toList());
    }






    public Lesson addLessonToCourse(Long courseId, String title, String videoUrl, String resourceFile) {
        // Find the course
        Course c = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        // Create the new lesson
        Lesson lesson = Lesson.builder()
                .title(title)
                .videoUrl(videoUrl)
                .resourceFile(resourceFile)
                .course(c)
                .build();

        // Save the lesson
        Lesson savedLesson = lessonRepository.save(lesson);

        // Notify enrolled students
        List<Long> enrolledStudents = courseRepository.findEnrolledStudents(courseId); // Assuming this method exists
        for (Long studentId : enrolledStudents) {
            String message = "A new lesson has been added to your course: " + title;
            notificationService.sendNotification(studentId, message);
        }

        return savedLesson;
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