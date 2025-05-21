package dev.andreasgeorgatos.pointofservice.controller.orders;

import dev.andreasgeorgatos.pointofservice.model.order.OrderHistory;
import dev.andreasgeorgatos.pointofservice.service.order.OrderHistoryService;
import dev.andreasgeorgatos.pointofservice.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing order history.
 * Provides endpoints for CRUD operations on order history records.
 */
@RestController
@RequestMapping("/api/v1/order-history")
public class OrderHistoryController {

    private final OrderHistoryService orderHistoryService;

    /**
     * Constructs an OrderHistoryController with the necessary service.
     * @param orderHistoryService The service to handle order history operations.
     */
    @Autowired
    public OrderHistoryController(OrderHistoryService orderHistoryService) {
        this.orderHistoryService = orderHistoryService;
    }

    /**
     * Retrieves all order history records.
     * @return A ResponseEntity containing a list of all order history records and HTTP status OK.
     */
    @GetMapping()
    public ResponseEntity<List<OrderHistory>> getAllOrderHistories() {
        return orderHistoryService.getAllOrdersHistory();
    }

    /**
     * Retrieves a specific order history record by its ID.
     * @param id The ID of the order history record to retrieve.
     * @return A ResponseEntity containing the order history record and HTTP status OK,
     * or HTTP status NOT_FOUND if the record does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderHistory> getOrderHistoryById(@PathVariable Long id) {
        return orderHistoryService.getOrderHistoryById(id);
    }

    /**
     * Creates a new order history record.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param orderHistory The order history record to create.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the created order history record and HTTP status CREATED,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PostMapping()
    public ResponseEntity<?> createOrderHistory(@Valid @RequestBody OrderHistory orderHistory, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return orderHistoryService.createOrderHistory(orderHistory);
    }

    /**
     * Updates an existing order history record.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param id The ID of the order history record to update.
     * @param orderHistory The updated order history information.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the updated order history record and HTTP status OK,
     * or HTTP status NOT_FOUND if the record does not exist,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> editOrderHistoryById(@PathVariable Long id, @Valid @RequestBody OrderHistory orderHistory, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return orderHistoryService.editOrderHistoryById(id, orderHistory);
    }

    /**
     * Deletes a specific order history record by its ID.
     * @param id The ID of the order history record to delete.
     * @return A ResponseEntity with HTTP status NO_CONTENT if successful,
     * or HTTP status NOT_FOUND if the record does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderHistory(@PathVariable Long id) { // Removed @Valid and BindingResult
        return orderHistoryService.deleteOrderHistoryById(id);
    }
}
