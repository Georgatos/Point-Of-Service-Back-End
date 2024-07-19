package dev.andreasgeorgatos.pointofservice.repository.payments;

import dev.andreasgeorgatos.pointofservice.model.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
