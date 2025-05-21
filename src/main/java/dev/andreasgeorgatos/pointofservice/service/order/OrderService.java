package dev.andreasgeorgatos.pointofservice.service.order;

import dev.andreasgeorgatos.pointofservice.model.item.Item;
import dev.andreasgeorgatos.pointofservice.model.order.Order;
import dev.andreasgeorgatos.pointofservice.model.order.OrderStatuses;
import dev.andreasgeorgatos.pointofservice.model.order.OrderType;
import dev.andreasgeorgatos.pointofservice.repository.item.ItemRepository;
import dev.andreasgeorgatos.pointofservice.repository.orders.OrderRepository;
import dev.andreasgeorgatos.pointofservice.repository.orders.OrderStatusRepository;
import dev.andreasgeorgatos.pointofservice.repository.orders.OrderTypeRepository;
import org.springframework.transaction.annotation.Transactional; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
// import java.time.LocalDate; // Not explicitly used, but good for date handling if needed in future

/**
 * {@code OrderService} handles business logic for orders, including creation, retrieval, updates, and deletion.
 * It interacts with various repositories to manage order data and related entities like items, order types, and statuses.
 */
@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderTypeRepository orderTypeRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final ItemRepository itemRepository;

    /**
     * Constructs an {@code OrderService} with the specified repositories.
     *
     * @param orderRepository Repository for order data access.
     * @param orderTypeRepository Repository for order type data access.
     * @param orderStatusRepository Repository for order status data access.
     * @param itemRepository Repository for item data access, used for validating items in orders.
     */
    @Autowired
    public OrderService(OrderRepository orderRepository, OrderTypeRepository orderTypeRepository, OrderStatusRepository orderStatusRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.orderTypeRepository = orderTypeRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.itemRepository = itemRepository;
    }

    /**
     * Retrieves all orders. This operation is read-only.
     *
     * @return A {@link ResponseEntity} containing a list of all {@link Order} objects and HTTP status OK,
     *         or HTTP status NOT_FOUND if no orders exist.
     */
    @Transactional(readOnly = true) 
    public ResponseEntity<List<Order>> getAllOrders() {
        logger.info("Fetching all orders");
        List<Order> orders = orderRepository.findAll();

        if (orders.isEmpty()) {
            logger.info("No orders found");
            return ResponseEntity.notFound().build();
        }
        logger.debug("Found {} orders", orders.size());
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves a specific order by its ID. This operation is read-only.
     *
     * @param id The ID of the order to retrieve.
     * @return A {@link ResponseEntity} containing the {@link Order} if found and HTTP status OK,
     *         or HTTP status NOT_FOUND if the order does not exist.
     */
    @Transactional(readOnly = true) 
    public ResponseEntity<Order> getOrderById(long id) {
        logger.info("Fetching order with ID: {}", id);
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            logger.debug("Found order: {}", order.get());
            return ResponseEntity.ok(order.get());
        }
        logger.warn("Order with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }

    /**
     * Updates an existing order by its ID.
     * The method validates the existence of the order and the provided {@code OrderType} and {@code OrderStatus} IDs.
     * Note: For robust updates, item validation (e.g., stock availability) and recalculation of {@code orderTotal}
     * should occur if the items list is modified. The current implementation directly sets items and total from the input.
     * The {@code orderDate} is also directly set from input; typically, this is set upon creation and not updated.
     *
     * @param id The ID of the order to edit.
     * @param orderDetails The {@link Order} object containing the new details for the order.
     * @return A {@link ResponseEntity} containing the updated {@link Order} and HTTP status OK if successful.
     *         Returns HTTP status NOT_FOUND if the order with the given ID does not exist.
     *         Returns HTTP status BAD_REQUEST if essential linked entities (OrderType, OrderStatus) are missing or invalid in the input.
     */
    @Transactional
    public ResponseEntity<?> editOrderById(long id, Order orderDetails) { 
        logger.info("Editing order with ID: {}, new details: {}", id, orderDetails);
        Optional<Order> foundOrder = orderRepository.findById(id);

        if (foundOrder.isEmpty()) {
            logger.warn("Failed to edit. Order with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }

        if (orderDetails.getOrderTypeId() == null || orderDetails.getOrderTypeId().getId() == null) {
            logger.warn("Attempted to edit order with ID: {} with null OrderType ID.", id);
            return ResponseEntity.badRequest().body("OrderType ID must be provided for editing an order.");
        }
        if (orderDetails.getOrderStatusId() == null || orderDetails.getOrderStatusId().getId() == null) {
            logger.warn("Attempted to edit order with ID: {} with null OrderStatus ID.", id);
            return ResponseEntity.badRequest().body("OrderStatus ID must be provided for editing an order.");
        }
        
        Optional<OrderType> orderTypeOpt = orderTypeRepository.findById(orderDetails.getOrderTypeId().getId());
        if (orderTypeOpt.isEmpty()) {
            logger.warn("Invalid OrderType ID: {} provided for order ID: {}", orderDetails.getOrderTypeId().getId(), id);
            return ResponseEntity.badRequest().body("Invalid OrderType ID provided.");
        }
        Optional<OrderStatuses> orderStatusOpt = orderStatusRepository.findById(orderDetails.getOrderStatusId().getId());
        if (orderStatusOpt.isEmpty()) {
            logger.warn("Invalid OrderStatus ID: {} provided for order ID: {}", orderDetails.getOrderStatusId().getId(), id);
            return ResponseEntity.badRequest().body("Invalid OrderStatus ID provided.");
        }

        Order oldOrder = foundOrder.get();

        oldOrder.setOrderTotal(orderDetails.getOrderTotal()); 
        oldOrder.setOrderStatusId(orderStatusOpt.get());
        oldOrder.setOrderTypeId(orderTypeOpt.get());
        oldOrder.setItems(orderDetails.getItems()); 
        oldOrder.setOrderDate(orderDetails.getOrderDate()); 

        Order savedOrder = orderRepository.save(oldOrder);
        logger.info("Order with ID: {} updated successfully", savedOrder.getId());
        return ResponseEntity.ok(savedOrder);
    }

    /**
     * Creates a new order.
     * This method requires {@code OrderType}, {@code OrderStatus}, and at least one item to be specified in the input {@code order}.
     * It validates the existence of these entities and calculates the {@code orderTotal} based on the prices of the items.
     * The {@code orderDate} should ideally be set before calling this method or defaulted here (e.g., to current date).
     *
     * @param order The {@link Order} object to create. Must include valid IDs for {@code OrderType}, {@code OrderStatus}, and a non-empty list of items with valid IDs.
     * @return A {@link ResponseEntity} containing the created {@link Order} with HTTP status CREATED if successful.
     *         Returns HTTP status BAD_REQUEST if required fields (items, OrderType ID, OrderStatus ID) are missing or invalid,
     *         or if any specified item is not found.
     */
    @Transactional
    public ResponseEntity<?> createOrder(Order order) { 
        logger.info("Creating new order: {}", order);
        if (order.getItems() == null || order.getItems().isEmpty()) {
            logger.warn("Attempted to create order with no items.");
            return ResponseEntity.badRequest().body("Order must contain at least one item.");
        }
        if (order.getOrderTypeId() == null || order.getOrderTypeId().getId() == null) {
            logger.warn("Attempted to create order with null OrderType ID.");
            return ResponseEntity.badRequest().body("OrderType must be provided.");
        }
        if (order.getOrderStatusId() == null || order.getOrderStatusId().getId() == null) {
            logger.warn("Attempted to create order with null OrderStatus ID.");
            return ResponseEntity.badRequest().body("OrderStatus must be provided.");
        }
        
        Optional<OrderType> orderTypeOpt = orderTypeRepository.findById(order.getOrderTypeId().getId());
        Optional<OrderStatuses> orderStatusOpt = orderStatusRepository.findById(order.getOrderStatusId().getId());

        if (orderTypeOpt.isEmpty()) {
            logger.warn("Invalid OrderType ID: {} provided for new order.", order.getOrderTypeId().getId());
            return ResponseEntity.badRequest().body("Invalid OrderType ID: " + order.getOrderTypeId().getId());
        }
        if (orderStatusOpt.isEmpty()) {
            logger.warn("Invalid OrderStatus ID: {} provided for new order.", order.getOrderStatusId().getId());
            return ResponseEntity.badRequest().body("Invalid OrderStatus ID: " + order.getOrderStatusId().getId());
        }

        List<Item> items = new ArrayList<>();
        double orderTotal = 0;

        for (Item item : order.getItems()) {
            if (item == null || item.getId() == null) {
                 logger.warn("Order creation attempt with a null item or item with null ID in the item list.");
                 return ResponseEntity.badRequest().body("Order items cannot be null and must have an ID.");
            }
            Optional<Item> optionalItem = itemRepository.findById(item.getId());
            if (optionalItem.isEmpty()) {
                logger.warn("Item with ID {} not found during order creation.", item.getId());
                return ResponseEntity.badRequest().body("Item with ID " + item.getId() + " not found.");
            }
            items.add(optionalItem.get());
            orderTotal += optionalItem.get().getItemPrice(); 
        }

        order.setOrderTypeId(orderTypeOpt.get());
        order.setOrderStatusId(orderStatusOpt.get());
        order.setItems(items); 
        order.setOrderTotal(orderTotal);
        // if (order.getOrderDate() == null) order.setOrderDate(LocalDate.now()); // Ensure orderDate is set

        Order savedOrder = orderRepository.save(order);
        logger.info("Order created successfully with ID: {}", savedOrder.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder); 
    }


    /**
     * Deletes an order by its ID.
     *
     * @param id The ID of the order to delete.
     * @return A {@link ResponseEntity} with HTTP status NO_CONTENT if deletion is successful,
     *         or HTTP status NOT_FOUND if the order with the given ID does not exist.
     */
    @Transactional
    public ResponseEntity<Void> deleteOrderById(long id) { 
        logger.info("Deleting order with ID: {}", id);
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            orderRepository.deleteById(id);
            logger.info("Order with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        }
        logger.warn("Failed to delete. Order with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }

}