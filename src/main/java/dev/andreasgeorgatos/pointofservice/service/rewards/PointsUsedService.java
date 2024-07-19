package dev.andreasgeorgatos.pointofservice.service.rewards;

import dev.andreasgeorgatos.pointofservice.model.rewards.MembershipCard;
import dev.andreasgeorgatos.pointofservice.model.rewards.PointsUsed;
import dev.andreasgeorgatos.pointofservice.model.user.User;
import dev.andreasgeorgatos.pointofservice.repository.rewards.MembershipCardRepository;
import dev.andreasgeorgatos.pointofservice.repository.rewards.PointsUsedRepository;
import dev.andreasgeorgatos.pointofservice.repository.users.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PointsUsedService {

    private final PointsUsedRepository pointsUsedRepository;
    private final MembershipCardRepository membershipCardRepository;
    private final UserRepository userRepository;

    @Autowired
    public PointsUsedService(PointsUsedRepository pointsUsedRepository, MembershipCardRepository membershipCardRepository, UserRepository userRepository) {
        this.pointsUsedRepository = pointsUsedRepository;
        this.membershipCardRepository = membershipCardRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<List<PointsUsed>> getAllPointsUsed() {
        List<PointsUsed> pointsToEuroRatio = pointsUsedRepository.findAll();

        if (pointsToEuroRatio.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pointsToEuroRatio);
    }

    public ResponseEntity<PointsUsed> getPointsUsedById(long id) {
        Optional<PointsUsed> optionalPointsUsed = pointsUsedRepository.findById(id);

        return optionalPointsUsed.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<PointsUsed> createPointsUsed(PointsUsed pointsUsed) {
        Optional<MembershipCard> optionalMembershipCard = membershipCardRepository.findById(pointsUsed.getMembershipCard().getId());
        Optional<User> optionalUser = userRepository.findById(pointsUsed.getUser().getId());

        if (optionalMembershipCard.isEmpty()) {
            pointsUsed.setMembershipCard(null);
        } else {
            pointsUsed.setMembershipCard(optionalMembershipCard.get());
        }


        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pointsUsed.setUser(optionalUser.get());

        pointsUsed.setMembershipCard(pointsUsed.getMembershipCard());

        return ResponseEntity.ok(pointsUsedRepository.save(pointsUsed));
    }

    @Transactional
    public ResponseEntity<PointsUsed> editPointsUsed(long id, PointsUsed pointsUsed) {
        Optional<PointsUsed> optionalPointsUsed = pointsUsedRepository.findById(id);
        Optional<MembershipCard> optionalMembershipCard = membershipCardRepository.findById(pointsUsed.getMembershipCard().getId());
        Optional<User> optionalUser = userRepository.findById(pointsUsed.getUser().getId());

        if (optionalPointsUsed.isPresent()) {
            PointsUsed pointsUsedFound = optionalPointsUsed.get();

            pointsUsedFound.setUsed(pointsUsed.getUsed());

            if (optionalMembershipCard.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            if (optionalUser.isEmpty()) {
                pointsUsed.setUser(null);
            } else {
                pointsUsed.setUser(optionalUser.get());
            }

            pointsUsed.setMembershipCard(pointsUsed.getMembershipCard());

            pointsUsedRepository.save(pointsUsedFound);

            return ResponseEntity.ok(pointsUsedFound);
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<PointsUsed> deletePointsUsedById(long id) {
        Optional<PointsUsed> optionalsPointsUsed = pointsUsedRepository.findById(id);

        if (optionalsPointsUsed.isPresent()) {
            pointsUsedRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
