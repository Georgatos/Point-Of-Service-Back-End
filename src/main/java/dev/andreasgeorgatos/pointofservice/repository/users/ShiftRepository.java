package dev.andreasgeorgatos.pointofservice.repository.users;

import dev.andreasgeorgatos.pointofservice.controller.user.ShiftController;
import dev.andreasgeorgatos.pointofservice.model.user.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
}
