package dev.andreasgeorgatos.pointofservice.service.order;

import dev.andreasgeorgatos.pointofservice.model.item.Item;
import dev.andreasgeorgatos.pointofservice.model.item.OrderItem;
import dev.andreasgeorgatos.pointofservice.repository.item.ItemRepository;
import dev.andreasgeorgatos.pointofservice.repository.item.OrderItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository, ItemRepository itemRepository) {
        this.orderItemRepository = orderItemRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional

    public ResponseEntity<List<OrderItem>> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemRepository.findAll();

        if (orderItems.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(orderItems);
    }

    @Transactional
    public ResponseEntity<OrderItem> getOrderItemById(long id) {
        Optional<OrderItem> orderItem = orderItemRepository.findById(id);

        return orderItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<OrderItem> editOrderItemById(long id, OrderItem orderItem) {
        Optional<OrderItem> foundOrderItem = orderItemRepository.findById(id);

        if (foundOrderItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        OrderItem item = foundOrderItem.get();

        item.setItem(orderItem.getItem());
        item.setOrder(orderItem.getOrder());
        item.setQuantity(orderItem.getQuantity());
        item.setUpdated_at(LocalDate.now());

        orderItemRepository.save(item);

        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

   @Transactional
   public ResponseEntity<OrderItem> createOrderItem(OrderItem orderItem) {
       Optional<Item> foundItem = itemRepository.findById(orderItem.getItem().getId());

       if (foundItem.isEmpty()) {
           return ResponseEntity.notFound().build();
       }

       Item item = foundItem.get();
       orderItem.setItem(item);
       orderItem.setCreatedAt(LocalDate.now());
       orderItem.setUpdated_at(LocalDate.now());

       orderItemRepository.save(orderItem);

       return ResponseEntity.status(HttpStatus.CREATED).body(orderItem);
   }


    @Transactional
    public ResponseEntity<OrderItem> deleteOrderItemById(long id) {
        Optional<OrderItem> order = orderItemRepository.findById(id);

        if (order.isPresent()) {
            orderItemRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}