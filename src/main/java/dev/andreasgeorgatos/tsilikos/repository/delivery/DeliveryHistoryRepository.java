package dev.andreasgeorgatos.tsilikos.repository.delivery;

import dev.andreasgeorgatos.tsilikos.model.delivery.DeliveryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistory, Long> {
}
