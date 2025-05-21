package dev.andreasgeorgatos.pointofservice.service.order;

import dev.andreasgeorgatos.pointofservice.model.order.OrderStatuses;
import dev.andreasgeorgatos.pointofservice.repository.orders.OrderStatusRepository;
import org.springframework.transaction.annotation.Transactional; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * {@code OrderStatusService} manages CRUD operations for order statuses.
 * It provides functionalities to create, retrieve, update, and delete order status entries.
 */
@Service
public class OrderStatusService {

    private static final Logger logger = LoggerFactory.getLogger(OrderStatusService.class);

    private final OrderStatusRepository orderStatusRepository;

    /**
     * Constructs an {@code OrderStatusService} with the specified {@link OrderStatusRepository}.
     *
     * @param orderStatusRepository The repository for order status data access.
     */
    @Autowired
    public OrderStatusService(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }

    /**
     * Retrieves all order statuses. This operation is read-only.
     *
     * @return A {@link ResponseEntity} containing a list of all {@link OrderStatuses} and HTTP status OK,
     *         or HTTP status NOT_FOUND if no statuses exist.
     */
    @Transactional(readOnly = true) 
    public ResponseEntity<List<OrderStatuses>> getAllOrderStatuses() { 
        logger.info("Fetching all order statuses");
        List<OrderStatuses> statuses = orderStatusRepository.findAll();

        if (statuses.isEmpty()) {
            logger.info("No order statuses found");
            return ResponseEntity.notFound().build();
        }
        logger.debug("Found {} order statuses", statuses.size());
        return ResponseEntity.ok(statuses);
    }

    /**
     * Retrieves a specific order status by its ID. This operation is read-only.
     *
     * @param id The ID of the order status to retrieve.
     * @return A {@link ResponseEntity} containing the {@link OrderStatuses} if found and HTTP status OK,
     *         or HTTP status NOT_FOUND if the status does not exist.
     */
    @Transactional(readOnly = true) 
    public ResponseEntity<OrderStatuses> getOrderStatusById(long id) {
        logger.info("Fetching order status with ID: {}", id);
        Optional<OrderStatuses> orderStatusOptional = orderStatusRepository.findById(id); 

        if (orderStatusOptional.isPresent()) {
            logger.debug("Found order status: {}", orderStatusOptional.get());
            return ResponseEntity.ok(orderStatusOptional.get());
        }
        logger.warn("Order status with ID: {} not found", id);
        // The line below was inconsistent with the preceding if-block return pattern, fixed.
        return ResponseEntity.notFound().build(); 
    }

    /**
     * Creates a new order status.
     *
     * @param orderStatus The {@link OrderStatuses} object to create.
     * @return A {@link ResponseEntity} containing the created {@link OrderStatuses} and HTTP status CREATED.
     */
    @Transactional
    public ResponseEntity<OrderStatuses> createOrderStatus(OrderStatuses orderStatus) {
        logger.info("Creating new order status: {}", orderStatus);
        OrderStatuses savedStatus = orderStatusRepository.save(orderStatus); // Renamed variable for clarity
        logger.info("Order status created successfully with ID: {}", savedStatus.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStatus); 
    }

    /**
     * Updates an existing order status by its ID.
     *
     * @param id The ID of the order status to edit.
     * @param orderStatusDetails The {@link OrderStatuses} object containing the new details for the status.
     * @return A {@link ResponseEntity} containing the updated {@link OrderStatuses} and HTTP status OK if successful,
     *         or HTTP status NOT_FOUND if the status with the given ID does not exist.
     */
    @Transactional
    public ResponseEntity<OrderStatuses> editOrderStatusById(long id, OrderStatuses orderStatusDetails) { // Renamed param for clarity
        logger.info("Editing order status with ID: {}, new details: {}", id, orderStatusDetails);
        Optional<OrderStatuses> foundOrderStatus = orderStatusRepository.findById(id);

        if (foundOrderStatus.isPresent()) {
            OrderStatuses oldOrderStatus = foundOrderStatus.get();

            oldOrderStatus.setStatus(orderStatusDetails.getStatus());
            OrderStatuses savedOrderStatus = orderStatusRepository.save(oldOrderStatus);
            logger.info("Order status with ID: {} updated successfully", savedOrderStatus.getId());
            return ResponseEntity.ok(savedOrderStatus);
        }
        logger.warn("Failed to edit. Order status with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }

    /**
     * Deletes an order status by its ID.
     *
     * @param id The ID of the order status to delete.
     * @return A {@link ResponseEntity} with HTTP status NO_CONTENT if deletion is successful,
     *         or HTTP status NOT_FOUND if the status with the given ID does not exist.
     */
    @Transactional
    public ResponseEntity<Void> deleteOrderStatusById(long id) { 
        logger.info("Deleting order status with ID: {}", id);
        Optional<OrderStatuses> orderStatus = orderStatusRepository.findById(id);

        if (orderStatus.isPresent()) {
            orderStatusRepository.deleteById(id);
            logger.info("Order status with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        }
        logger.warn("Failed to delete. Order status with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }
}
