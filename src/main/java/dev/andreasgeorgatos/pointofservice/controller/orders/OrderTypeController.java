package dev.andreasgeorgatos.pointofservice.controller.orders;

import dev.andreasgeorgatos.pointofservice.model.order.OrderType;
import dev.andreasgeorgatos.pointofservice.service.order.OrderTypeService;
import dev.andreasgeorgatos.pointofservice.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-types")
public class OrderTypeController {

    private final OrderTypeService orderTypeService;

    @Autowired
    public OrderTypeController(OrderTypeService orderTypeService) {
        this.orderTypeService = orderTypeService;
    }


    @GetMapping()
    public ResponseEntity<List<OrderType>> getAllOrderStatuses() {
        return orderTypeService.getAllOrderTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderType> getOrderTypeId(@PathVariable Long id) {
        return orderTypeService.getOrderTypeById(id);
    }

    @PostMapping()
    public ResponseEntity<?> createOrderType(@Valid @RequestBody OrderType orderType, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return orderTypeService.createOrderType(orderType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editOrderTypeById(@Valid @PathVariable Long id, @RequestBody OrderType orderType, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.getValidationErrors(bindingResult));
        }
        return orderTypeService.editOrderTypeById(id, orderType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderType(@Valid @PathVariable Long id) {
        return orderTypeService.deleteOrderTypeById(id);
    }
}
