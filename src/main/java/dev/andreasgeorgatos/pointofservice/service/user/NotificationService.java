package dev.andreasgeorgatos.pointofservice.service.user;


import dev.andreasgeorgatos.pointofservice.model.user.Notification;
import dev.andreasgeorgatos.pointofservice.repository.users.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();

        if (notifications.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(notifications);
    }

    public ResponseEntity<Notification> getNotificationById(long id) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);

        return optionalNotification.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<Notification> createNotification(Notification Notification) {
        Notification savedNotification = notificationRepository.save(Notification);
        return ResponseEntity.ok(savedNotification);
    }

    @Transactional
    public ResponseEntity<Notification> editNotificationById(long id, Notification notification) {
        Optional<Notification> foundNotification = notificationRepository.findById(id);

        if (foundNotification.isPresent()) {
            Notification oldNotification = foundNotification.get();

            oldNotification.setNotificationDate(notification.getNotificationDate());
            oldNotification.setNotificationType(notification.getNotificationType());
            oldNotification.setMessage(notification.getMessage());
            oldNotification.setUserId(notification.getUserId());
            oldNotification.setOrderStatusId(notification.getOrderStatusId());

            Notification savedNotification = notificationRepository.save(notification);

            return ResponseEntity.ok(savedNotification);
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<Notification> deleteNotificationById(long id) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);

        if (optionalNotification.isPresent()) {
            notificationRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
