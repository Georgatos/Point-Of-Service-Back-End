package dev.andreasgeorgatos.pointofservice.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "user_types")
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "user_type", nullable = false, length = 30)
    @NotBlank(message = "Please specify the type of the user.")
    private String userType;
}
