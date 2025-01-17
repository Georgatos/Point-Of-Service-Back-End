package dev.andreasgeorgatos.pointofservice.service.order;

import dev.andreasgeorgatos.pointofservice.enums.TableStatus;
import dev.andreasgeorgatos.pointofservice.model.order.DineInTable;
import dev.andreasgeorgatos.pointofservice.repository.orders.DineInTableRepository;
import dev.andreasgeorgatos.pointofservice.service.user.ReviewService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DineInTableService {

    private final DineInTableRepository dineInTableRepository;
    private final ReviewService reviewService;

    @Autowired
    public DineInTableService(DineInTableRepository dineInTableRepository, ReviewService reviewService) {
        this.dineInTableRepository = dineInTableRepository;
        this.reviewService = reviewService;
    }

    public ResponseEntity<List<DineInTable>> getAllDineInTables() {
        List<DineInTable> dineInTables = dineInTableRepository.findAll();

        if (!dineInTables.isEmpty()) {
            return ResponseEntity.ok(dineInTables);
        }
        return ResponseEntity.notFound().build();
    }


    public ResponseEntity<DineInTable> getDineInTableById(long id) {
        Optional<DineInTable> dineInTable = dineInTableRepository.findById(id);

        return dineInTable.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @Transactional
    public ResponseEntity<DineInTable> getDineInTableByTableNumber(long tableNumber) {
        Optional<DineInTable> optionalDineInTable = dineInTableRepository.getDineInTableByTableNumber(tableNumber);
        return optionalDineInTable.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @Transactional
    public ResponseEntity<DineInTable> createDineInTable(DineInTable dineInTable) {
        DineInTable table = new DineInTable();

        table.setCreatedAt(LocalDate.now());
        table.setUpdatedAt(LocalDate.now());

        try {
            TableStatus status = TableStatus.valueOf(dineInTable.getStatus().toString());
            table.setStatus(status);
        } catch (IllegalArgumentException | NullPointerException e) {
            table.setStatus(TableStatus.AVAILABLE);
        }

        table.setTableNumber(dineInTable.getTableNumber());
        return ResponseEntity.ok(dineInTableRepository.save(table));
    }

    @Transactional
    public ResponseEntity<DineInTable> editOrderHistoryById(long id, DineInTable dineInTable) {
        Optional<DineInTable> optionalDineInTable = dineInTableRepository.findById(id);

        if (optionalDineInTable.isPresent()) {
            DineInTable oldDineInTable = optionalDineInTable.get();

            oldDineInTable.setTableNumber(dineInTable.getTableNumber());
            oldDineInTable.setCreatedAt(dineInTable.getCreatedAt());
            oldDineInTable.setUpdatedAt(dineInTable.getUpdatedAt());
            oldDineInTable.setCreatedAt(dineInTable.getCreatedAt());

            DineInTable savedOrderHistory = dineInTableRepository.save(oldDineInTable);

            return ResponseEntity.ok(savedOrderHistory);
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<DineInTable> deleteDineInTableById(long id) {
        Optional<DineInTable> dineInTable = dineInTableRepository.findById(id);

        if (dineInTable.isPresent()) {
            dineInTableRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
