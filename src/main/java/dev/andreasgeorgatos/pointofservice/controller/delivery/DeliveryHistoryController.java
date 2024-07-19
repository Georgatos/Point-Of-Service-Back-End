package dev.andreasgeorgatos.pointofservice.controller.delivery;

import dev.andreasgeorgatos.pointofservice.model.delivery.DeliveryHistory;
import dev.andreasgeorgatos.pointofservice.service.delivery.DeliveryHistoryService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/delivery-history")
public class DeliveryHistoryController {

    private final DeliveryHistoryService deliveryHistoryService;

    public DeliveryHistoryController(DeliveryHistoryService deliveryHistoryService) {
        this.deliveryHistoryService = deliveryHistoryService;
    }

    @GetMapping()
    public ResponseEntity<List<DeliveryHistory>> getAllDeliveryHistories() {
        return deliveryHistoryService.getAllDeliveryHistories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryHistory> getDeliveryHistoryById(@PathVariable Long id) {
        return deliveryHistoryService.getDeliveryHistoryById(id);
    }

    @PostMapping()
    public ResponseEntity<?> createDeliveryHistory(@Valid @RequestBody DeliveryHistory deliveryHistory, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return deliveryHistoryService.createDeliveryHistory(deliveryHistory);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editOrderById(@Valid @PathVariable Long id, @RequestBody DeliveryHistory deliveryHistory, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        return deliveryHistoryService.editDeliveryHistoryById(id, deliveryHistory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDeliveryHistory(@Valid @PathVariable Long id) {
        return deliveryHistoryService.deleteDeliveryHistory(id);
    }


}
