package dev.andreasgeorgatos.tsilikos.model.payment;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "payment_statuses")
public class PaymentStatuses {

    @Id
    @GeneratedValue(generator = "increment")
    @Column(name = "id", nullable = false)
    private long id;


    @Column(name = "payment_status", nullable = false)
    private String status;
}
