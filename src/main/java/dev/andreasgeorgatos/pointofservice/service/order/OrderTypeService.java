package dev.andreasgeorgatos.pointofservice.service.order;

import dev.andreasgeorgatos.pointofservice.model.order.OrderType;
import dev.andreasgeorgatos.pointofservice.repository.orders.OrderTypeRepository;
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
 * {@code OrderTypeService} manages CRUD operations for order types.
 * It provides functionalities to create, retrieve, update, and delete order type entries.
 */
@Service
public class OrderTypeService {

    private static final Logger logger = LoggerFactory.getLogger(OrderTypeService.class);

    private final OrderTypeRepository orderTypeRepository;

    /**
     * Constructs an {@code OrderTypeService} with the specified {@link OrderTypeRepository}.
     *
     * @param orderTypeRepository The repository for order type data access.
     */
    @Autowired
    public OrderTypeService(OrderTypeRepository orderTypeRepository) {
        this.orderTypeRepository = orderTypeRepository;
    }

    /**
     * Retrieves all order types. This operation is read-only.
     *
     * @return A {@link ResponseEntity} containing a list of all {@link OrderType} objects and HTTP status OK,
     *         or HTTP status NOT_FOUND if no order types exist.
     */
    @Transactional(readOnly = true) 
    public ResponseEntity<List<OrderType>> getAllOrderTypes() {
        logger.info("Fetching all order types");
        List<OrderType> orderTypes = orderTypeRepository.findAll();

        if (orderTypes.isEmpty()) {
            logger.info("No order types found");
            return ResponseEntity.notFound().build();
        }
        logger.debug("Found {} order types", orderTypes.size());
        return ResponseEntity.ok(orderTypes);
    }

    /**
     * Retrieves a specific order type by its ID. This operation is read-only.
     *
     * @param id The ID of the order type to retrieve.
     * @return A {@link ResponseEntity} containing the {@link OrderType} if found and HTTP status OK,
     *         or HTTP status NOT_FOUND if the order type does not exist.
     */
    @Transactional(readOnly = true) 
    public ResponseEntity<OrderType> getOrderTypeById(long id) {
        logger.info("Fetching order type with ID: {}", id);
        Optional<OrderType> orderType = orderTypeRepository.findById(id);

        if(orderType.isPresent()) {
            logger.debug("Found order type: {}", orderType.get());
            return ResponseEntity.ok(orderType.get());
        }
        logger.warn("Order type with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }

    /**
     * Updates an existing order type by its ID.
     *
     * @param id The ID of the order type to edit.
     * @param orderTypeDetails The {@link OrderType} object containing the new details for the order type.
     * @return A {@link ResponseEntity} containing the updated {@link OrderType} and HTTP status OK if successful,
     *         or HTTP status NOT_FOUND if the order type with the given ID does not exist.
     */
    @Transactional
    public ResponseEntity<OrderType> editOrderTypeById(long id, OrderType orderTypeDetails) { 
        logger.info("Editing order type with ID: {}, new details: {}", id, orderTypeDetails);
        Optional<OrderType> foundOrderType = orderTypeRepository.findById(id);

        if (foundOrderType.isPresent()) {
            OrderType oldOrderType = foundOrderType.get();

            oldOrderType.setType(orderTypeDetails.getType());

            OrderType savedOrderType = orderTypeRepository.save(oldOrderType);
            logger.info("Order type with ID: {} updated successfully", savedOrderType.getId());
            return ResponseEntity.ok(savedOrderType);
        }
        logger.warn("Failed to edit. Order type with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }

    /**
     * Creates a new order type.
     *
     * @param orderType The {@link OrderType} object to create.
     * @return A {@link ResponseEntity} containing the created {@link OrderType} and HTTP status CREATED.
     */
    @Transactional
    public ResponseEntity<OrderType> createOrderType(OrderType orderType) { 
        logger.info("Creating new order type: {}", orderType);
        OrderType savedOrderType = orderTypeRepository.save(orderType);
        logger.info("Order type created successfully with ID: {}", savedOrderType.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrderType); 
    }

    /**
     * Deletes an order type by its ID.
     *
     * @param id The ID of the order type to delete.
     * @return A {@link ResponseEntity} with HTTP status NO_CONTENT if deletion is successful,
     *         or HTTP status NOT_FOUND if the order type with the given ID does not exist.
     */
    @Transactional
    public ResponseEntity<Void> deleteOrderTypeById(long id) { 
        logger.info("Deleting order type with ID: {}", id);
        Optional<OrderType> orderType = orderTypeRepository.findById(id);

        if (orderType.isPresent()) {
            orderTypeRepository.deleteById(id);
            logger.info("Order type with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        }
        logger.warn("Failed to delete. Order type with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }
}
