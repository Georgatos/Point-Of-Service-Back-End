package dev.andreasgeorgatos.pointofservice.controller.orders;

import dev.andreasgeorgatos.pointofservice.model.order.Order;
import dev.andreasgeorgatos.pointofservice.service.order.OrderService;
import dev.andreasgeorgatos.pointofservice.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing orders.
 * Provides endpoints for CRUD operations on orders.
 */
@RestController
@RequestMapping("/api/v1/orders") // Changed RequestMapping
public class OrderController {

    private final OrderService orderService;

    /**
     * Constructs an OrderController with the necessary service.
     * @param orderService The service to handle order operations.
     */
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Retrieves all orders.
     * @return A ResponseEntity containing a list of all orders and HTTP status OK.
     */
    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrders() {
        return orderService.getAllOrders();
    }

    /**
     * Retrieves a specific order by its ID.
     * @param id The ID of the order to retrieve.
     * @return A ResponseEntity containing the order and HTTP status OK,
     * or HTTP status NOT_FOUND if the order does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    /**
     * Creates a new order.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param order The order to create.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the created order and HTTP status CREATED,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PostMapping()
    public ResponseEntity<?> createOrder(@Valid @RequestBody Order order, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return orderService.createOrder(order);
    }

    /**
     * Updates an existing order.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param id The ID of the order to update.
     * @param order The updated order information.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the updated order and HTTP status OK,
     * or HTTP status NOT_FOUND if the order does not exist,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> editOrderById(@PathVariable Long id, @Valid @RequestBody Order order, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return orderService.editOrderById(id, order);
    }

    /**
     * Deletes a specific order by its ID.
     * @param id The ID of the order to delete.
     * @return A ResponseEntity with HTTP status NO_CONTENT if successful,
     * or HTTP status NOT_FOUND if the order does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) { // Removed @Valid
        return orderService.deleteOrderById(id);
    }
}