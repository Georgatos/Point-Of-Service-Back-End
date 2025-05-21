package dev.andreasgeorgatos.pointofservice.service.delivery;

import dev.andreasgeorgatos.pointofservice.model.delivery.DeliveryHistory;
import dev.andreasgeorgatos.pointofservice.model.delivery.DeliveryStatus;
import dev.andreasgeorgatos.pointofservice.model.order.Order;
import dev.andreasgeorgatos.pointofservice.model.order.OrderStatuses;
import dev.andreasgeorgatos.pointofservice.repository.delivery.DeliveryHistoryRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * {@code DeliveryHistoryService} handles business logic for delivery history records.
 * It provides functionalities to create, retrieve, update, and delete delivery history entries,
 * interacting with various repositories to manage related entities.
 */
@Service
public class DeliveryHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryHistoryService.class);

    private final DeliveryHistoryRepository deliveryHistoryRepository;
    private final OrderRepository orderRepository;
    private final DeliveryStatusRepository deliveryStatusRepository;
    private final OrderStatusRepository orderStatusRepository;

    /**
     * Constructs a {@code DeliveryHistoryService} with the specified repositories.
     *
     * @param deliveryHistoryRepository Repository for delivery history data access.
     * @param orderRepository Repository for order data access, used for validating orders.
     * @param deliveryStatusRepository Repository for delivery status data access.
     * @param orderStatusRepository Repository for order status data access.
     */
    @Autowired
    public DeliveryHistoryService(DeliveryHistoryRepository deliveryHistoryRepository, OrderRepository orderRepository, DeliveryStatusRepository deliveryStatusRepository, OrderStatusRepository orderStatusRepository) {
        this.deliveryHistoryRepository = deliveryHistoryRepository;
        this.orderRepository = orderRepository;
        this.deliveryStatusRepository = deliveryStatusRepository;
        this.orderStatusRepository = orderStatusRepository;
    }

    /**
     * Retrieves all delivery history records. This operation is read-only.
     *
     * @return A {@link ResponseEntity} containing a list of all {@link DeliveryHistory} objects and HTTP status OK,
     *         or HTTP status NOT_FOUND if no delivery history records exist.
     */
    @Transactional(readOnly = true) 
    public ResponseEntity<List<DeliveryHistory>> getAllDeliveryHistories() {
        logger.info("Fetching all delivery histories");
        List<DeliveryHistory> deliveryHistories = deliveryHistoryRepository.findAll();

        if (!deliveryHistories.isEmpty()) {
            logger.debug("Found {} delivery histories", deliveryHistories.size());
            return ResponseEntity.ok(deliveryHistories);
        }
        logger.info("No delivery histories found");
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves a specific delivery history record by its ID. This operation is read-only.
     *
     * @param id The ID of the delivery history record to retrieve.
     * @return A {@link ResponseEntity} containing the {@link DeliveryHistory} if found and HTTP status OK,
     *         or HTTP status NOT_FOUND if the record does not exist.
     */
    @Transactional(readOnly = true) 
    public ResponseEntity<DeliveryHistory> getDeliveryHistoryById(long id) {
        logger.info("Fetching delivery history with ID: {}", id);
        Optional<DeliveryHistory> deliveryHistory = deliveryHistoryRepository.findById(id);

        if (deliveryHistory.isPresent()) {
            logger.debug("Found delivery history: {}", deliveryHistory.get());
            return ResponseEntity.ok(deliveryHistory.get());
        }
        logger.warn("Delivery history with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }

    /**
     * Creates a new delivery history record.
     * Requires valid {@code OrderStatus} and {@code DeliveryStatus} IDs to be provided.
     * Associated orders are also validated by their IDs.
     *
     * @param deliveryHistory The {@link DeliveryHistory} object to create. Must include valid IDs for associated entities.
     * @return A {@link ResponseEntity} containing the created {@link DeliveryHistory} with HTTP status CREATED if successful.
     *         Returns HTTP status BAD_REQUEST if required fields (OrderStatus ID, DeliveryStatus ID, or valid Order IDs) are missing or invalid.
     */
    @Transactional
    public ResponseEntity<?> createDeliveryHistory(DeliveryHistory deliveryHistory) { 
        logger.info("Creating new delivery history: {}", deliveryHistory);
        if (deliveryHistory.getOrderStatus() == null || deliveryHistory.getOrderStatus().getId() == null) {
            logger.warn("Attempted to create delivery history with null OrderStatus ID.");
            return ResponseEntity.badRequest().body("Order Status and its ID must be provided.");
        }
        if (deliveryHistory.getDeliveryStatus() == null || deliveryHistory.getDeliveryStatus().getId() == null) {
            logger.warn("Attempted to create delivery history with null DeliveryStatus ID.");
            return ResponseEntity.badRequest().body("Delivery Status and its ID must be provided.");
        }

        Optional<OrderStatuses> optionalOrderStatus = orderStatusRepository.findById(deliveryHistory.getOrderStatus().getId());
        Optional<DeliveryStatus> optionalDeliveryStatus = deliveryStatusRepository.findById(deliveryHistory.getDeliveryStatus().getId());

        if (optionalOrderStatus.isEmpty()) {
            logger.warn("Invalid OrderStatus ID: {} provided for new delivery history.", deliveryHistory.getOrderStatus().getId());
            return ResponseEntity.badRequest().body("Invalid OrderStatus ID: " + deliveryHistory.getOrderStatus().getId());
        }
        if (optionalDeliveryStatus.isEmpty()) {
            logger.warn("Invalid DeliveryStatus ID: {} provided for new delivery history.", deliveryHistory.getDeliveryStatus().getId());
            return ResponseEntity.badRequest().body("Invalid DeliveryStatus ID: " + deliveryHistory.getDeliveryStatus().getId());
        }

        List<Order> orders = new ArrayList<>();
        if (deliveryHistory.getOrders() != null) {
            for (Order order : deliveryHistory.getOrders()) {
                if (order == null || order.getId() == null) {
                    logger.warn("Delivery history creation attempt with a null order or order with null ID in the list.");
                    return ResponseEntity.badRequest().body("Order in list cannot be null and must have an ID.");
                }
                Optional<Order> optionalOrder = orderRepository.findById(order.getId());
                if (optionalOrder.isEmpty()) {
                    logger.warn("Invalid Order ID {} in list for new delivery history.", order.getId());
                    return ResponseEntity.badRequest().body("Invalid Order ID in list: " + order.getId());
                }
                orders.add(optionalOrder.get());
            }
        }

        deliveryHistory.setOrderStatus(optionalOrderStatus.get());
        deliveryHistory.setDeliveryStatus(optionalDeliveryStatus.get());
        deliveryHistory.setOrders(orders); 

        DeliveryHistory savedDeliveryHistory = deliveryHistoryRepository.save(deliveryHistory);
        logger.info("Delivery history created successfully with ID: {}", savedDeliveryHistory.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDeliveryHistory); 
    }

    /**
     * Updates an existing delivery history record by its ID.
     * This method replaces the entire list of associated orders with the new list provided in {@code deliveryHistoryDetails},
     * after validating each order ID. It also updates the {@code OrderStatus} and {@code DeliveryStatus}.
     *
     * @param id The ID of the delivery history record to edit.
     * @param deliveryHistoryDetails The {@link DeliveryHistory} object containing the new details.
     * @return A {@link ResponseEntity} containing the updated {@link DeliveryHistory} and HTTP status OK if successful.
     *         Returns HTTP status NOT_FOUND if the delivery history record with the given ID does not exist.
     *         Returns HTTP status BAD_REQUEST if required fields (OrderStatus ID, DeliveryStatus ID, or valid Order IDs) are missing or invalid.
     */
    @Transactional
    public ResponseEntity<?> editDeliveryHistoryById(long id, DeliveryHistory deliveryHistoryDetails) { 
        logger.info("Editing delivery history with ID: {}, new details: {}", id, deliveryHistoryDetails);
        Optional<DeliveryHistory> optionalDeliveryHistory = deliveryHistoryRepository.findById(id);

        if (optionalDeliveryHistory.isEmpty()) {
            logger.warn("Failed to edit. Delivery history with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }

        if (deliveryHistoryDetails.getOrderStatus() == null || deliveryHistoryDetails.getOrderStatus().getId() == null) {
            logger.warn("Attempted to edit delivery history ID: {} with null OrderStatus ID.", id);
            return ResponseEntity.badRequest().body("Order Status and its ID must be provided.");
        }
        if (deliveryHistoryDetails.getDeliveryStatus() == null || deliveryHistoryDetails.getDeliveryStatus().getId() == null) {
            logger.warn("Attempted to edit delivery history ID: {} with null DeliveryStatus ID.", id);
            return ResponseEntity.badRequest().body("Delivery Status and its ID must be provided.");
        }

        Optional<OrderStatuses> optionalOrderStatus = orderStatusRepository.findById(deliveryHistoryDetails.getOrderStatus().getId());
        Optional<DeliveryStatus> optionalDeliveryStatus = deliveryStatusRepository.findById(deliveryHistoryDetails.getDeliveryStatus().getId());

        if (optionalOrderStatus.isEmpty()) {
            logger.warn("Invalid OrderStatus ID: {} provided for delivery history ID: {}", deliveryHistoryDetails.getOrderStatus().getId(), id);
            return ResponseEntity.badRequest().body("Invalid OrderStatus ID: " + deliveryHistoryDetails.getOrderStatus().getId());
        }
        if (optionalDeliveryStatus.isEmpty()) {
            logger.warn("Invalid DeliveryStatus ID: {} provided for delivery history ID: {}", deliveryHistoryDetails.getDeliveryStatus().getId(), id);
             return ResponseEntity.badRequest().body("Invalid DeliveryStatus ID: " + deliveryHistoryDetails.getDeliveryStatus().getId());
        }

        DeliveryHistory foundDeliveryHistory = optionalDeliveryHistory.get();
        
        List<Order> updatedOrders = new ArrayList<>();
        if (deliveryHistoryDetails.getOrders() != null) {
            for (Order order : deliveryHistoryDetails.getOrders()) {
                 if (order == null || order.getId() == null) {
                    logger.warn("Edit delivery history ID: {} attempt with a null order or order with null ID in the list.", id);
                    return ResponseEntity.badRequest().body("Order in list cannot be null and must have an ID.");
                }
                Optional<Order> optionalOrder = orderRepository.findById(order.getId());
                if (optionalOrder.isEmpty()) {
                    logger.warn("Invalid Order ID {} in list for delivery history ID: {}.", order.getId(), id);
                    return ResponseEntity.badRequest().body("Invalid Order ID in list: " + order.getId());
                }
                updatedOrders.add(optionalOrder.get());
            }
        }
        foundDeliveryHistory.setOrders(updatedOrders); 

        foundDeliveryHistory.setOrderStatus(optionalOrderStatus.get());
        foundDeliveryHistory.setDeliveryStatus(optionalDeliveryStatus.get());
        // Any other editable fields for DeliveryHistory would be set here, e.g.:
        // foundDeliveryHistory.setDriverName(deliveryHistoryDetails.getDriverName());
        
        DeliveryHistory savedDeliveryHistory = deliveryHistoryRepository.save(foundDeliveryHistory);
        logger.info("Delivery history with ID: {} updated successfully", savedDeliveryHistory.getId());
        return ResponseEntity.ok(savedDeliveryHistory);
    }

    /**
     * Deletes a delivery history record by its ID.
     *
     * @param id The ID of the delivery history record to delete.
     * @return A {@link ResponseEntity} with HTTP status NO_CONTENT if deletion is successful,
     *         or HTTP status NOT_FOUND if the record with the given ID does not exist.
     */
    @Transactional
    public ResponseEntity<Void> deleteDeliveryHistoryById(long id) { 
        logger.info("Deleting delivery history with ID: {}", id);
        Optional<DeliveryHistory> deliveryHistory = deliveryHistoryRepository.findById(id);

        if (deliveryHistory.isPresent()) {
            deliveryHistoryRepository.deleteById(id);
            logger.info("Delivery history with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        }
        logger.warn("Failed to delete. Delivery history with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }
}
