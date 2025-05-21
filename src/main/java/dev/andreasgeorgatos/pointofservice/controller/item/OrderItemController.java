package dev.andreasgeorgatos.pointofservice.controller.item;

import dev.andreasgeorgatos.pointofservice.model.item.OrderItem;
import dev.andreasgeorgatos.pointofservice.service.order.OrderItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import dev.andreasgeorgatos.pointofservice.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing order items.
 * Provides endpoints for CRUD operations on order items.
 */
@RestController
@RequestMapping("/api/v1/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    /**
     * Constructs an OrderItemController with the necessary service.
     * @param orderItemService The service to handle order item operations.
     */
    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    /**
     * Retrieves all order items.
     * @return A ResponseEntity containing a list of all order items and HTTP status OK.
     */
    @GetMapping()
    public ResponseEntity<List<OrderItem>> getAllOrderItems() {
        return orderItemService.getAllOrderItems();
    }

    /**
     * Retrieves a specific order item by its ID.
     * @param id The ID of the order item to retrieve.
     * @return A ResponseEntity containing the order item and HTTP status OK,
     * or HTTP status NOT_FOUND if the item does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long id) {
        return orderItemService.getOrderItemById(id);
    }

    /**
     * Creates a new order item.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param orderItem The order item to create.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the created order item and HTTP status CREATED,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PostMapping()
    public ResponseEntity<?> createOrderItem(@Valid @RequestBody OrderItem orderItem, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return orderItemService.createOrderItem(orderItem);
    }

    /**
     * Updates an existing order item.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param id The ID of the order item to update.
     * @param orderItem The updated order item information.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the updated order item and HTTP status OK,
     * or HTTP status NOT_FOUND if the item does not exist,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> editOrderItemById(@PathVariable Long id, @Valid @RequestBody OrderItem orderItem, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return orderItemService.editOrderItemById(id, orderItem);
    }

    /**
     * Deletes a specific order item by its ID.
     * @param id The ID of the order item to delete.
     * @return A ResponseEntity with HTTP status NO_CONTENT if successful,
     * or HTTP status NOT_FOUND if the item does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderItemById(@PathVariable Long id) {
        return orderItemService.deleteOrderItemById(id);
    }
}
