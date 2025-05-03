package dev.andreasgeorgatos.pointofservice.controller.payment;

import dev.andreasgeorgatos.pointofservice.model.payment.PaymentMethod;
import dev.andreasgeorgatos.pointofservice.service.payment.PaymentMethodService;
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
@RequestMapping("/api/v1/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @Autowired
    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping()
    public ResponseEntity<List<PaymentMethod>> getAllPaymentMethods() {
        return paymentMethodService.getAllPaymentMethods();
    }

    @PostMapping()
    public ResponseEntity<?> createPaymentMethod(@Valid @RequestBody PaymentMethod paymentMethod, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return paymentMethodService.createPaymentMethod(paymentMethod);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethod> getPaymentMethodById(@PathVariable Long id) {
        return paymentMethodService.getPaymentMethodById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editPaymentMethodById(@Valid @PathVariable Long id, @RequestBody PaymentMethod paymentMethod, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }


        return paymentMethodService.editPaymentMethod(id, paymentMethod.getMethod());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PaymentMethod> deletePaymentMethodById(@PathVariable Long id) {
        return paymentMethodService.deletePaymentMethodById(id);
    }

}
