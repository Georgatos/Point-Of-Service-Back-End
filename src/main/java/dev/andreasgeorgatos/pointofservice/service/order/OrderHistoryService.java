package dev.andreasgeorgatos.pointofservice.service.order;


import dev.andreasgeorgatos.pointofservice.model.order.OrderHistory;
import dev.andreasgeorgatos.pointofservice.repository.orders.OrderHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderHistoryService {

    private final OrderHistoryRepository orderHistoryRepository;

    @Autowired
    public OrderHistoryService(OrderHistoryRepository orderHistoryRepository) {
        this.orderHistoryRepository = orderHistoryRepository;
    }

    public ResponseEntity<List<OrderHistory>> getAllOrdersHistory() {
        List<OrderHistory> addresses = orderHistoryRepository.findAll();

        if (!addresses.isEmpty()) {
            return ResponseEntity.ok(addresses);
        }
        return ResponseEntity.notFound().build();
    }


    public ResponseEntity<OrderHistory> getOrderHistoryById(long id) {
        Optional<OrderHistory> address = orderHistoryRepository.findById(id);

        return address.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<OrderHistory> createOrderHistory(OrderHistory orderHistory) {
        return ResponseEntity.ok(orderHistoryRepository.save(orderHistory));
    }

    @Transactional
    public ResponseEntity<OrderHistory> editOrderHistoryById(long id, OrderHistory orderHistory) {
        Optional<OrderHistory> optionalOrderHistory = orderHistoryRepository.findById(id);

        if (optionalOrderHistory.isPresent()) {
            OrderHistory oldOrderHistory = optionalOrderHistory.get();

            oldOrderHistory.setOrder(orderHistory.getOrder());
            oldOrderHistory.setOrderStatus(orderHistory.getOrderStatus());

            OrderHistory savedOrderHistory = orderHistoryRepository.save(oldOrderHistory);

            return ResponseEntity.ok(savedOrderHistory);
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<OrderHistory> deleteOrderHistoryById(long id) {
        Optional<OrderHistory> orderHistory = orderHistoryRepository.findById(id);

        if (orderHistory.isPresent()) {
            orderHistoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
