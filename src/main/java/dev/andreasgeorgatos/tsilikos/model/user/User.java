package dev.andreasgeorgatos.tsilikos.model.user;

import dev.andreasgeorgatos.tsilikos.model.address.Address;
import dev.andreasgeorgatos.tsilikos.model.order.Order;
import dev.andreasgeorgatos.tsilikos.model.rewards.MembershipCard;
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
    @NotNull(message = "You can't have a blank foreign key.")
    private UserType userType;

    @Column(name = "first_name", nullable = false, length = 20)
    @NotBlank(message = "Please add your first name.")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 20)
    @NotBlank(message = "Please add your last name.")
    private String lastName;

    @Column(name = "password", nullable = false, length = 256)
    @Size(min = 6, message = "The password must have at least 6 characters, numbers or symbols.")
    @NotNull(message = "Please add a password.")
    private String password;

    @Column(name = "email", nullable = false, length = 200, unique = true)
    @Email(message = "That's not a valid e-mail address, please make sure the syntax is correct.")
    @NotBlank(message = "Please fill your e-mail.")
    private String email;

    @Column(name = "phone_number", nullable = false, length = 10)
    @Size(min = 10, max = 10, message = "The phone number must have exactly 9 digits.")
    @NotBlank(message = "Please fill your phone number.")
    private String phoneNumber;

    @ManyToMany
    @JoinColumn(name = "address_id")
    @NotNull(message = "You can't have a blank foreign key.")
    private List<Address> address;

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
