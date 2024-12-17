package com.example.demo.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.tables.Notification;


import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Find unread notifications for a specific recipient
    List<Notification> findByRecipientIdAndIsRead(Long recipientId, boolean isRead);

    // Find all notifications for a recipient
    List<Notification> findByRecipientId(Long recipientId);

    // Find notifications by the sender (optional use case)
    List<Notification> findByUserId(Long userId);
}


