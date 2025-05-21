package dev.andreasgeorgatos.pointofservice.controller.item;

import dev.andreasgeorgatos.pointofservice.model.item.Item;
import dev.andreasgeorgatos.pointofservice.service.item.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import dev.andreasgeorgatos.pointofservice.utils.ValidationUtils; // Assuming this utility class is available
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing general items.
 * Provides endpoints for CRUD operations on items.
 */
@RestController
@RequestMapping("/api/v1/item")
public class ItemController {

    private final ItemService itemService;

    /**
     * Constructs an ItemController with the necessary service.
     * @param itemService The service to handle item operations.
     */
    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * Retrieves all items.
     * @return A ResponseEntity containing a list of all items and HTTP status OK.
     */
    @GetMapping()
    public ResponseEntity<List<Item>> getAllItems() {
        return itemService.getAllItems();
    }

    /**
     * Retrieves a specific item by its ID.
     * @param id The ID of the item to retrieve.
     * @return A ResponseEntity containing the item and HTTP status OK,
     * or HTTP status NOT_FOUND if the item does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemService.getItemById(id);
    }

    /**
     * Creates a new item.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param item The item to create.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the created item and HTTP status CREATED,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PostMapping()
    public ResponseEntity<?> createItem(@Valid @RequestBody Item item, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return itemService.createItem(item);
    }

    /**
     * Updates an existing item.
     * Validates the request body. If validation fails, returns a list of errors with HTTP status BAD_REQUEST.
     * @param id The ID of the item to update.
     * @param item The updated item information.
     * @param bindingResult Container for validation results.
     * @return A ResponseEntity containing the updated item and HTTP status OK,
     * or HTTP status NOT_FOUND if the item does not exist,
     * or a list of validation errors and HTTP status BAD_REQUEST.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> editItemById(@PathVariable Long id, @Valid @RequestBody Item item, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return itemService.editItemById(id, item);
    }

    /**
     * Deletes a specific item by its ID.
     * @param id The ID of the item to delete.
     * @return A ResponseEntity with HTTP status NO_CONTENT if successful,
     * or HTTP status NOT_FOUND if the item does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItemById(@PathVariable Long id) {
        return itemService.deleteItemById(id);
    }
}
