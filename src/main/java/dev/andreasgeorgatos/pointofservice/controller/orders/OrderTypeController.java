package dev.andreasgeorgatos.pointofservice.controller.orders;

import dev.andreasgeorgatos.pointofservice.model.order.OrderType;
import dev.andreasgeorgatos.pointofservice.service.order.OrderTypeService;
import dev.andreasgeorgatos.pointofservice.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing order types.
 * Provides endpoints for CRUD operations on order types.
 */
@RestController
@RequestMapping("/api/v1/order-types")
public class OrderTypeController {

    private final OrderTypeService orderTypeService;

    /**
     * Constructs an OrderTypeController with the necessary service.
     * @param orderTypeService The service to handle order type operations.
     */
    @Autowired
    public OrderTypeController(OrderTypeService orderTypeService) {
        this.orderTypeService = orderTypeService;
    }

    /**
     * Retrieves all order types.
     * @return A ResponseEntity containing a list of all order types and HTTP status OK.
     */
    @GetMapping()
    public ResponseEntity<List<OrderType>> getAllOrderTypes() { // Renamed method
        return orderTypeService.getAllOrderTypes();
    }

    /**
     * Retrieves a specific order type by its ID.
     * @param id The ID of the order type to retrieve.
     * @return A ResponseEntity containing the order type and HTTP status OK,
     * or HTTP status NOT_FOUND if the order type does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderType> getOrderTypeById(@PathVariable Long id) { // Renamed method
        return orderTypeService.getOrderTypeById(id);
    }

    /**
     * Creates a new order type.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param orderType The order type to create.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the created order type and HTTP status CREATED,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PostMapping()
    public ResponseEntity<?> createOrderType(@Valid @RequestBody OrderType orderType, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return orderTypeService.createOrderType(orderType);
    }

    /**
     * Updates an existing order type.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param id The ID of the order type to update.
     * @param orderType The updated order type information.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the updated order type and HTTP status OK,
     * or HTTP status NOT_FOUND if the order type does not exist,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> editOrderTypeById(@PathVariable Long id, @Valid @RequestBody OrderType orderType, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return orderTypeService.editOrderTypeById(id, orderType);
    }

    /**
     * Deletes a specific order type by its ID.
     * @param id The ID of the order type to delete.
     * @return A ResponseEntity with HTTP status NO_CONTENT if successful,
     * or HTTP status NOT_FOUND if the order type does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderType(@PathVariable Long id) { // Removed @Valid
        return orderTypeService.deleteOrderTypeById(id);
    }
}
