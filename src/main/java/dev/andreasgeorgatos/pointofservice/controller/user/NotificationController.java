package dev.andreasgeorgatos.pointofservice.controller.user;

import dev.andreasgeorgatos.pointofservice.model.user.Notification;
import dev.andreasgeorgatos.pointofservice.service.user.NotificationService;
import dev.andreasgeorgatos.pointofservice.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @GetMapping()
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        return notificationService.getNotificationById(id);
    }

    @PostMapping()
    public ResponseEntity<?> createNotification(@Valid @RequestBody Notification notification, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return notificationService.createNotification(notification);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editNotificationById(@Valid @PathVariable Long id, @RequestBody Notification notification, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return notificationService.editNotificationById(id, notification);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotificationById(@Valid @PathVariable Long id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }

        return notificationService.deleteNotificationById(id);
    }

}
