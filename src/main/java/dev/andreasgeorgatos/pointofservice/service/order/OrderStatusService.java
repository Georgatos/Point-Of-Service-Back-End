package dev.andreasgeorgatos.pointofservice.service.order;

import dev.andreasgeorgatos.pointofservice.model.order.OrderStatuses;
import dev.andreasgeorgatos.pointofservice.repository.orders.OrderStatusRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderStatusService {

    private final OrderStatusRepository orderStatusRepository;

    @Autowired
    public OrderStatusService(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }

    public ResponseEntity<List<OrderStatuses>> getAllOrderStatutes() {
        List<OrderStatuses> statuses = orderStatusRepository.findAll();

        if (statuses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(statuses);
    }

    public ResponseEntity<OrderStatuses> getOrderStatusById(long id) {
        Optional<OrderStatuses> OrderStatus = orderStatusRepository.findById(id);

        return OrderStatus.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<OrderStatuses> createOrderStatus(OrderStatuses orderStatus) {
        OrderStatuses savedOrder = orderStatusRepository.save(orderStatus);
        return ResponseEntity.ok(savedOrder);
    }

    @Transactional
    public ResponseEntity<OrderStatuses> editOrderStatusById(long id, OrderStatuses orderStatus) {
        Optional<OrderStatuses> foundOrderStatus = orderStatusRepository.findById(id);

        if (foundOrderStatus.isPresent()) {
            OrderStatuses oldOrderStatus = foundOrderStatus.get();

            oldOrderStatus.setStatus(orderStatus.getStatus());
            OrderStatuses savedOrderStatus = orderStatusRepository.save(oldOrderStatus);

            return ResponseEntity.ok(savedOrderStatus);
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<OrderStatuses> deleteOrderStatusById(long id) {
        Optional<OrderStatuses> orderStatus = orderStatusRepository.findById(id);

        if (orderStatus.isPresent()) {
            orderStatusRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
