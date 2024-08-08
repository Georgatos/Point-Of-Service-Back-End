package dev.andreasgeorgatos.pointofservice.repository.users;

import dev.andreasgeorgatos.pointofservice.model.user.ServerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerAssignmentRepository extends JpaRepository<ServerAssignment, Long> {
}
