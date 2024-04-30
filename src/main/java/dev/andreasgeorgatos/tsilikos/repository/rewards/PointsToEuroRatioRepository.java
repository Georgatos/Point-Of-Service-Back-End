package dev.andreasgeorgatos.tsilikos.repository.rewards;

import dev.andreasgeorgatos.tsilikos.model.rewards.PointsToEuroRatio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsToEuroRatioRepository extends JpaRepository<PointsToEuroRatio, Long> {
}
