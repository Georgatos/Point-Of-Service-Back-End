package dev.andreasgeorgatos.pointofservice.controller.delivery;

import dev.andreasgeorgatos.pointofservice.model.delivery.DeliveryStatus;
import dev.andreasgeorgatos.pointofservice.service.delivery.DeliveryStatusService;
import dev.andreasgeorgatos.pointofservice.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing delivery status records.
 * Provides endpoints for CRUD operations on delivery statuses.
 */
@RestController
@RequestMapping("/api/v1/delivery-status")
public class DeliveryStatusController {
    private final DeliveryStatusService deliveryStatusService;

    /**
     * Constructs a DeliveryStatusController with the necessary service.
     * @param deliveryStatusService The service to handle delivery status operations.
     */
    public DeliveryStatusController(DeliveryStatusService deliveryStatusService) {
        this.deliveryStatusService = deliveryStatusService;
    }

    /**
     * Retrieves all delivery status records.
     * @return A ResponseEntity containing a list of all delivery statuses and HTTP status OK.
     */
    @GetMapping()
    public ResponseEntity<List<DeliveryStatus>> getAllDeliveryStatuses() {
        return deliveryStatusService.getAllDeliveryStatuses();
    }

    /**
     * Retrieves a specific delivery status record by its ID.
     * @param id The ID of the delivery status record to retrieve.
     * @return A ResponseEntity containing the delivery status record and HTTP status OK,
     * or HTTP status NOT_FOUND if the record does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryStatus> getDeliveryStatusById(@PathVariable Long id) {
        return deliveryStatusService.getDeliveryStatusById(id);
    }

    /**
     * Creates a new delivery status record.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param deliveryStatus The delivery status record to create.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the created delivery status record and HTTP status CREATED,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PostMapping()
    public ResponseEntity<?> createDeliveryStatus(@Valid @RequestBody DeliveryStatus deliveryStatus, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return deliveryStatusService.createDeliveryStatus(deliveryStatus);
    }

    /**
     * Updates an existing delivery status record.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param id The ID of the delivery status record to update.
     * @param deliveryStatus The updated delivery status information.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the updated delivery status record and HTTP status OK,
     * or HTTP status NOT_FOUND if the record does not exist,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> editDeliveryStatusById(@PathVariable Long id, @Valid @RequestBody DeliveryStatus deliveryStatus, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return deliveryStatusService.editDeliveryStatusById(id, deliveryStatus);
    }

    /**
     * Deletes a specific delivery status record by its ID.
     * @param id The ID of the delivery status record to delete.
     * @return A ResponseEntity with HTTP status NO_CONTENT if successful,
     * or HTTP status NOT_FOUND if the record does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDeliveryStatus(@PathVariable Long id) {
        return deliveryStatusService.deleteDeliveryStatus(id);
    }
}
