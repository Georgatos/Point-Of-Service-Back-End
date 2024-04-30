package dev.andreasgeorgatos.tsilikos.controller.delivery;

import dev.andreasgeorgatos.tsilikos.model.delivery.DeliveryStatus;
import dev.andreasgeorgatos.tsilikos.service.delivery.DeliveryStatusService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/delivery-status")
public class DeliveryStatusController {
    private final DeliveryStatusService deliveryStatusService;

    public DeliveryStatusController(DeliveryStatusService deliveryStatusService) {
        this.deliveryStatusService = deliveryStatusService;
    }

    @GetMapping()
    public ResponseEntity<List<DeliveryStatus>> getAllDeliveryHistories() {
        return deliveryStatusService.getAllDeliveryStatuses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryStatus> getDeliveryHistoryById(@PathVariable Long id) {
        return deliveryStatusService.getDeliveryStatusById(id);
    }


    @PostMapping()
    public ResponseEntity<?> createDeliveryHistory(@Valid @RequestBody DeliveryStatus deliveryStatus, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return deliveryStatusService.createDeliveryStatus(deliveryStatus);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editOrderById(@Valid @PathVariable Long id, @RequestBody DeliveryStatus deliveryStatus, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return deliveryStatusService.editDeliveryStatusById(id, deliveryStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDeliveryHistory(@Valid @PathVariable Long id) {
        return deliveryStatusService.deleteDeliveryStatus(id);
    }
}
