package dev.andreasgeorgatos.tsilikos.controller.rewards;

import dev.andreasgeorgatos.tsilikos.model.rewards.PointsEarned;
import dev.andreasgeorgatos.tsilikos.service.rewards.PointsEarnedService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/points-earned")
public class PointsEarnedController {

    private final PointsEarnedService pointsEarnedService;

    @Autowired
    public PointsEarnedController(PointsEarnedService pointsEarnedService) {
        this.pointsEarnedService = pointsEarnedService;
    }


    @GetMapping
    public ResponseEntity<List<PointsEarned>> getAllPointsEarned() {
        return pointsEarnedService.getAllPointsEarned();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PointsEarned> getPointsEarnedById(@PathVariable Long id) {
        return pointsEarnedService.getPointsEarnedById(id);
    }

    @PostMapping
    public ResponseEntity<?> createPointsEarned(@Valid @RequestBody PointsEarned pointsEarned, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return pointsEarnedService.createPointsEarned(pointsEarned);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editPointsEarnedById(@Valid @PathVariable Long id, @RequestBody PointsEarned pointsEarned, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return pointsEarnedService.editPointsEarnedById(id, pointsEarned);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PointsEarned> deletePointsEarnedById(@PathVariable Long id) {
        return pointsEarnedService.deletePointsEarnedById(id);
    }


}
