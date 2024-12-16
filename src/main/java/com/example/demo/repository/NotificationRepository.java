package com.example.demo.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.tables.Notification;


import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientIdAndIsRead(Long recipientId, boolean isRead);
    List<Notification> findByRecipientId(Long recipientId);
}

