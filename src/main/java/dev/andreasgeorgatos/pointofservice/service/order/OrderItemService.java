package dev.andreasgeorgatos.pointofservice.service.order;

import dev.andreasgeorgatos.pointofservice.model.item.Item;
import dev.andreasgeorgatos.pointofservice.model.item.OrderItem;
import dev.andreasgeorgatos.pointofservice.repository.item.ItemRepository;
import dev.andreasgeorgatos.pointofservice.repository.item.OrderItemRepository;
import org.springframework.transaction.annotation.Transactional; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class that handles business logic for order items.
 * It provides functionalities to create, retrieve, update, and delete order items.
 */
@Service
public class OrderItemService {
    private static final Logger logger = LoggerFactory.getLogger(OrderItemService.class);

    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * Constructs an {@code OrderItemService} with the specified repositories.
     *
     * @param orderItemRepository The repository for order item data access.
     * @param itemRepository The repository for item data access, used to validate items when creating order items.
     */
    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository, ItemRepository itemRepository) {
        this.orderItemRepository = orderItemRepository;
        this.itemRepository = itemRepository;
    }

    /**
     * Retrieves all order items. This operation is read-only.
     *
     * @return A {@link ResponseEntity} containing a list of all {@link OrderItem} objects and HTTP status OK,
     *         or HTTP status NOT_FOUND if no order items exist.
     */
    @Transactional(readOnly = true) 
    public ResponseEntity<List<OrderItem>> getAllOrderItems() {
        logger.info("Fetching all order items");
        List<OrderItem> orderItems = orderItemRepository.findAll();

        if (orderItems.isEmpty()) {
            logger.info("No order items found");
            return ResponseEntity.notFound().build();
        }
        logger.debug("Found {} order items", orderItems.size());
        return ResponseEntity.ok(orderItems);
    }

    /**
     * Retrieves a specific order item by its ID. This operation is read-only.
     *
     * @param id The ID of the order item to retrieve.
     * @return A {@link ResponseEntity} containing the {@link OrderItem} if found and HTTP status OK,
     *         or HTTP status NOT_FOUND if the order item does not exist.
     */
    @Transactional(readOnly = true) 
    public ResponseEntity<OrderItem> getOrderItemById(long id) {
        logger.info("Fetching order item with ID: {}", id);
        Optional<OrderItem> orderItem = orderItemRepository.findById(id);

        if (orderItem.isPresent()) {
            logger.debug("Found order item: {}", orderItem.get());
            return ResponseEntity.ok(orderItem.get());
        }
        logger.warn("Order item with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }

    /**
     * Updates an existing order item by its ID.
     * The {@code updated_at} field is set to the current date.
     *
     * @param id The ID of the order item to edit.
     * @param orderItemDetails The {@link OrderItem} object containing the new details for the order item.
     * @return A {@link ResponseEntity} containing the updated {@link OrderItem} and HTTP status OK if successful,
     *         or HTTP status NOT_FOUND if the order item with the given ID does not exist.
     */
    @Transactional
    public ResponseEntity<OrderItem> editOrderItemById(long id, OrderItem orderItemDetails) { 
        logger.info("Editing order item with ID: {}, new details: {}", id, orderItemDetails);
        Optional<OrderItem> foundOrderItem = orderItemRepository.findById(id);

        if (foundOrderItem.isEmpty()) {
            logger.warn("Failed to edit. Order item with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }

        OrderItem item = foundOrderItem.get();

        item.setItem(orderItemDetails.getItem());
        item.setOrder(orderItemDetails.getOrder());
        item.setQuantity(orderItemDetails.getQuantity());
        item.setUpdated_at(LocalDate.now());

        OrderItem savedItem = orderItemRepository.save(item); 
        logger.info("Order item with ID: {} updated successfully", savedItem.getId());
        return ResponseEntity.ok(savedItem); 
    }

   /**
    * Creates a new order item.
    * Validates if the associated {@link Item} exists.
    * Sets the {@code createdAt} and {@code updated_at} fields to the current date.
    *
    * @param orderItem The {@link OrderItem} object to create.
    * @return A {@link ResponseEntity} containing the created {@link OrderItem} and HTTP status CREATED if successful,
    *         HTTP status BAD_REQUEST if the item in {@code orderItem} is null or has a null ID,
    *         or HTTP status NOT_FOUND if the associated item does not exist in the database.
    */
   @Transactional
   public ResponseEntity<OrderItem> createOrderItem(OrderItem orderItem) {
       logger.info("Creating new order item: {}", orderItem);
       if (orderItem.getItem() == null || orderItem.getItem().getId() == null) {
           logger.warn("Attempted to create order item with null item or item ID.");
           return ResponseEntity.badRequest().build(); 
       }
       
       Optional<Item> foundItem = itemRepository.findById(orderItem.getItem().getId());

       if (foundItem.isEmpty()) {
           logger.warn("Failed to create order item. Item with ID: {} not found", orderItem.getItem().getId());
           return ResponseEntity.notFound().build();
       }

       Item item = foundItem.get();
       orderItem.setItem(item);
       orderItem.setCreatedAt(LocalDate.now());
       orderItem.setUpdated_at(LocalDate.now());

       OrderItem savedOrderItem = orderItemRepository.save(orderItem); 
       logger.info("Order item created successfully with ID: {}", savedOrderItem.getId());
       return ResponseEntity.status(HttpStatus.CREATED).body(savedOrderItem); 
   }


    /**
     * Deletes an order item by its ID.
     *
     * @param id The ID of the order item to delete.
     * @return A {@link ResponseEntity} with HTTP status NO_CONTENT if deletion is successful,
     *         or HTTP status NOT_FOUND if the order item with the given ID does not exist.
     */
    @Transactional
    public ResponseEntity<Void> deleteOrderItemById(long id) { 
        logger.info("Deleting order item with ID: {}", id);
        Optional<OrderItem> order = orderItemRepository.findById(id);

        if (order.isPresent()) {
            orderItemRepository.deleteById(id);
            logger.info("Order item with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        }
        logger.warn("Failed to delete. Order item with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }

}