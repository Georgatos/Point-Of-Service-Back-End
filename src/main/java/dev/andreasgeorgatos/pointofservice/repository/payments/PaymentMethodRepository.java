package dev.andreasgeorgatos.pointofservice.repository.payments;

import dev.andreasgeorgatos.pointofservice.model.payment.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
}
