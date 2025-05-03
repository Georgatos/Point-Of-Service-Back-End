package dev.andreasgeorgatos.pointofservice.controller.orders;

import dev.andreasgeorgatos.pointofservice.model.order.OrderHistory;
import dev.andreasgeorgatos.pointofservice.service.order.OrderHistoryService;
import dev.andreasgeorgatos.pointofservice.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-history")
public class OrderHistoryController {

    private final OrderHistoryService orderHistoryService;

    @Autowired
    public OrderHistoryController(OrderHistoryService orderHistoryService) {
        this.orderHistoryService = orderHistoryService;
    }

    @GetMapping()
    public ResponseEntity<List<OrderHistory>> getAllOrders() {
        return orderHistoryService.getAllOrdersHistory();
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderHistory> getOrderById(@PathVariable Long id) {
        return orderHistoryService.getOrderHistoryById(id);
    }

    @PostMapping()
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderHistory orderHistory, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return orderHistoryService.createOrderHistory(orderHistory);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editOrderById(@Valid @PathVariable Long id, @RequestBody OrderHistory orderHistory, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return orderHistoryService.editOrderHistoryById(id, orderHistory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable Long id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }

        return orderHistoryService.deleteOrderHistoryById(id);
    }
}
