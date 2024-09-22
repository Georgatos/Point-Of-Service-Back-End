package dev.andreasgeorgatos.pointofservice.service.delivery;

import dev.andreasgeorgatos.pointofservice.model.delivery.DeliveryStatus;
import dev.andreasgeorgatos.pointofservice.model.order.Order;
import dev.andreasgeorgatos.pointofservice.model.order.OrderStatuses;
import dev.andreasgeorgatos.pointofservice.repository.delivery.DeliveryStatusRepository;
import dev.andreasgeorgatos.pointofservice.repository.orders.OrderRepository;
import dev.andreasgeorgatos.pointofservice.repository.orders.OrderStatusRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryStatusService {

    private final DeliveryStatusRepository deliveryStatusRepository;
    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;

    @Autowired
    public DeliveryStatusService(DeliveryStatusRepository deliveryStatusRepository, OrderRepository orderRepository, OrderStatusRepository orderStatusRepository) {
        this.deliveryStatusRepository = deliveryStatusRepository;
        this.orderRepository = orderRepository;
        this.orderStatusRepository = orderStatusRepository;
    }

    public ResponseEntity<List<DeliveryStatus>> getAllDeliveryStatuses() {
        List<DeliveryStatus> deliveryStatuses = deliveryStatusRepository.findAll();

        if (!deliveryStatuses.isEmpty()) {
            return ResponseEntity.ok(deliveryStatuses);
        }
        return ResponseEntity.notFound().build();
    }


    public ResponseEntity<DeliveryStatus> getDeliveryStatusById(long id) {
        Optional<DeliveryStatus> deliveryHistory = deliveryStatusRepository.findById(id);

        return deliveryHistory.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<DeliveryStatus> createDeliveryStatus(DeliveryStatus deliveryStatus) {
        Optional<Order> optionalOrder = orderRepository.findById(deliveryStatus.getOrderId().getId());
        Optional<OrderStatuses> optionalOrderStatus = orderStatusRepository.findById(deliveryStatus.getOrderStatusId().getId());

        if (optionalOrder.isEmpty() || optionalOrderStatus.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        deliveryStatus.setOrderId(optionalOrder.get());
        deliveryStatus.setOrderStatusId(optionalOrderStatus.get());

        return ResponseEntity.ok(deliveryStatusRepository.save(deliveryStatus));
    }

    @Transactional
    public ResponseEntity<DeliveryStatus> editDeliveryStatusById(Long id, DeliveryStatus deliveryStatus) {
        Optional<DeliveryStatus> optionalDeliveryStatus = deliveryStatusRepository.findById(id);
        Optional<Order> optionalOrder = orderRepository.findById(deliveryStatus.getOrderId().getId());
        Optional<OrderStatuses> optionalOrderStatus = orderStatusRepository.findById(deliveryStatus.getOrderStatusId().getId());

        if (optionalDeliveryStatus.isPresent()) {
            DeliveryStatus oldDeliveryStatus = optionalDeliveryStatus.get();

            if (optionalOrder.isEmpty() || optionalOrderStatus.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            oldDeliveryStatus.setDeliveryDate(deliveryStatus.getDeliveryDate());
            oldDeliveryStatus.setOrderStatusId(optionalOrderStatus.get());
            oldDeliveryStatus.setOrderId(optionalOrder.get());

            DeliveryStatus savedDeliveryHistory = deliveryStatusRepository.save(oldDeliveryStatus);

            return ResponseEntity.ok(savedDeliveryHistory);
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<Order> deleteDeliveryStatus(Long id) {
        Optional<DeliveryStatus> deliveryHistory = deliveryStatusRepository.findById(id);

        if (deliveryHistory.isPresent()) {
            deliveryStatusRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
