package dev.andreasgeorgatos.pointofservice.controller.orders;


import dev.andreasgeorgatos.pointofservice.model.order.OrderStatuses;
import dev.andreasgeorgatos.pointofservice.service.order.OrderStatusService;
import dev.andreasgeorgatos.pointofservice.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing order statuses.
 * Provides endpoints for CRUD operations on order statuses.
 */
@RestController
@RequestMapping("/api/v1/order-status")
public class OrderStatusController {

    private final OrderStatusService orderStatusService;

    /**
     * Constructs an OrderStatusController with the necessary service.
     * @param orderStatusService The service to handle order status operations.
     */
    @Autowired
    public OrderStatusController(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    /**
     * Retrieves all order statuses.
     * Note: This method calls `orderStatusService.getAllOrderStatutes()` which might be a typo in the service layer (statutes vs statuses).
     * @return A ResponseEntity containing a list of all order statuses and HTTP status OK.
     */
    @GetMapping()
    public ResponseEntity<List<OrderStatuses>> getAllOrderStatuses() {
        return orderStatusService.getAllOrderStatutes();
    }

    /**
     * Retrieves a specific order status by its ID.
     * @param id The ID of the order status to retrieve.
     * @return A ResponseEntity containing the order status and HTTP status OK,
     * or HTTP status NOT_FOUND if the order status does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderStatuses> getOrderStatusById(@PathVariable Long id) {
        return orderStatusService.getOrderStatusById(id);
    }

    /**
     * Creates a new order status.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param orderStatus The order status to create.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the created order status and HTTP status CREATED,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PostMapping()
    public ResponseEntity<?> createOrderStatus(@Valid @RequestBody OrderStatuses orderStatus, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return orderStatusService.createOrderStatus(orderStatus);
    }

    /**
     * Updates an existing order status.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param id The ID of the order status to update.
     * @param orderStatus The updated order status information.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the updated order status and HTTP status OK,
     * or HTTP status NOT_FOUND if the order status does not exist,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> editOrderStatusById(@PathVariable Long id, @Valid @RequestBody OrderStatuses orderStatus, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return orderStatusService.editOrderStatusById(id, orderStatus);
    }

    /**
     * Deletes a specific order status by its ID.
     * @param id The ID of the order status to delete.
     * @return A ResponseEntity with HTTP status NO_CONTENT if successful,
     * or HTTP status NOT_FOUND if the order status does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderStatus(@PathVariable Long id) { // Removed @Valid
        return orderStatusService.deleteOrderStatusById(id);
    }
}
