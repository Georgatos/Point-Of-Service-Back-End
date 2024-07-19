package dev.andreasgeorgatos.pointofservice.service.food;

import dev.andreasgeorgatos.pointofservice.model.food.Item;
import dev.andreasgeorgatos.pointofservice.repository.food.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemRepository.findAll();

        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(items);
    }

    public ResponseEntity<Item> getItemById(long id) {
        Optional<Item> optionalReview = itemRepository.findById(id);

        return optionalReview.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<Item> createItem(Item item) {
        Item savedItem = itemRepository.save(item);

        if (savedItem == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(item);

    }

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
