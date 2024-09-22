package dev.andreasgeorgatos.pointofservice.repository.rewards;

import dev.andreasgeorgatos.pointofservice.model.rewards.PointsTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsTransactionRepository extends JpaRepository<PointsTransaction, Long> {
}
