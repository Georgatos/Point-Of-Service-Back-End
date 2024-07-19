package dev.andreasgeorgatos.pointofservice.model.rewards;

import dev.andreasgeorgatos.pointofservice.enums.MembershipType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "cards")
public class MembershipCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @NotNull(message = "The discount type is a required field.")
    private MembershipType type;

    @JoinColumn(name = "referral_source")
    @OneToOne
    private ReferralSource referralSource;

    @Column(name = "percentage")
    @PositiveOrZero
    private double percentage;

    @Column(name = "can_be_refreshed")
    private boolean canBeRefreshed;

    @Column(name = "total_spent")
    @PositiveOrZero
    private double totalSpent;

    @Column(name = "referral_points")
    @PositiveOrZero
    private double referralPointsReward;

    @Column(name = "points_per_euro_spend")
    @PositiveOrZero
    private double pointsPerEuroSpend;

    @Column(name = "total_points")
    @PositiveOrZero
    private double totalPoints;

    @Column(name = "can_earn_points")
    private boolean canEarnPoints;

    @Column(name = "archived")
    private boolean isArchived;

    @Column(name = "active")
    private boolean isActive;

    @Column(name = "requires_account")
    private boolean requiresAccount;

    @Column(name = "issued_date", nullable = false)
    private LocalDate issuedDate;

    @Column(name = "last_used")
    private LocalDate lastUsed;

    @Column(name = "expiration_date", nullable = false)
    @NotNull(message = "The expiration date for the membership card is required.")
    private LocalDate expirationDate;
}
