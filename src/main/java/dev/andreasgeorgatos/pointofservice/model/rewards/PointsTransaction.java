package dev.andreasgeorgatos.pointofservice.model.rewards;

import dev.andreasgeorgatos.pointofservice.model.order.Order;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "points_transactions")
public class PointsTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shifts_id")
    private long id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "points")
    private double points;

    @Column(name = "transaction_type")
    private String transactionType;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order orderId;
}
