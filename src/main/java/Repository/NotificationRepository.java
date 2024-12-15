package Repository;



import org.springframework.data.jpa.repository.JpaRepository;
import tables.Notification;


import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientIdAndIsRead(Long recipientId, boolean isRead);
    List<Notification> findByRecipientId(Long recipientId);
}

