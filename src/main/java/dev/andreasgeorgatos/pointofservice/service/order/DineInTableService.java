package dev.andreasgeorgatos.pointofservice.service.order;

import dev.andreasgeorgatos.pointofservice.model.order.DineInTable;
import dev.andreasgeorgatos.pointofservice.repository.orders.DineInTableRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DineInTableService {

    private final DineInTableRepository dineInTableRepository;

    @Autowired
    public DineInTableService(DineInTableRepository dineInTableRepository) {
        this.dineInTableRepository = dineInTableRepository;
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
    public ResponseEntity<DineInTable> createDineInTable(DineInTable dineInTable) {
        return ResponseEntity.ok(dineInTableRepository.save(dineInTable));
    }

    @Transactional
    public ResponseEntity<DineInTable> editOrderHistoryById(long id, DineInTable dineInTable) {
        Optional<DineInTable> optionalDineInTable = dineInTableRepository.findById(id);

        if (optionalDineInTable.isPresent()) {
            DineInTable oldDineInTable = optionalDineInTable.get();

            oldDineInTable.setTableNumber(dineInTable.getTableNumber());
            oldDineInTable.setCreatedAt(dineInTable.getCreatedAt());
            oldDineInTable.setOrder_item_id(dineInTable.getOrder_item_id());
            oldDineInTable.setUpdatedAt(dineInTable.getUpdatedAt());
            oldDineInTable.setCreatedAt(dineInTable.getCreatedAt());

            DineInTable savedOrderHistory = dineInTableRepository.save(oldDineInTable);

            return ResponseEntity.ok(savedOrderHistory);
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<DineInTable> deleteDineInTable(long id) {
        Optional<DineInTable> dineInTable = dineInTableRepository.findById(id);

        if (dineInTable.isPresent()) {
            dineInTableRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
