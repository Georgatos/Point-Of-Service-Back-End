package dev.andreasgeorgatos.pointofservice.controller.user;

import dev.andreasgeorgatos.pointofservice.model.user.UserType;
import dev.andreasgeorgatos.pointofservice.service.user.UserTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-types")
public class UserTypeController {

    private final UserTypeService userTypeService;

    @Autowired
    public UserTypeController(UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }

    @GetMapping
    public ResponseEntity<List<UserType>> getAllUserTypes() {
        return userTypeService.getAllUserTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserType> getEntityById(@PathVariable Long id) {
        return userTypeService.getUserTypeById(id);
    }

    @PostMapping()
    public ResponseEntity<?> createUserType(@Valid @RequestBody UserType userType, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return userTypeService.createUserType(userType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserType(@Valid @PathVariable Long id, @RequestBody UserType updatedUserType, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        return userTypeService.editUserType(id, updatedUserType.getUserType());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserType> deleteUserType(@PathVariable Long id) {
        return userTypeService.deleteUserType(id);
    }
}

