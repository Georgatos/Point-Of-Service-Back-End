package dev.andreasgeorgatos.tsilikos.repository.rewards;

import dev.andreasgeorgatos.tsilikos.model.rewards.ReferralSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralSourceRepository extends JpaRepository<ReferralSource, Long> {
}
