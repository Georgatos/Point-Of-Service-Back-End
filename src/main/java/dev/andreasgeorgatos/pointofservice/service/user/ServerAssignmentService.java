package dev.andreasgeorgatos.pointofservice.service.user;

import dev.andreasgeorgatos.pointofservice.model.user.ServerAssignment;
import dev.andreasgeorgatos.pointofservice.repository.users.ServerAssignmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServerAssignmentService {
    private final ServerAssignmentRepository serverAssignmentRepository;

    @Autowired
    public ServerAssignmentService(ServerAssignmentRepository serverAssignmentRepository) {
        this.serverAssignmentRepository = serverAssignmentRepository;
    }

    public ResponseEntity<List<ServerAssignment>> getAllServerAssignments() {
        List<ServerAssignment> serverAssignments = serverAssignmentRepository.findAll();

        if (serverAssignments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(serverAssignments);
    }

    public ResponseEntity<ServerAssignment> getServerAssignmentById(long id) {
        Optional<ServerAssignment> optionalServerAssignment = serverAssignmentRepository.findById(id);

        return optionalServerAssignment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<ServerAssignment> createServerAssignment(ServerAssignment serverAssignment) {
        ServerAssignment savedShift = serverAssignmentRepository.save(serverAssignment);

        return ResponseEntity.ok(savedShift);
    }

    @Transactional
    public ResponseEntity<ServerAssignment> editServerAssignmentById(long id, ServerAssignment serverAssignment) {
        Optional<ServerAssignment> foundServerAssignment = serverAssignmentRepository.findById(id);

        if (foundServerAssignment.isPresent()) {
            ServerAssignment oldServerAssignment = foundServerAssignment.get();

            oldServerAssignment.setAssignmentDate(serverAssignment.getAssignmentDate());
            oldServerAssignment.setStatus(serverAssignment.getStatus());
            oldServerAssignment.setTableId(serverAssignment.getTableId());
            oldServerAssignment.setUserId(serverAssignment.getUserId());

            return ResponseEntity.ok(serverAssignmentRepository.save(oldServerAssignment));
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<ServerAssignment> deleteServerAssignmentById(long id) {
        Optional<ServerAssignment> optionalShift = serverAssignmentRepository.findById(id);

        if (optionalShift.isPresent()) {
            serverAssignmentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
