package dev.andreasgeorgatos.pointofservice.service.order;

import dev.andreasgeorgatos.pointofservice.model.order.OrderType;
import dev.andreasgeorgatos.pointofservice.repository.orders.OrderTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderTypeService {

    private final OrderTypeRepository orderTypeRepository;

    @Autowired
    public OrderTypeService(OrderTypeRepository orderTypeRepository) {
        this.orderTypeRepository = orderTypeRepository;
    }


    public ResponseEntity<List<OrderType>> getAllOrderTypes() {
        List<OrderType> orderTypes = orderTypeRepository.findAll();

        if (orderTypes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(orderTypes);
    }

    public ResponseEntity<OrderType> getOrderTypeById(long id) {
        Optional<OrderType> orderType = orderTypeRepository.findById(id);

        return orderType.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<OrderType> editOrderTypeById(long id, OrderType orderType) {
        Optional<OrderType> foundOrderType = orderTypeRepository.findById(id);

        if (foundOrderType.isPresent()) {
            OrderType oldOrderType = foundOrderType.get();

            oldOrderType.setType(orderType.getType());

            OrderType savedOrderType = orderTypeRepository.save(oldOrderType);
            return ResponseEntity.ok(savedOrderType);
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<OrderType> createOrderType(OrderType order) {
        OrderType savedOrderType = orderTypeRepository.save(order);
        return ResponseEntity.ok(savedOrderType);
    }


    @Transactional
    public ResponseEntity<OrderType> deleteOrderTypeById(long id) {
        Optional<OrderType> orderType = orderTypeRepository.findById(id);

        if (orderType.isPresent()) {
            orderTypeRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
