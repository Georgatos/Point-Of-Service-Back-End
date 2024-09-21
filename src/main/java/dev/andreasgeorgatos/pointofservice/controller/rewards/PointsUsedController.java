package dev.andreasgeorgatos.pointofservice.controller.rewards;

import dev.andreasgeorgatos.pointofservice.service.rewards.PointsUsedService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/points-used")
public class PointsUsedController {

    private final PointsUsedService pointsUsedService;

    @Autowired
    public PointsUsedController(PointsUsedService pointsUsedService) {
        this.pointsUsedService = pointsUsedService;
    }

    @GetMapping
    public ResponseEntity<List<PointsUsed>> getAllPointsUsed() {
        return pointsUsedService.getAllPointsUsed();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PointsUsed> getPointsUsedById(@PathVariable Long id) {
        return pointsUsedService.getPointsUsedById(id);
    }

    @PostMapping
    public ResponseEntity<?> createPointsUsed(@Valid @RequestBody PointsUsed pointsUsed, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return pointsUsedService.createPointsUsed(pointsUsed);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editPointsUsed(@Valid @PathVariable Long id, @RequestBody PointsUsed pointsUsed, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return pointsUsedService.editPointsUsed(id, pointsUsed);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PointsUsed> deletePointsUsedById(@PathVariable Long id) {
        return pointsUsedService.deletePointsUsedById(id);
    }
}