package dev.andreasgeorgatos.pointofservice.repository.rewards;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsEarnedRepository extends JpaRepository<PointsEarned, Long> {
}
