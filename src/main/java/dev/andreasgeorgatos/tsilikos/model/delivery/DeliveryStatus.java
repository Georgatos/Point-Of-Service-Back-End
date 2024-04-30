package dev.andreasgeorgatos.tsilikos.model.delivery;

import dev.andreasgeorgatos.tsilikos.model.order.Order;
import dev.andreasgeorgatos.tsilikos.model.order.OrderStatuses;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "delivery_status")
public class DeliveryStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "order_status_id", nullable = false)
    private OrderStatuses orderStatus;

    @Column(name = "delivery_date", nullable = false)
    private LocalDateTime deliveryDate;

}
