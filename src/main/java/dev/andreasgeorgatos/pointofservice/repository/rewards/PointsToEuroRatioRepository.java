package dev.andreasgeorgatos.pointofservice.repository.rewards;

import dev.andreasgeorgatos.pointofservice.model.rewards.PointsToEuroRatio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsToEuroRatioRepository extends JpaRepository<PointsToEuroRatio, Long> {
}
