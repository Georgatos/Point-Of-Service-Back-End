package dev.andreasgeorgatos.tsilikos.model.order;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "order_statuses")
public class OrderStatuses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "status", nullable = false, length = 20)
    private String status;
}
