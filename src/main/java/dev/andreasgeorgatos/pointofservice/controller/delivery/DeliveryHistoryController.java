package dev.andreasgeorgatos.pointofservice.controller.delivery;

import dev.andreasgeorgatos.pointofservice.model.delivery.DeliveryHistory;
import dev.andreasgeorgatos.pointofservice.service.delivery.DeliveryHistoryService;
import dev.andreasgeorgatos.pointofservice.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing delivery history records.
 * Provides endpoints for CRUD operations on delivery history.
 */
@RestController
@RequestMapping("/api/v1/delivery-history")
public class DeliveryHistoryController {

    private final DeliveryHistoryService deliveryHistoryService;

    /**
     * Constructs a DeliveryHistoryController with the necessary service.
     * @param deliveryHistoryService The service to handle delivery history operations.
     */
    public DeliveryHistoryController(DeliveryHistoryService deliveryHistoryService) {
        this.deliveryHistoryService = deliveryHistoryService;
    }

    /**
     * Retrieves all delivery history records.
     * @return A ResponseEntity containing a list of all delivery histories and HTTP status OK.
     */
    @GetMapping()
    public ResponseEntity<List<DeliveryHistory>> getAllDeliveryHistories() {
        return deliveryHistoryService.getAllDeliveryHistories();
    }

    /**
     * Retrieves a specific delivery history record by its ID.
     * @param id The ID of the delivery history record to retrieve.
     * @return A ResponseEntity containing the delivery history record and HTTP status OK,
     * or HTTP status NOT_FOUND if the record does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryHistory> getDeliveryHistoryById(@PathVariable Long id) {
        return deliveryHistoryService.getDeliveryHistoryById(id);
    }

    /**
     * Creates a new delivery history record.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param deliveryHistory The delivery history record to create.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the created delivery history record and HTTP status CREATED,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PostMapping()
    public ResponseEntity<?> createDeliveryHistory(@Valid @RequestBody DeliveryHistory deliveryHistory, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return deliveryHistoryService.createDeliveryHistory(deliveryHistory);
    }

    /**
     * Updates an existing delivery history record.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param id The ID of the delivery history record to update.
     * @param deliveryHistory The updated delivery history information.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the updated delivery history record and HTTP status OK,
     * or HTTP status NOT_FOUND if the record does not exist,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> editDeliveryHistoryById(@PathVariable Long id, @Valid @RequestBody DeliveryHistory deliveryHistory, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return deliveryHistoryService.editDeliveryHistoryById(id, deliveryHistory);
    }

    /**
     * Deletes a specific delivery history record by its ID.
     * @param id The ID of the delivery history record to delete.
     * @return A ResponseEntity with HTTP status NO_CONTENT if successful,
     * or HTTP status NOT_FOUND if the record does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDeliveryHistory(@PathVariable Long id) {
        return deliveryHistoryService.deleteDeliveryHistory(id);
    }

}
