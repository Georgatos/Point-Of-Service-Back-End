package dev.andreasgeorgatos.pointofservice.model.rewards;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "points_to_euro_ratio")
public class PointsToEuroRatio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "real_life_money")
    private BigDecimal realLifeMoney;

    @Column(name = "points_worth")
    private BigDecimal pointsWorth;
}
