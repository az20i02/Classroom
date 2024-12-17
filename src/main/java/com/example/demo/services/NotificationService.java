package com.example.demo.services;

import com.example.demo.repository.NotificationRepository;
import com.example.demo.tables.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    /**
     * Send a notification to a recipient.
     *
     * @param recipientId the ID of the recipient
     * @param message     the notification message
         (optional) the ID of the user triggering the notification
     */
    public void sendNotification(Long recipientId, String message) {
        Notification notification = Notification.builder()
                .recipientId(recipientId)
                .message(message)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();


        notificationRepository.save(notification);
    }

    /**
     * Get all unread notifications for a recipient.
     *
     * @param recipientId the ID of the recipient
     * @return list of unread notifications
     */
    public List<Notification> getUnreadNotifications(Long recipientId) {
        return notificationRepository.findByRecipientIdAndIsRead(recipientId, false);
    }

    /**
     * Get all notifications for a recipient.
     *
     * @param recipientId the ID of the recipient
     * @return list of all notifications
     */
    public List<Notification> getAllNotifications(Long recipientId) {
        return notificationRepository.findByRecipientId(recipientId);
    }

    /**
     * Mark a notification as read.
     *
     * @param notificationId the ID of the notification
     */
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found."));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
