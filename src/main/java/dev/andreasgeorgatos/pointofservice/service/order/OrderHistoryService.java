package dev.andreasgeorgatos.pointofservice.service.order;


import dev.andreasgeorgatos.pointofservice.model.order.OrderHistory;
import dev.andreasgeorgatos.pointofservice.repository.orders.OrderHistoryRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger; // Added for logging
import org.slf4j.LoggerFactory; // Added for logging
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class that handles business logic related to order history records.
 * It provides functionalities to create, retrieve, update, and delete order history entries.
 */
@Service
public class OrderHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(OrderHistoryService.class); // Added logger

    private final OrderHistoryRepository orderHistoryRepository;

    /**
     * Constructs an {@code OrderHistoryService} with the specified {@link OrderHistoryRepository}.
     *
     * @param orderHistoryRepository The repository for order history data access.
     */
    @Autowired
    public OrderHistoryService(OrderHistoryRepository orderHistoryRepository) {
        this.orderHistoryRepository = orderHistoryRepository;
    }

    /**
     * Retrieves all order history records.
     *
     * @return A {@link ResponseEntity} containing a list of all {@link OrderHistory} objects and HTTP status OK,
     *         or HTTP status NOT_FOUND if no order history records exist.
     */
    public ResponseEntity<List<OrderHistory>> getAllOrdersHistory() {
        logger.info("Fetching all order history records");
        List<OrderHistory> orderHistories = orderHistoryRepository.findAll(); // Renamed variable

        if (!orderHistories.isEmpty()) {
            logger.debug("Found {} order history records", orderHistories.size());
            return ResponseEntity.ok(orderHistories);
        }
        logger.info("No order history records found");
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves a specific order history record by its ID.
     *
     * @param id The ID of the order history record to retrieve.
     * @return A {@link ResponseEntity} containing the {@link OrderHistory} if found and HTTP status OK,
     *         or HTTP status NOT_FOUND if the record does not exist.
     */
    public ResponseEntity<OrderHistory> getOrderHistoryById(long id) {
        logger.info("Fetching order history record with ID: {}", id);
        Optional<OrderHistory> orderHistoryOptional = orderHistoryRepository.findById(id); // Renamed variable

        if (orderHistoryOptional.isPresent()) {
            logger.debug("Found order history record: {}", orderHistoryOptional.get());
            return ResponseEntity.ok(orderHistoryOptional.get());
        }
        logger.warn("Order history record with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }

    /**
     * Creates a new order history record.
     * Timestamps like {@code createdAt} or {@code updatedAt} within the {@link OrderHistory} entity
     * are expected to be handled by JPA or set by the caller before invoking this method,
     * as this service method directly saves the provided entity.
     *
     * @param orderHistory The {@link OrderHistory} object to create.
     * @return A {@link ResponseEntity} containing the created {@link OrderHistory} and HTTP status OK.
     */
    @Transactional
    public ResponseEntity<OrderHistory> createOrderHistory(OrderHistory orderHistory) {
        logger.info("Creating new order history record: {}", orderHistory);
        OrderHistory savedOrderHistory = orderHistoryRepository.save(orderHistory);
        logger.info("Order history record created successfully with ID: {}", savedOrderHistory.getId());
        return ResponseEntity.ok(savedOrderHistory);
    }

    /**
     * Updates the associated order and order status of an existing order history record.
     * Note: Consider if order history records should be immutable based on business requirements,
     * as this method allows modification of existing history entries.
     * Timestamps like {@code updatedAt} should ideally be set here or by JPA if applicable.
     *
     * @param id The ID of the order history record to edit.
     * @param orderHistoryDetails The {@link OrderHistory} object containing the new details.
     * @return A {@link ResponseEntity} containing the updated {@link OrderHistory} and HTTP status OK if successful,
     *         or HTTP status NOT_FOUND if the record with the given ID does not exist.
     */
    @Transactional
    public ResponseEntity<OrderHistory> editOrderHistoryById(long id, OrderHistory orderHistoryDetails) {
        logger.info("Editing order history record with ID: {}, new details: {}", id, orderHistoryDetails);
        Optional<OrderHistory> optionalOrderHistory = orderHistoryRepository.findById(id);

        if (optionalOrderHistory.isPresent()) {
            OrderHistory oldOrderHistory = optionalOrderHistory.get();

            oldOrderHistory.setOrder(orderHistoryDetails.getOrder());
            oldOrderHistory.setOrderStatus(orderHistoryDetails.getOrderStatus());
            // Consider setting an 'updatedAt' timestamp here if the entity has one:
            // oldOrderHistory.setUpdatedAt(LocalDateTime.now()); 

            OrderHistory savedOrderHistory = orderHistoryRepository.save(oldOrderHistory);
            logger.info("Order history record with ID: {} updated successfully", savedOrderHistory.getId());
            return ResponseEntity.ok(savedOrderHistory);
        }
        logger.warn("Failed to edit. Order history record with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }

    /**
     * Deletes an order history record by its ID.
     *
     * @param id The ID of the order history record to delete.
     * @return A {@link ResponseEntity} with HTTP status NO_CONTENT if deletion is successful,
     *         or HTTP status NOT_FOUND if the record with the given ID does not exist.
     */
    @Transactional
    public ResponseEntity<Void> deleteOrderHistoryById(long id) { // Changed signature to ResponseEntity<Void>
        logger.info("Deleting order history record with ID: {}", id);
        Optional<OrderHistory> orderHistory = orderHistoryRepository.findById(id);

        if (orderHistory.isPresent()) {
            orderHistoryRepository.deleteById(id);
            logger.info("Order history record with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        }
        logger.warn("Failed to delete. Order history record with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }
}
