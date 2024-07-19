package dev.andreasgeorgatos.pointofservice.repository.users;

import dev.andreasgeorgatos.pointofservice.model.user.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
