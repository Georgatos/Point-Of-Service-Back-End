package dev.andreasgeorgatos.pointofservice.model.item;

import dev.andreasgeorgatos.pointofservice.enums.TableStatus;
import dev.andreasgeorgatos.pointofservice.model.order.Order;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="order_item")

public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "itemId", nullable = false)
    private Item item;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updated_at;


}
