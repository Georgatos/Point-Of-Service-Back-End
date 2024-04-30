package dev.andreasgeorgatos.tsilikos.repository.rewards;

import dev.andreasgeorgatos.tsilikos.model.rewards.MembershipCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipCardRepository extends JpaRepository<MembershipCard, Long> {
}
