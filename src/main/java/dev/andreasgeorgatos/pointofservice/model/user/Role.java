package dev.andreasgeorgatos.pointofservice.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank(message = "Role name can't be blank.")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
