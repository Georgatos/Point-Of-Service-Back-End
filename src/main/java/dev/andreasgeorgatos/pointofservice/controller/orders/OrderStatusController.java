package dev.andreasgeorgatos.pointofservice.controller.orders;


import dev.andreasgeorgatos.pointofservice.model.order.OrderStatuses;
import dev.andreasgeorgatos.pointofservice.service.order.OrderStatusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-status")
public class OrderStatusController {

    private final OrderStatusService orderStatusService;

    @Autowired
    public OrderStatusController(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    @GetMapping()
    public ResponseEntity<List<OrderStatuses>> getAllOrderStatuses() {
        return orderStatusService.getAllOrderStatutes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderStatuses> getOrderStatusById(@PathVariable Long id) {
        return orderStatusService.getOrderStatusById(id);
    }

    @PostMapping()
    public ResponseEntity<?> createOrderStatus(@Valid @RequestBody OrderStatuses orderStatus, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return orderStatusService.createOrderStatus(orderStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editOrderStatusById(@Valid @PathVariable Long id, @RequestBody OrderStatuses orderStatus, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        return orderStatusService.editOrderStatusById(id, orderStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderStatus(@Valid @PathVariable Long id) {
        return orderStatusService.deleteOrderStatusById(id);
    }

}
