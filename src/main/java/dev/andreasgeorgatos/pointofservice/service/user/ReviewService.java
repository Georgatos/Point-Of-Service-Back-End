package dev.andreasgeorgatos.pointofservice.service.user;

import dev.andreasgeorgatos.pointofservice.model.user.Review;
import dev.andreasgeorgatos.pointofservice.repository.users.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();

        if (reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(reviews);
    }

    public ResponseEntity<Review> getReviewById(long id) {
        Optional<Review> optionalReview = reviewRepository.findById(id);

        return optionalReview.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<Review> createReview(Review review) {
        Review savedReview = reviewRepository.save(review);
        return ResponseEntity.ok(savedReview);
    }

    @Transactional
    public ResponseEntity<Review> editReviewById(long id, Review review) {
        Optional<Review> foundReview = reviewRepository.findById(id);

        if (foundReview.isPresent()) {
            Review oldReview = foundReview.get();

            oldReview.setReviewDate(review.getReviewDate());
            oldReview.setComment(review.getComment());
            oldReview.setRating(review.getRating());
            oldReview.setOrderId(review.getOrderId());
            oldReview.setReviewDate(review.getReviewDate());

            Review savedCategory = reviewRepository.save(review);

            return ResponseEntity.ok(savedCategory);
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<Review> deleteReviewById(long id) {
        Optional<Review> optionalReview = reviewRepository.findById(id);

        if (optionalReview.isPresent()) {
            reviewRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}