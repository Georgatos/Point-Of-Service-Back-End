package dev.andreasgeorgatos.tsilikos.model.user;

import dev.andreasgeorgatos.tsilikos.model.order.Order;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order orderId;

    @Column(name = "rating", nullable = false, precision = 2)
    private double rating;

    @Column(name = "comment", nullable = false, length = 300)
    private String comment;

    @Column(name = "review_date", nullable = false)
    private LocalDateTime reviewDate;

}
