package dev.andreasgeorgatos.pointofservice.repository.orders;

import dev.andreasgeorgatos.pointofservice.model.order.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
}
