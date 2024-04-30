package dev.andreasgeorgatos.tsilikos.service.payment;

import dev.andreasgeorgatos.tsilikos.model.payment.PaymentMethod;
import dev.andreasgeorgatos.tsilikos.repository.payments.PaymentMethodRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    @Autowired
    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public ResponseEntity<List<PaymentMethod>> getAllPaymentMethods() {
        List<PaymentMethod> methods = paymentMethodRepository.findAll();

        if (methods.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(methods);
    }

    public ResponseEntity<PaymentMethod> getPaymentMethodById(long id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);

        return paymentMethod.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<PaymentMethod> createPaymentMethod(PaymentMethod paymentMethod) {
        return ResponseEntity.ok(paymentMethodRepository.save(paymentMethod));
    }

    @Transactional
    public ResponseEntity<PaymentMethod> editPaymentMethod(long id, String newType) {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);

        if (paymentMethod.isPresent()) {
            PaymentMethod method = paymentMethod.get();

            method.setMethod(newType);
            paymentMethodRepository.save(method);

            return ResponseEntity.ok(method);
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<PaymentMethod> deletePaymentMethodById(long id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);

        if (paymentMethod.isPresent()) {
            paymentMethodRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
