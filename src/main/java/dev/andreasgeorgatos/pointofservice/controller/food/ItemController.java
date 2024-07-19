package dev.andreasgeorgatos.pointofservice.controller.food;

import dev.andreasgeorgatos.pointofservice.model.food.Item;
import dev.andreasgeorgatos.pointofservice.service.food.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/item")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping()
    public ResponseEntity<List<Item>> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemService.getItemById(id);
    }

    @PostMapping()
    public ResponseEntity<?> createItem(@Valid @RequestBody Item item, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return itemService.createItem(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editItemById(@Valid @PathVariable Long id, @RequestBody Item item, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }

        return itemService.editItemById(id, item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItemById(@Valid @PathVariable Long id) {
        return itemService.deleteItemById(id);
    }

}
