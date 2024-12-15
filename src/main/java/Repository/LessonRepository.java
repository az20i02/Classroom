package Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tables.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
