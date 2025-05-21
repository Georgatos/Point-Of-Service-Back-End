package dev.andreasgeorgatos.pointofservice.controller.payment;

import dev.andreasgeorgatos.pointofservice.model.payment.Payment;
import dev.andreasgeorgatos.pointofservice.service.payment.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import dev.andreasgeorgatos.pointofservice.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing payments.
 * Provides endpoints for CRUD operations on payments.
 */
@RestController
@RequestMapping("/api/v1/payments") // Changed RequestMapping
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Constructs a PaymentController with the necessary service.
     * @param paymentService The service to handle payment operations.
     */
    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Retrieves all payments.
     * @return A ResponseEntity containing a list of all payments and HTTP status OK.
     */
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return paymentService.getAllPayments();
    }

    /**
     * Retrieves a specific payment by its ID.
     * @param id The ID of the payment to retrieve.
     * @return A ResponseEntity containing the payment and HTTP status OK,
     * or HTTP status NOT_FOUND if the payment does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    /**
     * Creates a new payment.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param payment The payment to create.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the created payment and HTTP status CREATED,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PostMapping
    public ResponseEntity<?> createPayment(@Valid @RequestBody Payment payment, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return paymentService.createPayment(payment);
    }

    /**
     * Updates an existing payment.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param id The ID of the payment to update.
     * @param payment The updated payment information.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the updated payment and HTTP status OK,
     * or HTTP status NOT_FOUND if the payment does not exist,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> editPaymentById(@Valid @PathVariable Long id, @RequestBody Payment payment, BindingResult bindingResult) { // Renamed method
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return paymentService.editPaymentById(id, payment);
    }

    /**
     * Deletes a specific payment by its ID.
     * Note: The return type `ResponseEntity<Payment>` for a delete operation is unconventional.
     * Typically, it would be `ResponseEntity<Void>` or `ResponseEntity<?>` with `HttpStatus.NO_CONTENT`.
     * @param id The ID of the payment to delete.
     * @return A ResponseEntity usually indicating success or failure (e.g., the deleted payment or no content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Payment> deletePaymentById(@PathVariable Long id) {
        return paymentService.deletePaymentById(id);
    }
}
