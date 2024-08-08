package dev.andreasgeorgatos.pointofservice.controller.orders;

import dev.andreasgeorgatos.pointofservice.dto.tables.TableNumberDTO;
import dev.andreasgeorgatos.pointofservice.model.order.DineInTable;
import dev.andreasgeorgatos.pointofservice.service.order.DineInTableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1/order/DineIn")
public class DineInTableController {

    private final DineInTableService dineInTableService;

    @Autowired
    public DineInTableController(DineInTableService dineInTableService) {
        this.dineInTableService = dineInTableService;
    }

    @GetMapping()
    public ResponseEntity<List<DineInTable>> getAllDineInTables() {
        return dineInTableService.getAllDineInTables();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DineInTable> getDineInTableById(@PathVariable Long id) {
        return dineInTableService.getDineInTableById(id);
    }

    @PostMapping("/getDineInTableByNumber")
    public ResponseEntity<?> getDineInTableByNumber(@Valid @RequestBody TableNumberDTO tableNumberDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return dineInTableService.getDineInTableByTableNumber(tableNumberDTO.getTableNumber());
    }

    @PostMapping()
    public ResponseEntity<?> createDineInTable(@Valid @RequestBody DineInTable table, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return dineInTableService.createDineInTable(table);
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

    @PostMapping("/deleteTableByNumber")
    public ResponseEntity<?> deleteDineInTableByTableNumber(@Valid @RequestBody TableNumberDTO tableNumberDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        DineInTable table = dineInTableService.getDineInTableByTableNumber(tableNumberDTO.getTableNumber()).getBody();

        if (table == null) {
            return ResponseEntity.notFound().build();
        }

        return dineInTableService.deleteDineInTableById(table.getId());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDineInTableById(@Valid @PathVariable Long id) {
        return dineInTableService.deleteDineInTableById(id);
    }
}
