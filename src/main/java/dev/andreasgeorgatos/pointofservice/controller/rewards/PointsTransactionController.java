package dev.andreasgeorgatos.pointofservice.controller.rewards;

import dev.andreasgeorgatos.pointofservice.model.rewards.PointsTransaction;
import dev.andreasgeorgatos.pointofservice.service.rewards.PointsTransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/points-transaction")
public class PointsTransactionController {

    private final PointsTransactionService pointsTransactionService;

    @Autowired
    public PointsTransactionController(PointsTransactionService pointsTransactionService) {
        this.pointsTransactionService = pointsTransactionService;
    }

    @GetMapping
    public ResponseEntity<List<PointsTransaction>> getAllPointsUsed() {
        return pointsTransactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PointsTransaction> getPointsUsedById(@PathVariable Long id) {
        return pointsTransactionService.getTransactionById(id);
    }

    @PostMapping
    public ResponseEntity<?> createPointsUsed(@Valid @RequestBody PointsTransaction pointsTransaction, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return pointsTransactionService.createTransaction(pointsTransaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editPointsUsed(@Valid @PathVariable Long id, @RequestBody PointsTransaction pointsTransaction, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return pointsTransactionService.editTransactionById(id, pointsTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PointsTransaction> deletePointsUsedById(@PathVariable Long id) {
        return pointsTransactionService.deleteTransactionById(id);
    }

}
