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

    @Column(name = "Country", nullable = false)
    @NotBlank(message = "You must specify your country.")
    private String country;

    @Column(name = "city", nullable = false)
    @NotBlank(message = "You must specify your city address.")
    private String city;

    @Column(name = "street", nullable = false)
    @NotBlank(message = "You must specify a street.")
    private String street;

    @Column(name = "number", nullable = false)
    @PositiveOrZero(message = "The street address number must be a positive number.")
    private String number;

    @Column(name = "postal_code", nullable = false)
    @NotBlank(message = "You must specify your area postal code.")
    private String postalCode;

    @Column(name = "story_level", nullable = false)
    @PositiveOrZero(message = "Your story level must be positive.")
    private long storyLevel;

    @Column(name = "door_bell_ring_name", nullable = false, length = 50)
    @NotBlank(message = "You must specify the name of your door ring bell.")
    private String doorRingBellName;

    public Address(String country, String city, String street, String number, String postalCode, long storyLevel, String doorRingBellName) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.number = number;
        this.postalCode = postalCode;
        this.storyLevel = storyLevel;
        this.doorRingBellName = doorRingBellName;
    }

    public Address() {
    }
}
