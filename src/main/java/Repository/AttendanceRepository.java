package Repository;



import org.springframework.data.jpa.repository.JpaRepository;
import tables.Attendance;


public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}

