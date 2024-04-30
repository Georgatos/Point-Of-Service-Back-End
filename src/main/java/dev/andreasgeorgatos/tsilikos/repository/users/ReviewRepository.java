package dev.andreasgeorgatos.tsilikos.repository.users;

import dev.andreasgeorgatos.tsilikos.model.user.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
