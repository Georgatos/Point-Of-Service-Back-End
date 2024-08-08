package dev.andreasgeorgatos.pointofservice.controller.user;

import dev.andreasgeorgatos.pointofservice.model.user.ServerAssignment;
import dev.andreasgeorgatos.pointofservice.service.user.ServerAssignmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/users/serverAssignment")
public class ServerAssignmentController {

    private final ServerAssignmentService serverAssignmentService;

    @Autowired
    public ServerAssignmentController(ServerAssignmentService serverAssignmentService) {
        this.serverAssignmentService = serverAssignmentService;
    }

    @GetMapping()
    public ResponseEntity<List<ServerAssignment>> getAllServerAssignments() {
        return serverAssignmentService.getAllServerAssignments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServerAssignment> getServerAssignmentById(@PathVariable Long id) {
        return serverAssignmentService.getServerAssignmentById(id);
    }

    @PostMapping()
    public ResponseEntity<?> createServerAssignment(@Valid @RequestBody ServerAssignment serverAssignment, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return serverAssignmentService.createServerAssignment(serverAssignment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editServerAssignmentById(@Valid @PathVariable Long id, @RequestBody ServerAssignment serverAssignment, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }

        return serverAssignmentService.editServerAssignmentById(id, serverAssignment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteServerAssignmentById(@Valid @PathVariable Long id) {
        return serverAssignmentService.deleteServerAssignmentById(id);
    }
}
