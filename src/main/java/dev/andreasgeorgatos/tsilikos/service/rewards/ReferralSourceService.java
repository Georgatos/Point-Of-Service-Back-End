package dev.andreasgeorgatos.tsilikos.service.rewards;

import dev.andreasgeorgatos.tsilikos.model.rewards.ReferralSource;
import dev.andreasgeorgatos.tsilikos.repository.rewards.ReferralSourceRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReferralSourceService {

    private final ReferralSourceRepository referralSourceRepository;

    public ReferralSourceService(ReferralSourceRepository referralSourceRepository) {
        this.referralSourceRepository = referralSourceRepository;
    }

    public ResponseEntity<List<ReferralSource>> getAllReferrals() {
        List<ReferralSource> pointsToEuroRatio = referralSourceRepository.findAll();

        if (pointsToEuroRatio.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pointsToEuroRatio);
    }

    public ResponseEntity<ReferralSource> getReferralById(long id) {
        Optional<ReferralSource> optionalPointsUsed = referralSourceRepository.findById(id);

        return optionalPointsUsed.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<ReferralSource> createReferral(ReferralSource referralSource) {
        return ResponseEntity.ok(referralSourceRepository.save(referralSource));
    }

    @Transactional
    public ResponseEntity<ReferralSource> editReferralById(long id, ReferralSource referralSource) {
        Optional<ReferralSource> optionalReferralSource = referralSourceRepository.findById(id);

        if (optionalReferralSource.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ReferralSource foundReferral = optionalReferralSource.get();

        foundReferral.setAddress(referralSource.getAddress());
        foundReferral.setName(referralSource.getName());
        foundReferral.setDescription(referralSource.getDescription());

        referralSourceRepository.save(foundReferral);
        return ResponseEntity.ok(foundReferral);
    }

    @Transactional
    public ResponseEntity<ReferralSource> deleteReferralById(long id) {
        Optional<ReferralSource> optionalsPointsUsed = referralSourceRepository.findById(id);

        if (optionalsPointsUsed.isPresent()) {
            referralSourceRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
