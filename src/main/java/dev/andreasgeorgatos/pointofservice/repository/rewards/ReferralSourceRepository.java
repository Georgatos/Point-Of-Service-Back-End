package dev.andreasgeorgatos.pointofservice.repository.rewards;

import dev.andreasgeorgatos.pointofservice.model.rewards.ReferralSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralSourceRepository extends JpaRepository<ReferralSource, Long> {
}
