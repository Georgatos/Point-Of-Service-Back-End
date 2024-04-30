package dev.andreasgeorgatos.tsilikos.repository.payments;

import dev.andreasgeorgatos.tsilikos.model.payment.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
}
