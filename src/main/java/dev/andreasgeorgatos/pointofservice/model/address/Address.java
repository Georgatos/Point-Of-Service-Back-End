package dev.andreasgeorgatos.pointofservice.model.address;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "city", nullable = false)
    @NotBlank(message = "You must specify your city address.")
    private String city;

    @Column(name = "address", nullable = false)
    @NotBlank(message = "You must specify an address.")
    private String address;

    @Column(name = "address_number", nullable = false)
    @Positive(message = "The street address must be a positive number.")
    @NotBlank(message = "You must specify your street address number.")
    private long addressNumber;

    @Column(name = "postal_code", nullable = false)
    @Positive(message = "The postal code must be a positive number.")
    @NotBlank(message = "You must specify your area postal code.")
    private long postalCode;

    @Column(name = "story_level", nullable = false)
    @Positive(message = "Your story level must be positive.")
    @NotBlank(message = "You must specify the story level.")
    private int storyLevel;

    @Column(name = "door_bell_ring_name", nullable = false, length = 50)
    @NotBlank(message = "You must specify the name of your door ring bell.")
    private String doorRingBellName;
}
