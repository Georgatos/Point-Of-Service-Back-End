package dev.andreasgeorgatos.pointofservice.repository.delivery;

import dev.andreasgeorgatos.pointofservice.model.delivery.DeliveryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistory, Long> {
}
