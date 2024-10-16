package dev.andreasgeorgatos.pointofservice.repository.users;

import dev.andreasgeorgatos.pointofservice.model.user.ShiftLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ShiftLogRepository extends JpaRepository<ShiftLog, Long> {

    @Query("SELECT s FROM ShiftLog s WHERE s.id IN :ids")
    Set<ShiftLog> getPastShiftsById(long id);
}
