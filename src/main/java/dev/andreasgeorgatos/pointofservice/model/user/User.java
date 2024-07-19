package dev.andreasgeorgatos.pointofservice.model.user;

import dev.andreasgeorgatos.pointofservice.model.address.Address;
import dev.andreasgeorgatos.pointofservice.model.order.Order;
import dev.andreasgeorgatos.pointofservice.model.rewards.MembershipCard;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_type_id", nullable = false)
    @NotNull(message = "The user_type_id is a foreign key and it can't be null.")
    private UserType userType;

    @Column(name = "first_name", nullable = false, length = 20)
    @NotBlank(message = "The first name can't be null.")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 20)
    @NotBlank(message = "The last name can't be null.")
    private String lastName;

    @Column(name = "verification_token", nullable = false, length = 55)
    @NotBlank(message = "The verification token can't be null.")
    private String verificationToken;

    @Column(name = "verified", nullable = false)
    @NotNull(message = "The user can be either verified or not, not null.")
    private boolean verified;

    @Column(name = "password", nullable = false, length = 256)
    @Size(min = 6, message = "The password must have at least 6 characters, numbers or symbols.")
    @NotNull(message = "The password can't be null.")
    private String password;

    @Column(name = "email", nullable = false, length = 200, unique = true)
    @Email(message = "That's not a valid e-mail address, please make sure the syntax is correct.")
    @NotBlank(message = "The e-mail can't be left blank.")
    private String email;

    @Column(name = "phone_number", nullable = false, length = 10)
    @Size(min = 10, max = 10, message = "The phone number must have exactly 9 digits.")
    @NotBlank(message = "The phone number can't be left blank.")
    private String phoneNumber;


    @JoinColumn(name = "address_id")
    @NotNull(message = "The address_id is a foreign key and it can't be null.")
    @ManyToOne
    private Address address;

    @ManyToMany
    @JoinColumn(name = "order_id")
    private List<Order> orderList;

    @Column(name = "birthDate", nullable = false)
    @NotNull(message = "Please specify your age!")
    private LocalDate birthDate;

    @JoinColumn(name = "membership_card")
    @ManyToOne
    private MembershipCard membershipCard;
}
