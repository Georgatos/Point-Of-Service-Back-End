package dev.andreasgeorgatos.pointofservice.repository.rewards;

import dev.andreasgeorgatos.pointofservice.model.rewards.PointsUsed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsUsedRepository extends JpaRepository<PointsUsed, Long> {
}
