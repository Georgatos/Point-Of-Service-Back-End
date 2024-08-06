package dev.andreasgeorgatos.pointofservice.controller.orders;

import dev.andreasgeorgatos.pointofservice.model.order.DineInTable;
import dev.andreasgeorgatos.pointofservice.repository.orders.DineInTableRepository;
import dev.andreasgeorgatos.pointofservice.service.order.DineInTableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/order/DineIn")
public class DineInTableController {

    private final DineInTableService dineInTableService;
    private final DineInTableRepository dineInTableRepository;

    @Autowired
    public DineInTableController(DineInTableService dineInTableService, DineInTableRepository dineInTableRepository) {
        this.dineInTableService = dineInTableService;
        this.dineInTableRepository = dineInTableRepository;
    }

    @GetMapping()
    public ResponseEntity<List<DineInTable>> getAllDineInTables() {
        return dineInTableService.getAllDineInTables();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DineInTable> getDineInTableById(@PathVariable Long id) {
        return dineInTableService.getDineInTableById(id);
    }

    @GetMapping()
    public ResponseEntity<?> getDineInTableByNumber(@Valid @RequestBody Long tableNumber, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return dineInTableService.getDineInTableByTableNumber(tableNumber);
    }

    @PostMapping()
    public ResponseEntity<?> createDineInTable(@Valid @RequestBody DineInTable order, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return dineInTableService.createDineInTable(order);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editDineInTableById(@Valid @PathVariable Long id, @RequestBody DineInTable dineInTable, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }


        return dineInTableService.editOrderHistoryById(id, dineInTable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable Long id) {
        return dineInTableService.deleteDineInTable(id);
    }
}
