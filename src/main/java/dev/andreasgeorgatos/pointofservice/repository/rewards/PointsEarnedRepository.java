package dev.andreasgeorgatos.pointofservice.repository.rewards;

import dev.andreasgeorgatos.pointofservice.model.rewards.PointsEarned;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsEarnedRepository extends JpaRepository<PointsEarned, Long> {
}
