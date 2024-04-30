package dev.andreasgeorgatos.tsilikos.repository.rewards;

import dev.andreasgeorgatos.tsilikos.model.rewards.PointsUsed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsUsedRepository extends JpaRepository<PointsUsed, Long> {
}
