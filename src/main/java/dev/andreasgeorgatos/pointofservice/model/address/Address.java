package dev.andreasgeorgatos.pointofservice.model.address;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @PositiveOrZero(message = "The street address must be a positive number.")
    private long addressNumber;

    @Column(name = "postal_code", nullable = false)
    @NotBlank(message = "You must specify your area postal code.")
    private String postalCode;

    @Column(name = "story_level", nullable = false)
    @PositiveOrZero(message = "Your story level must be positive.")
    private long storyLevel;

    @Column(name = "door_bell_ring_name", nullable = false, length = 50)
    @NotBlank(message = "You must specify the name of your door ring bell.")
    private String doorRingBellName;

    public Address(String city, String address, long addressNumber, String postalCode, long storyLevel, String doorRingBellName) {
        this.city = city;
        this.address = address;
        this.addressNumber = addressNumber;
        this.postalCode = postalCode;
        this.storyLevel = storyLevel;
        this.doorRingBellName = doorRingBellName;
    }

    public Address() {
    }
}
