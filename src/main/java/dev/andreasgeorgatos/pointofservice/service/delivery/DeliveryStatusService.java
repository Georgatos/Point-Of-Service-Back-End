package dev.andreasgeorgatos.pointofservice.service.delivery;

import dev.andreasgeorgatos.pointofservice.model.delivery.DeliveryStatus;
import dev.andreasgeorgatos.pointofservice.model.order.Order;
import dev.andreasgeorgatos.pointofservice.model.order.OrderStatuses;
import dev.andreasgeorgatos.pointofservice.repository.delivery.DeliveryStatusRepository;
import dev.andreasgeorgatos.pointofservice.repository.orders.OrderRepository;
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
 * {@code DeliveryStatusService} manages CRUD operations for delivery statuses.
 * It provides functionalities to create, retrieve, update, and delete delivery status entries,
 * linking them to orders and overall order statuses.
 */
@Service
public class DeliveryStatusService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryStatusService.class);

    private final DeliveryStatusRepository deliveryStatusRepository;
    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;

    /**
     * Constructs a {@code DeliveryStatusService} with the specified repositories.
     *
     * @param deliveryStatusRepository Repository for delivery status data access.
     * @param orderRepository Repository for order data access, used for linking delivery statuses to orders.
     * @param orderStatusRepository Repository for order status data access, used for linking delivery statuses to overall order statuses.
     */
    @Autowired
    public DeliveryStatusService(DeliveryStatusRepository deliveryStatusRepository, OrderRepository orderRepository, OrderStatusRepository orderStatusRepository) {
        this.deliveryStatusRepository = deliveryStatusRepository;
        this.orderRepository = orderRepository;
        this.orderStatusRepository = orderStatusRepository;
    }

    /**
     * Retrieves all delivery statuses. This operation is read-only.
     *
     * @return A {@link ResponseEntity} containing a list of all {@link DeliveryStatus} objects and HTTP status OK,
     *         or HTTP status NOT_FOUND if no delivery statuses exist.
     */
    @Transactional(readOnly = true) 
    public ResponseEntity<List<DeliveryStatus>> getAllDeliveryStatuses() {
        logger.info("Fetching all delivery statuses");
        List<DeliveryStatus> deliveryStatuses = deliveryStatusRepository.findAll();

        if (!deliveryStatuses.isEmpty()) {
            logger.debug("Found {} delivery statuses", deliveryStatuses.size());
            return ResponseEntity.ok(deliveryStatuses);
        }
        logger.info("No delivery statuses found");
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves a specific delivery status by its ID. This operation is read-only.
     *
     * @param id The ID of the delivery status to retrieve.
     * @return A {@link ResponseEntity} containing the {@link DeliveryStatus} if found and HTTP status OK,
     *         or HTTP status NOT_FOUND if the status does not exist.
     */
    @Transactional(readOnly = true) 
    public ResponseEntity<DeliveryStatus> getDeliveryStatusById(long id) {
        logger.info("Fetching delivery status with ID: {}", id);
        Optional<DeliveryStatus> deliveryStatusOptional = deliveryStatusRepository.findById(id); 

        if (deliveryStatusOptional.isPresent()) {
            logger.debug("Found delivery status: {}", deliveryStatusOptional.get());
            return ResponseEntity.ok(deliveryStatusOptional.get());
        }
        logger.warn("Delivery status with ID: {} not found", id);
        return ResponseEntity.notFound().build(); 
    }

    /**
     * Creates a new delivery status.
     * Requires valid {@code Order} and {@code OrderStatus} IDs to be provided within the {@code deliveryStatus} object.
     * The {@code deliveryDate} should be set by the client or handled by business logic if it defaults (e.g., to current date/time).
     *
     * @param deliveryStatus The {@link DeliveryStatus} object to create. Must include valid IDs for associated {@code Order} and {@code OrderStatus}.
     * @return A {@link ResponseEntity} containing the created {@link DeliveryStatus} with HTTP status CREATED if successful.
     *         Returns HTTP status BAD_REQUEST if required fields (Order ID, OrderStatus ID) are missing or invalid.
     */
    @Transactional
    public ResponseEntity<?> createDeliveryStatus(DeliveryStatus deliveryStatus) { 
        logger.info("Creating new delivery status: {}", deliveryStatus);
        if (deliveryStatus.getOrderId() == null || deliveryStatus.getOrderId().getId() == null) {
            logger.warn("Attempted to create delivery status with null Order ID.");
            return ResponseEntity.badRequest().body("Order and its ID must be provided.");
        }
        if (deliveryStatus.getOrderStatusId() == null || deliveryStatus.getOrderStatusId().getId() == null) {
            logger.warn("Attempted to create delivery status with null OrderStatus ID.");
            return ResponseEntity.badRequest().body("Order Status and its ID must be provided.");
        }

        Optional<Order> optionalOrder = orderRepository.findById(deliveryStatus.getOrderId().getId());
        Optional<OrderStatuses> optionalOrderStatus = orderStatusRepository.findById(deliveryStatus.getOrderStatusId().getId());

        if (optionalOrder.isEmpty()) {
            logger.warn("Invalid Order ID: {} provided for new delivery status.", deliveryStatus.getOrderId().getId());
            return ResponseEntity.badRequest().body("Invalid Order ID: " + deliveryStatus.getOrderId().getId());
        }
        if (optionalOrderStatus.isEmpty()) {
            logger.warn("Invalid OrderStatus ID: {} provided for new delivery status.", deliveryStatus.getOrderStatusId().getId());
            return ResponseEntity.badRequest().body("Invalid OrderStatus ID: " + deliveryStatus.getOrderStatusId().getId());
        }

        deliveryStatus.setOrderId(optionalOrder.get());
        deliveryStatus.setOrderStatusId(optionalOrderStatus.get());
        // deliveryStatus.setDeliveryDate(...); // Ensure this is set if required by business logic
        
        DeliveryStatus savedDeliveryStatus = deliveryStatusRepository.save(deliveryStatus);
        logger.info("Delivery status created successfully with ID: {}", savedDeliveryStatus.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDeliveryStatus); 
    }

    /**
     * Updates an existing delivery status by its ID.
     * Requires valid {@code Order} and {@code OrderStatus} IDs in the {@code deliveryStatusDetails}.
     *
     * @param id The ID of the delivery status to edit.
     * @param deliveryStatusDetails The {@link DeliveryStatus} object containing the new details.
     * @return A {@link ResponseEntity} containing the updated {@link DeliveryStatus} and HTTP status OK if successful.
     *         Returns HTTP status NOT_FOUND if the delivery status with the given ID does not exist.
     *         Returns HTTP status BAD_REQUEST if required fields (Order ID, OrderStatus ID) are missing or invalid in {@code deliveryStatusDetails}.
     */
    @Transactional
    public ResponseEntity<?> editDeliveryStatusById(long id, DeliveryStatus deliveryStatusDetails) { 
        logger.info("Editing delivery status with ID: {}, new details: {}", id, deliveryStatusDetails);
        Optional<DeliveryStatus> optionalDeliveryStatus = deliveryStatusRepository.findById(id);

        if (optionalDeliveryStatus.isEmpty()) {
            logger.warn("Failed to edit. Delivery status with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
        
        if (deliveryStatusDetails.getOrderId() == null || deliveryStatusDetails.getOrderId().getId() == null) {
            logger.warn("Attempted to edit delivery status ID: {} with null Order ID.", id);
            return ResponseEntity.badRequest().body("Order and its ID must be provided for editing.");
        }
        if (deliveryStatusDetails.getOrderStatusId() == null || deliveryStatusDetails.getOrderStatusId().getId() == null) {
            logger.warn("Attempted to edit delivery status ID: {} with null OrderStatus ID.", id);
            return ResponseEntity.badRequest().body("Order Status and its ID must be provided for editing.");
        }

        Optional<Order> optionalOrder = orderRepository.findById(deliveryStatusDetails.getOrderId().getId());
        Optional<OrderStatuses> optionalOrderStatus = orderStatusRepository.findById(deliveryStatusDetails.getOrderStatusId().getId());

        if (optionalOrder.isEmpty()) {
            logger.warn("Invalid Order ID: {} provided for delivery status ID: {}", deliveryStatusDetails.getOrderId().getId(), id);
            return ResponseEntity.badRequest().body("Invalid Order ID: " + deliveryStatusDetails.getOrderId().getId());
        }
        if (optionalOrderStatus.isEmpty()) {
            logger.warn("Invalid OrderStatus ID: {} provided for delivery status ID: {}", deliveryStatusDetails.getOrderStatusId().getId(), id);
            return ResponseEntity.badRequest().body("Invalid OrderStatus ID: " + deliveryStatusDetails.getOrderStatusId().getId());
        }

        DeliveryStatus oldDeliveryStatus = optionalDeliveryStatus.get();
        oldDeliveryStatus.setDeliveryDate(deliveryStatusDetails.getDeliveryDate());
        oldDeliveryStatus.setOrderStatusId(optionalOrderStatus.get());
        oldDeliveryStatus.setOrderId(optionalOrder.get());
        // Set other fields from deliveryStatusDetails if they are meant to be editable, e.g.:
        // oldDeliveryStatus.setActualDeliveryTime(deliveryStatusDetails.getActualDeliveryTime());
        // oldDeliveryStatus.setNotes(deliveryStatusDetails.getNotes());
        
        DeliveryStatus savedDeliveryStatus = deliveryStatusRepository.save(oldDeliveryStatus);
        logger.info("Delivery status with ID: {} updated successfully", savedDeliveryStatus.getId());
        return ResponseEntity.ok(savedDeliveryStatus);
    }

    /**
     * Deletes a delivery status by its ID.
     *
     * @param id The ID of the delivery status to delete.
     * @return A {@link ResponseEntity} with HTTP status NO_CONTENT if deletion is successful,
     *         or HTTP status NOT_FOUND if the status with the given ID does not exist.
     */
    @Transactional
    public ResponseEntity<Void> deleteDeliveryStatusById(long id) { 
        logger.info("Deleting delivery status with ID: {}", id);
        Optional<DeliveryStatus> deliveryStatusOptional = deliveryStatusRepository.findById(id); 

        if (deliveryStatusOptional.isPresent()) {
            deliveryStatusRepository.deleteById(id);
            logger.info("Delivery status with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        }
        logger.warn("Failed to delete. Delivery status with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }
}
