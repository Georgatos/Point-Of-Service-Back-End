package dev.andreasgeorgatos.pointofservice.controller.payment;

import dev.andreasgeorgatos.pointofservice.model.payment.PaymentMethod;
import dev.andreasgeorgatos.pointofservice.service.payment.PaymentMethodService;
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
 * REST controller for managing payment methods.
 * Provides endpoints for CRUD operations on payment methods.
 */
@RestController // Changed from @Controller
@RequestMapping("/api/v1/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    /**
     * Constructs a PaymentMethodController with the necessary service.
     * @param paymentMethodService The service to handle payment method operations.
     */
    @Autowired
    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    /**
     * Retrieves all payment methods.
     * @return A ResponseEntity containing a list of all payment methods and HTTP status OK.
     */
    @GetMapping()
    public ResponseEntity<List<PaymentMethod>> getAllPaymentMethods() {
        return paymentMethodService.getAllPaymentMethods();
    }

    /**
     * Retrieves a specific payment method by its ID.
     * @param id The ID of the payment method to retrieve.
     * @return A ResponseEntity containing the payment method and HTTP status OK,
     * or HTTP status NOT_FOUND if the payment method does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethod> getPaymentMethodById(@PathVariable Long id) {
        return paymentMethodService.getPaymentMethodById(id);
    }

    /**
     * Creates a new payment method.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param paymentMethod The payment method to create.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the created payment method and HTTP status CREATED,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PostMapping()
    public ResponseEntity<?> createPaymentMethod(@Valid @RequestBody PaymentMethod paymentMethod, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return paymentMethodService.createPaymentMethod(paymentMethod);
    }

    /**
     * Updates an existing payment method.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * Note: The current service call `paymentMethodService.editPaymentMethod(id, paymentMethod.getMethod())`
     * suggests that only the 'method' string (e.g., "Credit Card", "Cash") from the PaymentMethod object
     * might be getting processed by the service layer, rather than the entire PaymentMethod object.
     * @param id The ID of the payment method to update.
     * @param paymentMethod The updated payment method information.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the updated payment method and HTTP status OK,
     * or HTTP status NOT_FOUND if the payment method does not exist,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> editPaymentMethodById(@PathVariable Long id, @Valid @RequestBody PaymentMethod paymentMethod, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        // Service call as per original, with Javadoc note highlighting its specific nature
        return paymentMethodService.editPaymentMethod(id, paymentMethod.getMethod());
    }

    /**
     * Deletes a specific payment method by its ID.
     * Note: The return type `ResponseEntity<PaymentMethod>` for a delete operation is unconventional.
     * Typically, it would be `ResponseEntity<Void>` or `ResponseEntity<?>` with `HttpStatus.NO_CONTENT`.
     * @param id The ID of the payment method to delete.
     * @return A ResponseEntity usually indicating success or failure (e.g., the deleted payment method or no content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<PaymentMethod> deletePaymentMethodById(@PathVariable Long id) {
        return paymentMethodService.deletePaymentMethodById(id);
    }
}
