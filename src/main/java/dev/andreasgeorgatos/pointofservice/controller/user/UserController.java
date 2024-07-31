package dev.andreasgeorgatos.pointofservice.controller.user;

import dev.andreasgeorgatos.pointofservice.DTO.CredentialsDTO;
import dev.andreasgeorgatos.pointofservice.DTO.UserDTO;
import dev.andreasgeorgatos.pointofservice.DTO.VerificationCodeDTO;
import dev.andreasgeorgatos.pointofservice.configuration.JWTUtil;
import dev.andreasgeorgatos.pointofservice.model.user.User;
import dev.andreasgeorgatos.pointofservice.service.user.POSUser;
import dev.andreasgeorgatos.pointofservice.service.user.UserService;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        if (!isUserValid(id)) {
            return ResponseEntity.badRequest().body("Not the same users.");
        }

        return userService.getUserDTO(id);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody CredentialsDTO credentialsDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }

        if (!userService.loginUser(credentialsDTO.getEmail(), credentialsDTO.getPassword())) {
            return ResponseEntity.notFound().build();
        }

        POSUser foundUser = (POSUser) userService.loadUserByUsername(credentialsDTO.getEmail());
        Map<String, Object> claims = userService.getClaims(foundUser);
        String jwe = jwtUtil.generateJWE(foundUser.getUser().getEmail(), claims);

        return ResponseEntity.ok().header("Authorization", "Bearer " + jwe).build();

    }

    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }


        if (userService.registerUser(userDTO).getStatusCode() != HttpStatus.CREATED){
            return ResponseEntity.badRequest().body("Failed to create the user.");
        }

        POSUser foundUser = (POSUser) userService.loadUserByUsername(userDTO.getEmail());

        Map<String, Object> claims = userService.getClaims(foundUser);
        String jwe = jwtUtil.generateJWE(userDTO.getEmail(), claims);

        return ResponseEntity.ok().header("Authorization", "Bearer " + jwe).body(userDTO);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody String email, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }

        return userService.resetPassword(email);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@Valid @RequestBody VerificationCodeDTO verificationCode, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }

        return userService.verifyEmail(verificationCode.getEmail(), verificationCode.getVerificationCode());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editUserById(@Valid @PathVariable Long id, @RequestBody UserDTO userDTO) {
        if (!isUserValid(id)) {
            return ResponseEntity.badRequest().body("Not the same users.");
        }
        return userService.editUserDtoById(id, userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@Valid @PathVariable Long id) {
        if (!isUserValid(id)) {
            return ResponseEntity.badRequest().body("Not the same users.");
        }

        userService.deleteUser(id);

        return ResponseEntity.ok().build();
    }

    private boolean isUserValid(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            return false;
        }

        return userService.isSameUser(authentication.getPrincipal().toString(), id);
    }
}
