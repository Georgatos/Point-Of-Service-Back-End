package dev.andreasgeorgatos.tsilikos.model.rewards;

import dev.andreasgeorgatos.tsilikos.model.address.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "referral_source")
public class ReferralSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    @NotNull(message = "Please specify the name of the source.")
    private String name;

    @Column(name = "description")
    @NotNull(message = "Please add an acceptable description.")
    private String description;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
}
