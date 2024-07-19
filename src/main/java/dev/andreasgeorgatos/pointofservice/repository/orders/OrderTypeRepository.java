package dev.andreasgeorgatos.pointofservice.repository.orders;

import dev.andreasgeorgatos.pointofservice.model.order.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTypeRepository extends JpaRepository<OrderType, Long> {
}
