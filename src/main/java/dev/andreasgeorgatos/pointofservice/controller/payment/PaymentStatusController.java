package dev.andreasgeorgatos.pointofservice.controller.payment;

import dev.andreasgeorgatos.pointofservice.model.payment.PaymentStatuses;
import dev.andreasgeorgatos.pointofservice.service.payment.PaymentStatusService;
import dev.andreasgeorgatos.pointofservice.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/payment-status")
public class PaymentStatusController {
    private final PaymentStatusService paymentStatusService;

    @Autowired
    public PaymentStatusController(PaymentStatusService paymentStatusService) {
        this.paymentStatusService = paymentStatusService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentStatuses>> getAllPaymentStatus() {
        return paymentStatusService.getAllPaymentStatuses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentStatuses> getPaymentStatusById(@PathVariable Long id) {
        return paymentStatusService.getPaymentStatusById(id);
    }

    @PostMapping
    public ResponseEntity<?> createPaymentMethod(@Valid @RequestBody PaymentStatuses paymentStatus, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return paymentStatusService.createPaymentStatus(paymentStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editPaymentMethod(@Valid @PathVariable Long id, @RequestBody PaymentStatuses paymentStatus, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }
        return paymentStatusService.editPaymentStatusById(id, paymentStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PaymentStatuses> deletePaymentMethod(@PathVariable Long id) {
        return paymentStatusService.deletePaymentStatusById(id);
    }
}
