package dev.andreasgeorgatos.pointofservice.model.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "order_types")
public class OrderType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "type")
    @NotBlank(message = "Please specify the type of the order.")
    private String type;


}
