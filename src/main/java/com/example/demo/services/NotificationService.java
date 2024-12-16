package com.example.demo.services;

import com.example.demo.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.tables.Notification;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void sendNotification(Long recipientId, String message) {
        Notification notif = Notification.builder()
                .recipientId(recipientId)
                .message(message)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notif);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByRecipientIdAndIsRead(userId, false);
    }

    public List<Notification> getAllNotifications(Long userId) {
        return notificationRepository.findByRecipientId(userId);
    }

    public void markAsRead(Long notificationId) {
        Notification notif = notificationRepository.findById(notificationId).orElseThrow();
        notif.setRead(true);
        notificationRepository.save(notif);
    }
}