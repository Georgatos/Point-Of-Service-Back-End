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

    @Column(name = "points_per_euro")
    private BigDecimal pointsPerEuro;
}
