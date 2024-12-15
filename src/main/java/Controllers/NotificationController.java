package Controllers;


import Services.JwtUtil;
import Services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import tables.Notification;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/unread")
    @PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN')")
    public List<Notification> getUnread(HttpServletRequest request) {
        Long userId = JwtUtil.extractUserId(request);
        return notificationService.getUnreadNotifications(userId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN')")
    public List<Notification> getAll(HttpServletRequest request) {
        Long userId = JwtUtil.extractUserId(request);
        return notificationService.getAllNotifications(userId);
    }

    @PostMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN')")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }
}