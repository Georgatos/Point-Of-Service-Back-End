package dev.andreasgeorgatos.pointofservice.service.order;

import dev.andreasgeorgatos.pointofservice.enums.TableStatus;
import dev.andreasgeorgatos.pointofservice.model.order.DineInTable;
import dev.andreasgeorgatos.pointofservice.repository.orders.DineInTableRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class that handles business logic for dine-in table operations.
 * This includes creating, retrieving, updating, and deleting dine-in tables.
 */
@Service
public class DineInTableService {

    private static final Logger logger = LoggerFactory.getLogger(DineInTableService.class);

    private final DineInTableRepository dineInTableRepository;

    /**
     * Constructs a {@code DineInTableService} with the specified {@link DineInTableRepository}.
     *
     * @param dineInTableRepository The repository for dine-in table data access.
     */
    @Autowired
    public DineInTableService(DineInTableRepository dineInTableRepository) {
        this.dineInTableRepository = dineInTableRepository;
    }

    /**
     * Retrieves all dine-in tables.
     *
     * @return A {@link ResponseEntity} containing a list of all {@link DineInTable} objects and HTTP status OK,
     *         or HTTP status NOT_FOUND if no tables exist.
     */
    public ResponseEntity<List<DineInTable>> getAllDineInTables() {
        logger.info("Fetching all dine-in tables");
        List<DineInTable> dineInTables = dineInTableRepository.findAll();

        if (!dineInTables.isEmpty()) {
            logger.debug("Found {} dine-in tables", dineInTables.size());
            return ResponseEntity.ok(dineInTables);
        }
        logger.info("No dine-in tables found");
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves a specific dine-in table by its ID.
     *
     * @param id The ID of the dine-in table to retrieve.
     * @return A {@link ResponseEntity} containing the {@link DineInTable} if found and HTTP status OK,
     *         or HTTP status NOT_FOUND if the table does not exist.
     */
    public ResponseEntity<DineInTable> getDineInTableById(long id) {
        logger.info("Fetching dine-in table with ID: {}", id);
        Optional<DineInTable> dineInTable = dineInTableRepository.findById(id);

        if (dineInTable.isPresent()) {
            logger.debug("Found dine-in table: {}", dineInTable.get());
            return ResponseEntity.ok(dineInTable.get());
        }
        logger.warn("Dine-in table with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves a specific dine-in table by its table number.
     *
     * @param tableNumber The number of the dine-in table to retrieve.
     * @return A {@link ResponseEntity} containing the {@link DineInTable} if found and HTTP status OK,
     *         or HTTP status NOT_FOUND if no table with the given number exists.
     */
    @Transactional
    public ResponseEntity<DineInTable> getDineInTableByTableNumber(long tableNumber) {
        logger.info("Fetching dine-in table by table number: {}", tableNumber);
        Optional<DineInTable> optionalDineInTable = dineInTableRepository.getDineInTableByTableNumber(tableNumber);
        if (optionalDineInTable.isPresent()) {
            logger.debug("Found dine-in table: {}", optionalDineInTable.get());
            return ResponseEntity.ok(optionalDineInTable.get());
        }
        logger.warn("Dine-in table with number: {} not found", tableNumber);
        return ResponseEntity.notFound().build();
    }

    /**
     * Creates a new dine-in table.
     * Sets the creation and update timestamps to the current date.
     * The table status defaults to {@link TableStatus#AVAILABLE} if the provided status in {@code dineInTableDetails} is invalid or null.
     *
     * @param dineInTableDetails The {@link DineInTable} object containing the details for the new table.
     *                           Only {@code tableNumber} and optionally {@code status} are used from this parameter.
     * @return A {@link ResponseEntity} containing the created {@link DineInTable} and HTTP status OK.
     */
    @Transactional
    public ResponseEntity<DineInTable> createDineInTable(DineInTable dineInTableDetails) {
        logger.info("Creating new dine-in table with input: {}", dineInTableDetails);
        DineInTable table = new DineInTable();

        table.setCreatedAt(LocalDate.now());
        table.setUpdatedAt(LocalDate.now());

        try {
            TableStatus status = TableStatus.valueOf(String.valueOf(dineInTableDetails.getStatus()));
            table.setStatus(status);
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.warn("Invalid or null status provided ('{}') for new table, defaulting to AVAILABLE.", dineInTableDetails.getStatus(), e);
            table.setStatus(TableStatus.AVAILABLE);
        }

        table.setTableNumber(dineInTableDetails.getTableNumber());
        DineInTable savedTable = dineInTableRepository.save(table);
        logger.info("Dine-in table created successfully with ID: {}", savedTable.getId());
        return ResponseEntity.ok(savedTable);
    }

   /**
    * Updates an existing dine-in table by its ID.
    * The {@code createdAt} field is not updated. The {@code updatedAt} field is set to the current date.
    *
    * @param id The ID of the dine-in table to edit.
    * @param dineInTableDetails The {@link DineInTable} object containing the new details for the table.
    * @return A {@link ResponseEntity} containing the updated {@link DineInTable} and HTTP status OK if successful,
    *         or HTTP status NOT_FOUND if the table with the given ID does not exist.
    */
   @Transactional
   public ResponseEntity<DineInTable> editDineInTableById(long id, DineInTable dineInTableDetails) {
       logger.info("Editing dine-in table with ID: {}, new details: {}", id, dineInTableDetails);
       Optional<DineInTable> optionalDineInTable = dineInTableRepository.findById(id);

       if (optionalDineInTable.isPresent()) {
           DineInTable oldDineInTable = optionalDineInTable.get();

           oldDineInTable.setTableNumber(dineInTableDetails.getTableNumber());
           oldDineInTable.setUpdatedAt(LocalDate.now()); 
           oldDineInTable.setStatus(dineInTableDetails.getStatus());

           DineInTable savedDineInTable = dineInTableRepository.save(oldDineInTable);
           logger.info("Dine-in table with ID: {} updated successfully", savedDineInTable.getId());
           return ResponseEntity.ok(savedDineInTable);
       }
       logger.warn("Failed to edit. Dine-in table with ID: {} not found", id);
       return ResponseEntity.notFound().build();
   }

    /**
     * Deletes a dine-in table by its ID.
     *
     * @param id The ID of the dine-in table to delete.
     * @return A {@link ResponseEntity} with HTTP status NO_CONTENT if deletion is successful,
     *         or HTTP status NOT_FOUND if the table with the given ID does not exist.
     */
    @Transactional
    public ResponseEntity<Void> deleteDineInTableById(long id) {
        logger.info("Deleting dine-in table with ID: {}", id);
        Optional<DineInTable> dineInTable = dineInTableRepository.findById(id);

        if (dineInTable.isPresent()) {
            dineInTableRepository.deleteById(id);
            logger.info("Dine-in table with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        }
        logger.warn("Failed to delete. Dine-in table with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }

}