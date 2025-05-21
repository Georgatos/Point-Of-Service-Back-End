package dev.andreasgeorgatos.pointofservice.controller.orders;

import dev.andreasgeorgatos.pointofservice.dto.tables.TableNumberDTO;
import dev.andreasgeorgatos.pointofservice.model.order.DineInTable;
import dev.andreasgeorgatos.pointofservice.service.order.DineInTableService;
import dev.andreasgeorgatos.pointofservice.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing dine-in tables.
 * Provides endpoints for CRUD operations on dine-in tables,
 * as well as specific operations like finding by table number.
 */
@RestController
@RequestMapping("/api/v1/dine-in-tables")
public class DineInTableController {

    private final DineInTableService dineInTableService;

    /**
     * Constructs a DineInTableController with the necessary service.
     * @param dineInTableService The service to handle dine-in table operations.
     */
    @Autowired
    public DineInTableController(DineInTableService dineInTableService) {
        this.dineInTableService = dineInTableService;
    }

    /**
     * Retrieves all dine-in tables.
     * @return A ResponseEntity containing a list of all dine-in tables and HTTP status OK.
     */
    @GetMapping()
    public ResponseEntity<List<DineInTable>> getAllDineInTables() {
        return dineInTableService.getAllDineInTables();
    }

    /**
     * Retrieves a specific dine-in table by its ID.
     * @param id The ID of the dine-in table to retrieve.
     * @return A ResponseEntity containing the dine-in table and HTTP status OK,
     * or HTTP status NOT_FOUND if the table does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DineInTable> getDineInTableById(@PathVariable Long id) {
        return dineInTableService.getDineInTableById(id);
    }

    /**
     * Retrieves a specific dine-in table by its table number.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param tableNumberDTO DTO containing the table number.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the dine-in table and HTTP status OK,
     * or HTTP status NOT_FOUND if the table does not exist,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PostMapping("/by-table-number") // Changed path for clarity
    public ResponseEntity<?> getDineInTableByNumber(@Valid @RequestBody TableNumberDTO tableNumberDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return dineInTableService.getDineInTableByTableNumber(tableNumberDTO.getTableNumber());
    }

    /**
     * Creates a new dine-in table.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param table The dine-in table to create.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the created dine-in table and HTTP status CREATED,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PostMapping()
    public ResponseEntity<?> createDineInTable(@Valid @RequestBody DineInTable table, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return dineInTableService.createDineInTable(table);
    }

    /**
     * Updates an existing dine-in table.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param id The ID of the dine-in table to update.
     * @param dineInTable The updated dine-in table information.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the updated dine-in table and HTTP status OK,
     * or HTTP status NOT_FOUND if the table does not exist,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> editDineInTableById(@PathVariable Long id, @Valid @RequestBody DineInTable dineInTable, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        // Assuming dineInTableService has a method like editDineInTableById or a generic update method
        return dineInTableService.editDineInTableById(id, dineInTable);
    }

    /**
     * Deletes a dine-in table by its table number.
     * This is a POST request to allow sending a request body with the table number.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param tableNumberDTO DTO containing the table number.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity with HTTP status NO_CONTENT if successful,
     * or HTTP status NOT_FOUND if the table does not exist,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PostMapping("/delete-by-table-number") // Changed path for clarity and RESTfulness
    public ResponseEntity<?> deleteDineInTableByTableNumber(@Valid @RequestBody TableNumberDTO tableNumberDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        // It's generally better for the service layer to handle the logic of fetching by number then deleting by ID.
        // However, sticking to the original structure for now:
        ResponseEntity<DineInTable> tableResponse = dineInTableService.getDineInTableByTableNumber(tableNumberDTO.getTableNumber());
        if (tableResponse.getStatusCode().is4xxClientError() || tableResponse.getBody() == null) {
             return ResponseEntity.notFound().build(); // Or forward the status from tableResponse
        }
        return dineInTableService.deleteDineInTableById(tableResponse.getBody().getId());
    }

    /**
     * Deletes a specific dine-in table by its ID.
     * @param id The ID of the dine-in table to delete.
     * @return A ResponseEntity with HTTP status NO_CONTENT if successful,
     * or HTTP status NOT_FOUND if the table does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDineInTableById(@PathVariable Long id) {
        return dineInTableService.deleteDineInTableById(id);
    }
}
