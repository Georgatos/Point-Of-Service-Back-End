package dev.andreasgeorgatos.tsilikos.repository.orders;

import dev.andreasgeorgatos.tsilikos.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
