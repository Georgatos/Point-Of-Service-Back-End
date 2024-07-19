package dev.andreasgeorgatos.pointofservice.service.rewards;

import dev.andreasgeorgatos.pointofservice.model.rewards.MembershipCard;
import dev.andreasgeorgatos.pointofservice.model.rewards.ReferralSource;
import dev.andreasgeorgatos.pointofservice.repository.rewards.MembershipCardRepository;
import dev.andreasgeorgatos.pointofservice.repository.rewards.ReferralSourceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MembershipCardService {

    private final MembershipCardRepository membershipCardRepository;
    private final ReferralSourceRepository referralSourceRepository;

    @Autowired
    public MembershipCardService(MembershipCardRepository membershipCardRepository, ReferralSourceRepository referralSourceRepository) {
        this.membershipCardRepository = membershipCardRepository;
        this.referralSourceRepository = referralSourceRepository;

    }


    public ResponseEntity<List<MembershipCard>> getAllMembershipCards() {
        List<MembershipCard> membershipCards = membershipCardRepository.findAll();

        if (membershipCards.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(membershipCards);
    }

    public ResponseEntity<MembershipCard> getMembershipCardById(long id) {
        Optional<MembershipCard> optionalMembershipCard = membershipCardRepository.findById(id);

        return optionalMembershipCard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<MembershipCard> createMembershipCard(MembershipCard membershipCard) {
        Optional<ReferralSource> optionalReferralSource = referralSourceRepository.findById(membershipCard.getReferralSource().getId());

        if (optionalReferralSource.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        membershipCard.setReferralSource(optionalReferralSource.get());

        return ResponseEntity.ok(membershipCardRepository.save(membershipCard));
    }

    @Transactional
    public ResponseEntity<MembershipCard> editMembershipCardById(Long id, MembershipCard membershipCard) {
        Optional<MembershipCard> optionalMembershipCard = membershipCardRepository.findById(id);
        Optional<ReferralSource> optionalReferralSource = referralSourceRepository.findById(membershipCard.getReferralSource().getId());

        if (optionalReferralSource.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (optionalMembershipCard.isEmpty()) {
            return ResponseEntity.notFound().build();

        }

        MembershipCard foundMembershipCard = optionalMembershipCard.get();

        foundMembershipCard.setType(membershipCard.getType());
        foundMembershipCard.setReferralSource(optionalReferralSource.get());
        foundMembershipCard.setCanBeRefreshed(membershipCard.isCanBeRefreshed());
        foundMembershipCard.setTotalSpent(membershipCard.getTotalSpent());
        foundMembershipCard.setCanEarnPoints(membershipCard.isCanEarnPoints());
        foundMembershipCard.setArchived(membershipCard.isArchived());
        foundMembershipCard.setActive(membershipCard.isActive());
        foundMembershipCard.setRequiresAccount(membershipCard.isRequiresAccount());
        foundMembershipCard.setIssuedDate(membershipCard.getIssuedDate());
        foundMembershipCard.setLastUsed(membershipCard.getLastUsed());
        foundMembershipCard.setExpirationDate(membershipCard.getExpirationDate());

        membershipCardRepository.save(foundMembershipCard);
        return ResponseEntity.ok(foundMembershipCard);

    }

    @Transactional
    public ResponseEntity<MembershipCard> deleteMembershipCardById(long id) {
        Optional<MembershipCard> optionalMembershipCard = membershipCardRepository.findById(id);

        if (optionalMembershipCard.isPresent()) {
            membershipCardRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
