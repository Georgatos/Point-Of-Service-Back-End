package dev.andreasgeorgatos.tsilikos.model.rewards;

import dev.andreasgeorgatos.tsilikos.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Entity
@Data
@Table(name = "points-used-history")
public class PointsUsed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id")
    @OneToOne
    private User user;

    @JoinColumn(name = "membership_card", nullable = false)
    @OneToOne
    private MembershipCard membershipCard;

    @Column(name = "used")
    @PositiveOrZero
    private double used;

}
