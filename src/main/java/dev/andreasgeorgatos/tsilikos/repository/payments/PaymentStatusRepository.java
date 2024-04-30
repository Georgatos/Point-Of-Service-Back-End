package dev.andreasgeorgatos.tsilikos.repository.payments;

import dev.andreasgeorgatos.tsilikos.model.payment.PaymentStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatuses, Long> {
}
