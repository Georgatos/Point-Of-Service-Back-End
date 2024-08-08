package dev.andreasgeorgatos.pointofservice.model.order;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "orders_history")
public class OrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @ManyToOne
    @JoinColumn(name = "order_status_id", nullable = false)
    private OrderStatuses orderStatus;
}
