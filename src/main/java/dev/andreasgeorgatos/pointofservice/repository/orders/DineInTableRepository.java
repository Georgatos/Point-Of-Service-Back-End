package dev.andreasgeorgatos.pointofservice.repository.orders;

import dev.andreasgeorgatos.pointofservice.model.order.DineInTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DineInTableRepository extends JpaRepository<DineInTable, Long> {
}
