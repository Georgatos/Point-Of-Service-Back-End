package dev.andreasgeorgatos.tsilikos.repository.delivery;

import dev.andreasgeorgatos.tsilikos.model.delivery.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryStatusRepository extends JpaRepository<DeliveryStatus, Long> {
}
