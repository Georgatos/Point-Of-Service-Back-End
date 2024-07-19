package dev.andreasgeorgatos.pointofservice.service.rewards;

import dev.andreasgeorgatos.pointofservice.model.rewards.MembershipCard;
import dev.andreasgeorgatos.pointofservice.model.rewards.PointsEarned;
import dev.andreasgeorgatos.pointofservice.model.user.User;
import dev.andreasgeorgatos.pointofservice.repository.rewards.MembershipCardRepository;
import dev.andreasgeorgatos.pointofservice.repository.rewards.PointsEarnedRepository;
import dev.andreasgeorgatos.pointofservice.repository.users.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PointsEarnedService {
    private final PointsEarnedRepository pointsEarnedRepository;
    private final UserRepository userRepository;
    private final MembershipCardRepository membershipCardRepository;

    @Autowired
    public PointsEarnedService(PointsEarnedRepository pointsEarnedRepository, UserRepository userRepository, MembershipCardRepository membershipCardRepository) {
        this.pointsEarnedRepository = pointsEarnedRepository;
        this.userRepository = userRepository;
        this.membershipCardRepository = membershipCardRepository;
    }


    public ResponseEntity<List<PointsEarned>> getAllPointsEarned() {
        List<PointsEarned> pointsEarned = pointsEarnedRepository.findAll();

        if (pointsEarned.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pointsEarned);
    }

    public ResponseEntity<PointsEarned> getPointsEarnedById(long id) {
        Optional<PointsEarned> optionalPointsEarned = pointsEarnedRepository.findById(id);

        return optionalPointsEarned.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<PointsEarned> createPointsEarned(PointsEarned pointsEarned) {
        Optional<User> optionalUser = userRepository.findById(pointsEarned.getUser().getId());
        Optional<MembershipCard> optionalMembershipCard = membershipCardRepository.findById(pointsEarned.getMembershipCard().getId());

        if (optionalUser.isEmpty() || optionalMembershipCard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pointsEarned.setUser(optionalUser.get());
        pointsEarned.setMembershipCard(optionalMembershipCard.get());

        return ResponseEntity.ok(pointsEarnedRepository.save(pointsEarned));
    }

    @Transactional
    public ResponseEntity<PointsEarned> editPointsEarnedById(long id, PointsEarned pointsEarned) {
        Optional<PointsEarned> optionalPointsEarned = pointsEarnedRepository.findById(id);
        Optional<User> optionalUser = userRepository.findById(id);
        Optional<MembershipCard> optionalMembershipCard = membershipCardRepository.findById(pointsEarned.getMembershipCard().getId());

        if (optionalPointsEarned.isEmpty() || optionalUser.isEmpty() || optionalMembershipCard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PointsEarned foundPointsEarned = optionalPointsEarned.get();

        foundPointsEarned.setEarned(pointsEarned.getEarned());
        foundPointsEarned.setUser(pointsEarned.getUser());
        foundPointsEarned.setMembershipCard(optionalMembershipCard.get());

        PointsEarned savedPoints = pointsEarnedRepository.save(foundPointsEarned);

        return ResponseEntity.ok(savedPoints);
    }

    @Transactional
    public ResponseEntity<PointsEarned> deletePointsEarnedById(long id) {
        Optional<PointsEarned> optionalPointsEarned = pointsEarnedRepository.findById(id);

        if (optionalPointsEarned.isPresent()) {
            pointsEarnedRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
