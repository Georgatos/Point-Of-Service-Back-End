package dev.andreasgeorgatos.pointofservice.service.item;

import dev.andreasgeorgatos.pointofservice.model.item.Item;
import dev.andreasgeorgatos.pointofservice.repository.item.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for business logic related to items.
 * It interacts with the {@link ItemRepository} to perform CRUD operations and other item-related actions.
 */
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * Constructs an {@code ItemService} with the given {@link ItemRepository}.
     *
     * @param itemRepository The repository used for item data operations.
     */
    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Retrieves all items from the database.
     *
     * @return A {@link ResponseEntity} containing a list of all items if found,
     *         or a {@code notFound} status if no items exist.
     */
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemRepository.findAll();

        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(items);
    }

    /**
     * Retrieves an item by its unique identifier.
     *
     * @param id The unique identifier of the item.
     * @return A {@link ResponseEntity} containing the item if found,
     *         or a {@code notFound} status if the item does not exist.
     */
    public ResponseEntity<Item> getItemById(long id) {
        Optional<Item> optionalReview = itemRepository.findById(id);

        return optionalReview.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new item and saves it to the database.
     *
     * @param item The item object to be created. Must not be null.
     * @return A {@link ResponseEntity} containing the saved item if creation is successful,
     *         or an {@code internalServerError} status if saving the item fails.
     *         The current implementation returns the original item passed as parameter upon success, not necessarily the instance returned by save().
     */
    @Transactional
    public ResponseEntity<Item> createItem(Item item) {
        Item savedItem = itemRepository.save(item);

        if (savedItem == null) {
            return ResponseEntity.internalServerError().build();
        }
        // Note: The original code returns the input 'item', not 'savedItem'.
        // Javadoc reflects the actual behavior of returning the input item.
        return ResponseEntity.ok(item);

    }

    /**
     * Edits an existing item identified by its ID.
     * Updates the item's properties with the values from the provided item object.
     *
     * @param id The unique identifier of the item to be edited.
     * @param item The item object containing the updated information.
     * @return A {@link ResponseEntity} containing the updated item if found and updated successfully,
     *         or a {@code notFound} status if the item does not exist.
     */
    @Transactional
    public ResponseEntity<Item> editItemById(long id, Item item) {
        Optional<Item> optionalItem = itemRepository.findById(id);

        if (optionalItem.isPresent()) {
            Item oldItem = optionalItem.get();

            oldItem.setItemDescription(item.getItemDescription());
            oldItem.setItemName(item.getItemName());
            oldItem.setCategory(item.getCategory());
            oldItem.setItemPrice(item.getItemPrice());

            Item savedItem = itemRepository.save(oldItem);

            return ResponseEntity.ok(savedItem);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deletes an item identified by its unique identifier.
     *
     * @param id The unique identifier of the item to be deleted.
     * @return A {@link ResponseEntity} with {@code noContent} status if the item was found and deleted successfully,
     *         or a {@code notFound} status if the item does not exist.
     */
    @Transactional
    public ResponseEntity<Item> deleteItemById(long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);

        if (optionalItem.isPresent()) {
            itemRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
