package dev.andreasgeorgatos.pointofservice.service.rewards;

import dev.andreasgeorgatos.pointofservice.model.rewards.PointsTransaction;
import dev.andreasgeorgatos.pointofservice.repository.rewards.PointsTransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PointsTransactionService {

    private final PointsTransactionRepository pointsTransactionRepository;

    public PointsTransactionService(PointsTransactionRepository pointsTransactionRepository) {
        this.pointsTransactionRepository = pointsTransactionRepository;
    }

    public ResponseEntity<List<PointsTransaction>> getAllTransactions() {
        List<PointsTransaction> transactions = pointsTransactionRepository.findAll();

        if (transactions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactions);
    }

    public ResponseEntity<PointsTransaction> getTransactionById(long id) {
        Optional<PointsTransaction> transaction = pointsTransactionRepository.findById(id);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<PointsTransaction> createTransaction(PointsTransaction transaction) {
        return ResponseEntity.ok(pointsTransactionRepository.save(transaction));
    }

    public ResponseEntity<PointsTransaction> editTransactionById(long id, PointsTransaction transaction) {
        Optional<PointsTransaction> transactionOptional = pointsTransactionRepository.findById(id);

        if (transactionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PointsTransaction pointsTransaction = transactionOptional.get();

        pointsTransaction.setPoints(transaction.getPoints());
        pointsTransaction.setOrderId(transaction.getOrderId());
        pointsTransaction.setTransactionType(transaction.getTransactionType());
        pointsTransaction.setDate(transaction.getDate());

        return ResponseEntity.ok(pointsTransactionRepository.save(pointsTransaction));
    }

    public ResponseEntity<PointsTransaction> deleteTransactionById(long id) {
        Optional<PointsTransaction> transaction = pointsTransactionRepository.findById(id);

        if (transaction.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pointsTransactionRepository.delete(transaction.get());
        return ResponseEntity.ok().build();
    }
}
