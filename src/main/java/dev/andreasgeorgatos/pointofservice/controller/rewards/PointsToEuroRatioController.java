package dev.andreasgeorgatos.pointofservice.controller.rewards;

import dev.andreasgeorgatos.pointofservice.model.rewards.PointsToEuroRatio;
import dev.andreasgeorgatos.pointofservice.service.rewards.PointsToEuroRatioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/points-to-euro-ratio")
public class PointsToEuroRatioController {

    private final PointsToEuroRatioService pointsToEuroRatioService;

    @Autowired
    public PointsToEuroRatioController(PointsToEuroRatioService pointsToEuroRatioService) {
        this.pointsToEuroRatioService = pointsToEuroRatioService;
    }


    @GetMapping
    public ResponseEntity<List<PointsToEuroRatio>> getAllPointsToEuroRatio() {
        return pointsToEuroRatioService.getAllPointsToEuroRatio();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PointsToEuroRatio> getPointsToEuroRatiById(@PathVariable Long id) {
        return pointsToEuroRatioService.getPointsToEuroRatiById(id);
    }

    @PostMapping
    public ResponseEntity<?> createPointsToEuroRatio(@Valid @RequestBody PointsToEuroRatio pointsToEuroRatio, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return pointsToEuroRatioService.createPointsToEuroRatio(pointsToEuroRatio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editPointsToEuroRatio(@Valid @PathVariable Long id, @RequestBody PointsToEuroRatio pointsToEuroRatio, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return pointsToEuroRatioService.editPointsToEuroRatio(id, pointsToEuroRatio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PointsToEuroRatio> deletePointsToEuroRatioById(@PathVariable Long id) {
        return pointsToEuroRatioService.deletePointsToEuroRatioById(id);
    }
}
