package dev.andreasgeorgatos.pointofservice.service.payment;

import dev.andreasgeorgatos.pointofservice.model.payment.PaymentStatuses;
import dev.andreasgeorgatos.pointofservice.repository.payments.PaymentStatusRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentStatusService {

    private final PaymentStatusRepository paymentStatusRepository;

    @Autowired
    public PaymentStatusService(PaymentStatusRepository paymentStatusRepository) {
        this.paymentStatusRepository = paymentStatusRepository;
    }

    public ResponseEntity<List<PaymentStatuses>> getAllPaymentStatuses() {
        List<PaymentStatuses> paymentStatuses = paymentStatusRepository.findAll();

        if (paymentStatuses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(paymentStatuses);
    }

    public ResponseEntity<PaymentStatuses> getPaymentStatusById(long id) {
        Optional<PaymentStatuses> paymentStatus = paymentStatusRepository.findById(id);

        return paymentStatus.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<PaymentStatuses> createPaymentStatus(PaymentStatuses paymentStatus) {
        PaymentStatuses savedStatus = paymentStatusRepository.save(paymentStatus);

        return ResponseEntity.ok(savedStatus);
    }

    @Transactional
    public ResponseEntity<PaymentStatuses> editPaymentStatusById(Long id, PaymentStatuses paymentStatus) {
        Optional<PaymentStatuses> status = paymentStatusRepository.findById(id);

        if (status.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PaymentStatuses found = status.get();
        found.setStatus(paymentStatus.getStatus());

        paymentStatusRepository.save(found);
        return ResponseEntity.ok(found);
    }

    @Transactional
    public ResponseEntity<PaymentStatuses> deletePaymentStatusById(long id) {
        Optional<PaymentStatuses> status = paymentStatusRepository.findById(id);

        if (status.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        paymentStatusRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
