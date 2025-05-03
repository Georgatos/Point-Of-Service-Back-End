package dev.andreasgeorgatos.pointofservice.service.order;

import dev.andreasgeorgatos.pointofservice.model.item.Item;
import dev.andreasgeorgatos.pointofservice.model.order.Order;
import dev.andreasgeorgatos.pointofservice.model.order.OrderStatuses;
import dev.andreasgeorgatos.pointofservice.model.order.OrderType;
import dev.andreasgeorgatos.pointofservice.repository.item.ItemRepository;
import dev.andreasgeorgatos.pointofservice.repository.orders.OrderRepository;
import dev.andreasgeorgatos.pointofservice.repository.orders.OrderStatusRepository;
import dev.andreasgeorgatos.pointofservice.repository.orders.OrderTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTypeRepository orderTypeRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderTypeRepository orderTypeRepository, OrderStatusRepository orderStatusRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.orderTypeRepository = orderTypeRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional

    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        if (orders.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(orders);
    }

    @Transactional
    public ResponseEntity<Order> getOrderById(long id) {
        Optional<Order> order = orderRepository.findById(id);

        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<Order> editOrderById(long id, Order order) {
        Optional<Order> foundOrder = orderRepository.findById(id);

        if (foundOrder.isPresent()) {
            Order oldOrder = foundOrder.get();

            oldOrder.setOrderTotal(order.getOrderTotal());
            oldOrder.setOrderStatusId(order.getOrderStatusId());
            oldOrder.setOrderTypeId(order.getOrderTypeId());
            oldOrder.setItems(order.getItems());
            oldOrder.setOrderDate(order.getOrderDate());

            Order savedOrder = orderRepository.save(oldOrder);
            return ResponseEntity.ok(savedOrder);
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<Order> createOrder(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        
        Optional<OrderType> orderType = orderTypeRepository.findById(order.getOrderTypeId().getId());
        Optional<OrderStatuses> orderStatus = orderStatusRepository.findById(order.getOrderStatusId().getId());

        if (orderType.isEmpty() || orderStatus.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Item> items = new ArrayList<>();
        double orderTotal = 0;

        for (Item item : order.getItems()) {
            Optional<Item> optionalItem = itemRepository.findById(item.getId());
            if (optionalItem.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            items.add(optionalItem.get());
            orderTotal += optionalItem.get().getItemPrice();
        }

        order.setOrderTypeId(orderType.get());
        order.setOrderStatusId(orderStatus.get());
        order.setItems(items);
        order.setOrderTotal(orderTotal);

        orderRepository.save(order);
        return ResponseEntity.ok(order);
    }


    @Transactional
    public ResponseEntity<Order> deleteOrderById(long id) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            orderRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}