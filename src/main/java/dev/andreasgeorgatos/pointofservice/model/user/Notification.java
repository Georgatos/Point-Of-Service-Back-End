package dev.andreasgeorgatos.pointofservice.model.user;

import dev.andreasgeorgatos.pointofservice.model.order.OrderStatuses;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long notificationId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "notification_message", nullable = false)
    private String message;

    @Column(name = "notification_date", nullable = false)
    private LocalDateTime notificationDate;

    @ManyToOne
    @JoinColumn(name = "order_status_id", nullable = false)
    private OrderStatuses orderStatusId;

    @Column(name = "notification_type", nullable = false)
    private String notificationType;
}
