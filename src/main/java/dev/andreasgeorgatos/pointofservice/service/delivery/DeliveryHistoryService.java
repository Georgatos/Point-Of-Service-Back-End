package dev.andreasgeorgatos.pointofservice.service.delivery;

import dev.andreasgeorgatos.pointofservice.model.delivery.DeliveryHistory;
import dev.andreasgeorgatos.pointofservice.model.delivery.DeliveryStatus;
import dev.andreasgeorgatos.pointofservice.model.order.Order;
import dev.andreasgeorgatos.pointofservice.model.order.OrderStatuses;
import dev.andreasgeorgatos.pointofservice.repository.delivery.DeliveryHistoryRepository;
import dev.andreasgeorgatos.pointofservice.repository.delivery.DeliveryStatusRepository;
import dev.andreasgeorgatos.pointofservice.repository.orders.OrderRepository;
import dev.andreasgeorgatos.pointofservice.repository.orders.OrderStatusRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryHistoryService {

    private final DeliveryHistoryRepository deliveryHistoryRepository;
    private final OrderRepository orderRepository;
    private final DeliveryStatusRepository deliveryStatusRepository;
    private final OrderStatusRepository orderStatusRepository;

    @Autowired
    public DeliveryHistoryService(DeliveryHistoryRepository deliveryHistoryRepository, OrderRepository orderRepository, DeliveryStatusRepository deliveryStatusRepository, OrderStatusRepository orderStatusRepository) {
        this.deliveryHistoryRepository = deliveryHistoryRepository;
        this.orderRepository = orderRepository;
        this.deliveryStatusRepository = deliveryStatusRepository;
        this.orderStatusRepository = orderStatusRepository;
    }

    public ResponseEntity<List<DeliveryHistory>> getAllDeliveryHistories() {
        List<DeliveryHistory> deliveryHistories = deliveryHistoryRepository.findAll();

        if (!deliveryHistories.isEmpty()) {
            return ResponseEntity.ok(deliveryHistories);
        }
        return ResponseEntity.notFound().build();
    }


    public ResponseEntity<DeliveryHistory> getDeliveryHistoryById(long id) {
        Optional<DeliveryHistory> deliveryHistory = deliveryHistoryRepository.findById(id);

        return deliveryHistory.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<DeliveryHistory> createDeliveryHistory(DeliveryHistory deliveryHistory) {
        Optional<OrderStatuses> optionalOrderStatus = orderStatusRepository.findById(deliveryHistory.getOrderStatus().getId());
        Optional<DeliveryStatus> optionalDeliveryStatus = deliveryStatusRepository.findById(deliveryHistory.getDeliveryStatus().getId());

        if (optionalOrderStatus.isEmpty() || optionalDeliveryStatus.isEmpty()) {

            return ResponseEntity.notFound().build();
        }

        List<Order> orders = new ArrayList<>();

        for (Order order : deliveryHistory.getOrders()) {
            Optional<Order> optionalOrder = orderRepository.findById(order.getId());

            if (optionalOrder.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            orders.add(optionalOrder.get());
        }

        deliveryHistory.setOrderStatus(optionalOrderStatus.get());
        deliveryHistory.setDeliveryStatus(optionalDeliveryStatus.get());
        deliveryHistory.setOrders(orders);

        return ResponseEntity.ok(deliveryHistoryRepository.save(deliveryHistory));
    }

    @Transactional
    public ResponseEntity<DeliveryHistory> editDeliveryHistoryById(long id, DeliveryHistory deliveryHistory) {
        Optional<DeliveryHistory> optionalDeliveryHistory = deliveryHistoryRepository.findById(id);

        if (optionalDeliveryHistory.isEmpty()) {
            return ResponseEntity.notFound().build();

        }

        DeliveryHistory foundDeliveryHistory = optionalDeliveryHistory.get();
        foundDeliveryHistory.setDeliveryStatus(deliveryHistory.getDeliveryStatus());

        List<Order> orders = foundDeliveryHistory.getOrders();

        for (Order order : deliveryHistory.getOrders()) {
            Optional<Order> optionalOrder = orderRepository.findById(order.getId());

            if (optionalOrder.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            orders.add(optionalOrder.get());
        }
        Optional<OrderStatuses> optionalOrderStatus = orderStatusRepository.findById(deliveryHistory.getOrderStatus().getId());
        Optional<DeliveryStatus> optionalDeliveryStatus = deliveryStatusRepository.findById(deliveryHistory.getDeliveryStatus().getId());


        if (optionalOrderStatus.isEmpty() || optionalDeliveryStatus.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        foundDeliveryHistory.setOrderStatus(optionalOrderStatus.get());
        foundDeliveryHistory.setDeliveryStatus(optionalDeliveryStatus.get());


        DeliveryHistory savedDeliveryHistory = deliveryHistoryRepository.save(foundDeliveryHistory);

        return ResponseEntity.ok(savedDeliveryHistory);
    }

    @Transactional
    public ResponseEntity<Order> deleteDeliveryHistory(long id) {
        Optional<DeliveryHistory> deliveryHistory = deliveryHistoryRepository.findById(id);

        if (deliveryHistory.isPresent()) {
            deliveryHistoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
