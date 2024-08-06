package dev.andreasgeorgatos.pointofservice.repository.orders;

import dev.andreasgeorgatos.pointofservice.model.order.DineInTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DineInTableRepository extends JpaRepository<DineInTable, Long> {

    @Query("SELECT dine FROM DineInTable dine WHERE  dine.tableNumber = :tableNumber")
    Optional<DineInTable> getDineInTableByTableNumber(long tableNumber);

}
