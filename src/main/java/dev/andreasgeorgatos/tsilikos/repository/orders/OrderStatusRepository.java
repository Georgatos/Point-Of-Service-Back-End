package dev.andreasgeorgatos.tsilikos.repository.orders;

import dev.andreasgeorgatos.tsilikos.model.order.OrderStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatuses, Long> {
}
