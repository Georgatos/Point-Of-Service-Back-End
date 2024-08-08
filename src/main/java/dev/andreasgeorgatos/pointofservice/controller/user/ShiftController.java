package dev.andreasgeorgatos.pointofservice.controller.user;

import dev.andreasgeorgatos.pointofservice.model.user.Shift;
import dev.andreasgeorgatos.pointofservice.service.user.ShiftService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/users/shifts")
public class ShiftController {

    private final ShiftService shiftService;

    @Autowired
    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping()
    public ResponseEntity<List<Shift>> getAllReviews() {
        return shiftService.getAllShifts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shift> getReviewById(@PathVariable Long id) {
        return shiftService.getShiftById(id);
    }

    @PostMapping()
    public ResponseEntity<?> createReview(@Valid @RequestBody Shift shift, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return shiftService.createShift(shift);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editReviewById(@Valid @PathVariable Long id, @RequestBody Shift shift, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }

        return shiftService.editShiftById(id, shift);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReviewById(@Valid @PathVariable Long id) {
        return shiftService.deleteReviewById(id);
    }
}
