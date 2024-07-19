package dev.andreasgeorgatos.pointofservice.model.payment;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "payment_methods")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "payment_method", nullable = false, length = 20)
    @NotBlank(message = "You must specify the type of payment, cash, card, etc.")
    private String method;
}
