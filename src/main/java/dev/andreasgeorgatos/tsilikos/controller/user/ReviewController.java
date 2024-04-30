package dev.andreasgeorgatos.tsilikos.controller.user;

import dev.andreasgeorgatos.tsilikos.model.user.Review;
import dev.andreasgeorgatos.tsilikos.service.user.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {


    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping()
    public ResponseEntity<List<Review>> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @PostMapping()
    public ResponseEntity<?> createReview(@Valid @RequestBody Review review, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return reviewService.createReview(review);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editReviewById(@Valid @PathVariable Long id, @RequestBody Review review, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        return reviewService.editReviewById(id, review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReviewById(@Valid @PathVariable Long id) {
        return reviewService.deleteReviewById(id);
    }

}
