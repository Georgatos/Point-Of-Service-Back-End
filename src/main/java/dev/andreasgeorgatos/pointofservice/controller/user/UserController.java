package dev.andreasgeorgatos.pointofservice.controller.user;

import dev.andreasgeorgatos.pointofservice.DTO.UserDTO;
import dev.andreasgeorgatos.pointofservice.configuration.JWTUtil;
import dev.andreasgeorgatos.pointofservice.model.user.User;
import dev.andreasgeorgatos.pointofservice.service.user.TsilikosUserDetails;
import dev.andreasgeorgatos.pointofservice.service.user.UserService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping()
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        ResponseEntity<HttpStatus> build = validateUser(id);
        if (build != null) {
            return build;
        }
        return ResponseEntity.status(401).build();
    }

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserCredentials userCredentials, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }

        if (!userService.loginUser(userCredentials.getEmail(), userCredentials.getPassword())) {
            return ResponseEntity.notFound().build();
        }

        TsilikosUserDetails foundUser = (TsilikosUserDetails) userService.loadUserByUsername(userCredentials.getEmail());
        Map<String, Object> claims = userService.getClaims(foundUser);
        String jwe = jwtUtil.generateJWE(foundUser.getUser().getEmail(), claims);

        return ResponseEntity.ok().header("Authorization", "Bearer " + jwe).build();

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }

        return userService.registerUser(userDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editUserById(@Valid @PathVariable Long id, @RequestBody User user) {
        ResponseEntity<HttpStatus> build = validateUser(id);
        if (build != null) {
            return build;
        }
        return userService.editUserById(id, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@Valid @PathVariable Long id) {
        ResponseEntity<HttpStatus> build = validateUser(id);
        if (build != null) {
            return build;
        }

        userService.deleteUser(id);

        return ResponseEntity.ok().build();
    }

    private @Nullable ResponseEntity<HttpStatus> validateUser(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        if (!userService.isSameUser(authentication.getPrincipal().toString(), id)) {
            return ResponseEntity.status(401).build();
        }
        return null;
    }

    public static class UserCredentials {

        String email;
        String password;

        public UserCredentials(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}
