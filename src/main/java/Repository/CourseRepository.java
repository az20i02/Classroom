package Repository;



import org.springframework.data.jpa.repository.JpaRepository;
import tables.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}

