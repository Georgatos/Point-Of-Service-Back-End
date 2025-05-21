package dev.andreasgeorgatos.pointofservice.controller.payment;

import dev.andreasgeorgatos.pointofservice.model.payment.PaymentStatuses;
import dev.andreasgeorgatos.pointofservice.service.payment.PaymentStatusService;
import dev.andreasgeorgatos.pointofservice.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*; // Ensure @RestController is imported

import java.util.List;

/**
 * REST controller for managing payment statuses.
 * Provides endpoints for CRUD operations on payment statuses.
 */
@RestController // Changed from @Controller
@RequestMapping("/api/v1/payment-status")
public class PaymentStatusController {
    private final PaymentStatusService paymentStatusService;

    /**
     * Constructs a PaymentStatusController with the necessary service.
     * @param paymentStatusService The service to handle payment status operations.
     */
    @Autowired
    public PaymentStatusController(PaymentStatusService paymentStatusService) {
        this.paymentStatusService = paymentStatusService;
    }

    /**
     * Retrieves all payment statuses.
     * @return A ResponseEntity containing a list of all payment statuses and HTTP status OK.
     */
    @GetMapping
    public ResponseEntity<List<PaymentStatuses>> getAllPaymentStatuses() { // Renamed method
        return paymentStatusService.getAllPaymentStatuses();
    }

    /**
     * Retrieves a specific payment status by its ID.
     * @param id The ID of the payment status to retrieve.
     * @return A ResponseEntity containing the payment status and HTTP status OK,
     * or HTTP status NOT_FOUND if the payment status does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentStatuses> getPaymentStatusById(@PathVariable Long id) {
        return paymentStatusService.getPaymentStatusById(id);
    }

    /**
     * Creates a new payment status.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param paymentStatus The payment status to create.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the created payment status and HTTP status CREATED,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PostMapping
    public ResponseEntity<?> createPaymentStatus(@Valid @RequestBody PaymentStatuses paymentStatus, BindingResult bindingResult) { // Renamed method
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return paymentStatusService.createPaymentStatus(paymentStatus);
    }

    /**
     * Updates an existing payment status.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param id The ID of the payment status to update.
     * @param paymentStatus The updated payment status information.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the updated payment status and HTTP status OK,
     * or HTTP status NOT_FOUND if the payment status does not exist,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> editPaymentStatusById(@PathVariable Long id, @Valid @RequestBody PaymentStatuses paymentStatus, BindingResult bindingResult) { // Renamed method
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return paymentStatusService.editPaymentStatusById(id, paymentStatus);
    }

    /**
     * Deletes a specific payment status by its ID.
     * Note: The return type `ResponseEntity<PaymentStatuses>` for a delete operation is unconventional.
     * Typically, it would be `ResponseEntity<Void>` or `ResponseEntity<?>` with `HttpStatus.NO_CONTENT`.
     * @param id The ID of the payment status to delete.
     * @return A ResponseEntity usually indicating success or failure (e.g., the deleted payment status or no content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<PaymentStatuses> deletePaymentStatusById(@PathVariable Long id) { // Renamed method
        return paymentStatusService.deletePaymentStatusById(id);
    }
}
