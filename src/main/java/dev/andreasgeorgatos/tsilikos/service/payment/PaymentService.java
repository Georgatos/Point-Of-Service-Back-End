package dev.andreasgeorgatos.tsilikos.service.payment;

import dev.andreasgeorgatos.tsilikos.model.order.Order;
import dev.andreasgeorgatos.tsilikos.model.payment.Payment;
import dev.andreasgeorgatos.tsilikos.model.payment.PaymentMethod;
import dev.andreasgeorgatos.tsilikos.model.payment.PaymentStatuses;
import dev.andreasgeorgatos.tsilikos.repository.orders.OrderRepository;
import dev.andreasgeorgatos.tsilikos.repository.payments.PaymentMethodRepository;
import dev.andreasgeorgatos.tsilikos.repository.payments.PaymentRepository;
import dev.andreasgeorgatos.tsilikos.repository.payments.PaymentStatusRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, PaymentStatusRepository paymentStatusRepository, PaymentMethodRepository paymentMethodRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentStatusRepository = paymentStatusRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.orderRepository = orderRepository;
    }

    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();

        if (payments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(payments);
    }

    public ResponseEntity<Payment> getPaymentById(long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        return payment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<Payment> createPayment(Payment payment) {
        Optional<PaymentStatuses> optionalPaymentStatus = paymentStatusRepository.findById(payment.getPaymentStatus().getId());
        Optional<PaymentMethod> optionalPaymentMethod = paymentMethodRepository.findById(payment.getPaymentMethod().getId());
        Optional<Order> orderOptional = orderRepository.findById(payment.getOrder().getId());

        if (optionalPaymentStatus.isEmpty() || optionalPaymentMethod.isEmpty() || orderOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        payment.setPaymentStatus(optionalPaymentStatus.get());
        payment.setPaymentMethod(optionalPaymentMethod.get());
        payment.setOrder(orderOptional.get());

        Payment savedPayment = paymentRepository.save(payment);

        return ResponseEntity.ok(savedPayment);
    }

    @Transactional
    public ResponseEntity<Payment> editPaymentById(long id, Payment payment) {
        Optional<Payment> findPayment = paymentRepository.findById(id);

        if (findPayment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Payment oldPayment = findPayment.get();

        oldPayment.setPaymentDate(payment.getPaymentDate());
        oldPayment.setPaymentStatus(payment.getPaymentStatus());
        oldPayment.setId(payment.getId());
        oldPayment.setAmount(payment.getAmount());

        Payment savedPayment = paymentRepository.save(oldPayment);

        return ResponseEntity.ok(savedPayment);
    }

    @Transactional
    public ResponseEntity<Payment> deletePaymentById(long id) {
        Optional<Payment> findPayment = paymentRepository.findById(id);

        if (findPayment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

}
