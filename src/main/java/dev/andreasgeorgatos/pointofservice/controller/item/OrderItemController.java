package dev.andreasgeorgatos.pointofservice.controller.item;

import dev.andreasgeorgatos.pointofservice.model.item.OrderItem;
import dev.andreasgeorgatos.pointofservice.service.order.OrderItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/item/OrderItem")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping()
    public ResponseEntity<List<OrderItem>> getAllItems() {
        return orderItemService.getAllOrderItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getItemById(@PathVariable Long id) {
        return orderItemService.getOrderItemById(id);
    }

    @PostMapping()
    public ResponseEntity<?> createItem(@Valid @RequestBody OrderItem item, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return orderItemService.createOrderItem(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editItemById(@Valid @PathVariable Long id, @RequestBody OrderItem item, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return orderItemService.editOrderItemById(id, item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItemById(@Valid @PathVariable Long id) {
        return orderItemService.deleteOrderItemById(id);
    }

}
