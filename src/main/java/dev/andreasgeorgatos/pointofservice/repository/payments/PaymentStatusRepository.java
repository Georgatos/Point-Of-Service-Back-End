package dev.andreasgeorgatos.pointofservice.repository.payments;

import dev.andreasgeorgatos.pointofservice.model.payment.PaymentStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatuses, Long> {
}
