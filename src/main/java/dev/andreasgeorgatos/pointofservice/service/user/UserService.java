package dev.andreasgeorgatos.pointofservice.service.user;

import dev.andreasgeorgatos.pointofservice.utils.UtilClass;
import dev.andreasgeorgatos.pointofservice.configuration.SecurityConfig;
import dev.andreasgeorgatos.pointofservice.dto.users.UserDTO;
import dev.andreasgeorgatos.pointofservice.model.address.Address;
import dev.andreasgeorgatos.pointofservice.model.rewards.MembershipCard;
import dev.andreasgeorgatos.pointofservice.model.user.Role;
import dev.andreasgeorgatos.pointofservice.model.user.User;
import dev.andreasgeorgatos.pointofservice.repository.rewards.MembershipCardRepository;
import dev.andreasgeorgatos.pointofservice.repository.users.AddressRepository;
import dev.andreasgeorgatos.pointofservice.repository.users.RoleRepository;
import dev.andreasgeorgatos.pointofservice.repository.users.UserRepository;
import dev.andreasgeorgatos.pointofservice.service.email.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class responsible for user management, authentication, registration, password recovery,
 * and related operations. It also implements {@link UserDetailsService} for Spring Security integration,
 * allowing it to load user-specific data.
 */
@Service
@EnableScheduling
public class UserService implements UserDetailsService {

    private final SecurityConfig securityConfig;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MembershipCardRepository membershipCardRepository;
    private final EmailService emailService;
    private final AddressService addressService;

    /**
     * Constructs a new {@code UserService} with the specified dependencies.
     *
     * @param securityConfig The security configuration for password encoding.
     * @param userRepository The repository for user data access.
     * @param roleRepository The repository for role data access.
     * @param membershipCardRepository The repository for membership card data access.
     * @param addressRepository The repository for address data access (unused directly, consider removing or using via AddressService).
     * @param emailService The service for sending emails.
     * @param addressService The service for address management.
     */
    @Autowired
    public UserService(SecurityConfig securityConfig, UserRepository userRepository, RoleRepository roleRepository, MembershipCardRepository membershipCardRepository, AddressRepository addressRepository, EmailService emailService, AddressService addressService) {
        this.securityConfig = securityConfig;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.membershipCardRepository = membershipCardRepository;
        this.emailService = emailService;
        this.addressService = addressService;
    }

    /**
     * Retrieves a user by their ID and converts them to a {@link UserDTO}.
     *
     * @param id The ID of the user to retrieve.
     * @return A {@link ResponseEntity} containing the {@link UserDTO} and HTTP status OK if the user is found,
     *         or HTTP status NOT_FOUND if the user does not exist.
     */
    public ResponseEntity<?> getUserDTO(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        UserDTO userDTO = UtilClass.convertUserToUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * Loads a user by their username for Spring Security authentication.
     *
     * @param userName The username of the user to load.
     * @return A {@link UserDetails} object representing the loaded user.
     * @throws UsernameNotFoundException if the user with the given username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByUsername(userName);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with user name: " + userName);
        }

        User user = userOptional.get();

        return new POSUser(user);
    }

    /**
     * Checks if the authenticated principal (username) corresponds to the user with the given ID.
     *
     * @param principal The username of the authenticated principal.
     * @param id The ID of the user to compare against.
     * @return {@code true} if the principal's username matches the user with the given ID, {@code false} otherwise.
     */
    public boolean isSameUser(String principal, long id) {
        Optional<User> optionalUser = userRepository.findUserByUsername(principal);

        if (optionalUser.isEmpty()) {
            return false;
        }
        User user = optionalUser.get();
        return user.getId() == id;
    }

    /**
     * Extracts claims (authorities) from a {@link POSUser} object.
     *
     * @param userDetails The {@link POSUser} object containing user details and authorities.
     * @return A map containing the user's authorities under the key "auth".
     */
    public Map<String, Object> getClaims(POSUser userDetails) {

        Map<String, Object> claims = new HashMap<>();

        List<String> authoritiesNames = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        claims.put("auth", authoritiesNames);

        return claims;
    }

    /**
     * Validates user login credentials.
     *
     * @param userName The username of the user attempting to log in.
     * @param password The plain text password of the user.
     * @return {@code true} if the username exists and the password matches the stored hash, {@code false} otherwise.
     */
    public boolean loginUser(String userName, String password) {
        if (userRepository.findUserByUsername(userName).isEmpty()) {
            return false;
        }
        User user = userRepository.findUserByUsername(userName).get();
        return securityConfig.delegatingPasswordEncoder().matches(password, user.getPassword());
    }

    /**
     * Registers a new user in the system.
     * Assigns a default role, creates an address, generates a verification token, and sends a registration email.
     *
     * @param userDTO The {@link UserDTO} containing the information for the new user.
     * @return A {@link ResponseEntity} with HTTP status CREATED if registration is successful.
     *         Returns HTTP status CONFLICT if the email or username already exists.
     *         Returns HTTP status NOT_FOUND if the default role or address creation fails.
     */
    @Transactional
    public ResponseEntity<?> registerUser(UserDTO userDTO) {
        if (userDTO == null || userRepository.findUserByEmail(userDTO.getEmail()).isPresent() || userRepository.findUserByUsername(userDTO.getUserName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Optional<Role> potentialRole = roleRepository.findById(1L); // Assuming 1L is a default customer role

        if (potentialRole.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found");
        }

        List<Role> currentRoles = new ArrayList<>();
        currentRoles.add(potentialRole.get());

        Optional<MembershipCard> optionalMembershipCard = membershipCardRepository.findById(1L); // Assuming 1L is a default membership card
        MembershipCard membershipCard = optionalMembershipCard.orElse(null);

        Address address = addressService.createAddress(new Address(userDTO.getCountry(), userDTO.getCity(), userDTO.getStreet(), userDTO.getNumber(), userDTO.getPostalCode(), userDTO.getStoryLevel(), userDTO.getDoorRingBellName())).getBody();

        if (address == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The address is empty, please specify.");
        }

        String verificationToken = generateVerificationToken();

        User newUser = new User();
        newUser.setUserName(userDTO.getUserName());
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setBirthDate(userDTO.getBirthDate());
        newUser.setPhoneNumber(userDTO.getPhoneNumber());
        newUser.setAddressId(address);
        newUser.setMembershipCardId(membershipCard);
        newUser.setPassword(securityConfig.delegatingPasswordEncoder().encode(userDTO.getPassword()));
        newUser.setVerified(false);
        newUser.setVerificationToken(verificationToken);

        if (newUser.getRoles() != null) {
            currentRoles.addAll(newUser.getRoles());
        }
        newUser.setRoles(currentRoles);

        userRepository.save(newUser);
        emailService.sendRegistrationEmail(newUser.getEmail(), verificationToken);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Initiates the password recovery process for a user.
     * Generates a reset password token, saves it to the user, and sends a password reset email.
     *
     * @param email The email address of the user requesting password reset.
     * @return A {@link ResponseEntity} with HTTP status OK if the email is sent.
     *         Returns HTTP status BAD_REQUEST if the email is null or empty.
     *         Returns HTTP status NOT_FOUND if no user is found with the given email.
     */
    public ResponseEntity<?> forgotPassword(String email) {
        if (email == null || email.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser = userRepository.findUserByEmail(email); // Changed from findUserByUsername to findUserByEmail as per common practice for password reset

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String resetPasswordToken = generateVerificationToken();

        User user = optionalUser.get();
        user.setResetPasswordToken(resetPasswordToken);
        userRepository.save(user);

        emailService.sendForgotPasswordEmail(email, resetPasswordToken);
        return ResponseEntity.ok().build();
    }

    /**
     * Verifies a user's email address using a verification code.
     * Checks if the code is valid and has not expired.
     *
     * @param email The email address of the user to verify.
     * @param verificationCode The verification code sent to the user's email.
     * @return A {@link ResponseEntity} with HTTP status OK if verification is successful.
     *         Returns HTTP status BAD_REQUEST if email or verification code is null or empty.
     *         Returns HTTP status NOT_FOUND if no user is found with the given email.
     *         Returns HTTP status UNAUTHORIZED if the verification code is invalid or expired.
     */
    @Transactional
    public ResponseEntity<?> verifyEmail(String email, String verificationCode) {
        if (email == null || email.isEmpty() || verificationCode == null || verificationCode.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();
        if (user.getVerificationToken() == null || !user.getVerificationToken().substring(0, 36).equals(verificationCode)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Date emailSentDate = Date.from(Instant.ofEpochMilli(Long.parseLong(user.getVerificationToken().substring(36))));
        Instant tokenInstantTime = emailSentDate.toInstant().plus(5, ChronoUnit.MINUTES); // Assuming 5-minute expiry

        if (tokenInstantTime.isBefore(Instant.now())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        user.setVerified(true);
        user.setVerificationToken(null); // Clear token after successful verification
        userRepository.save(user);
        emailService.sendSuccessfulVerificationEmail(user.getEmail());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Resets a user's password using a reset token.
     *
     * @param email The email address of the user.
     * @param token The password reset token sent to the user's email.
     * @param password The new password.
     * @param confirmPassword The confirmation of the new password.
     * @return A {@link ResponseEntity} with HTTP status OK if the password is reset successfully.
     *         Returns HTTP status BAD_REQUEST if password fields are empty or do not match.
     *         Returns HTTP status NOT_FOUND if no user is found with the given email.
     *         Returns HTTP status UNAUTHORIZED if the reset token is invalid.
     */
    @Transactional
    public ResponseEntity<?> resetPassword(String email, String token, String password, String confirmPassword) {
        if (password == null || password.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!password.equals(confirmPassword)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();

        if (user.getResetPasswordToken() == null || !user.getResetPasswordToken().equals(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(securityConfig.delegatingPasswordEncoder().encode(password));
        user.setResetPasswordToken(null);
        userRepository.save(user); // Ensure user is saved after password change
        emailService.passwordChanged(user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @return A {@link ResponseEntity} with HTTP status NO_CONTENT if deletion is successful,
     *         or HTTP status NOT_FOUND if the user does not exist.
     */
    @Transactional
    public ResponseEntity<User> deleteUser(Long id) { // Return type could be ResponseEntity<Void> for NO_CONTENT
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Edits an existing user's details by their ID using information from a {@link UserDTO}.
     * Note: This method directly sets the password from UserDTO if provided, which might be insecure.
     * Consider having a separate password change mechanism.
     *
     * @param id The ID of the user to edit.
     * @param userDTO The {@link UserDTO} containing the updated user information.
     * @return A {@link ResponseEntity} containing the updated {@link UserDTO} and HTTP status OK if successful,
     *         or HTTP status NOT_FOUND if the user does not exist.
     */
    @Transactional
    public ResponseEntity<?> editUserDtoById(Long id, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();

        user.setUserName(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        // Directly setting password from DTO is generally not recommended without hashing.
        // Assuming userDTO.getPassword() is either plain text to be hashed or already hashed.
        // If plain text, it should be: user.setPassword(securityConfig.delegatingPasswordEncoder().encode(userDTO.getPassword()));
        // For now, matching existing logic:
        if (userDTO.getPassword() != null && !userDTO.getPassword().equals("This is classified information.") && !userDTO.getPassword().isEmpty()) {
             user.setPassword(securityConfig.delegatingPasswordEncoder().encode(userDTO.getPassword()));
        }
        user.setEmail(userDTO.getEmail());
        if (user.getAddressId() != null && userDTO.getStreet() != null) user.getAddressId().setStreet(userDTO.getStreet());
        if (user.getAddressId() != null && userDTO.getCity() != null) user.getAddressId().setCity(userDTO.getCity());
        if (user.getAddressId() != null && userDTO.getPostalCode() != null) user.getAddressId().setPostalCode(userDTO.getPostalCode());
        if (user.getAddressId() != null && userDTO.getDoorRingBellName() != null) user.getAddressId().setDoorRingBellName(userDTO.getDoorRingBellName());
        if (userDTO.getPhoneNumber() != null) user.setPhoneNumber(userDTO.getPhoneNumber()); // Corrected from user.getPhoneNumber() to userDTO.getPhoneNumber()

        if (user.getAddressId() != null && userDTO.getNumber() != null) user.getAddressId().setNumber(userDTO.getNumber());
        if (user.getAddressId() != null && userDTO.getStoryLevel() != null) user.getAddressId().setStoryLevel(userDTO.getStoryLevel());
        
        userRepository.save(user); // Save the updated user entity

        // Return the DTO representation of the updated user
        UserDTO updatedUserDTO = UtilClass.convertUserToUserDTO(user);
        return ResponseEntity.ok(updatedUserDTO);
    }

    /**
     * Generates a unique verification token composed of a UUID and a timestamp.
     *
     * @return A string representing the verification token.
     */
    private String generateVerificationToken() {
        return UUID.randomUUID().toString() + String.valueOf(new Date().toInstant().toEpochMilli());
    }

    /**
     * Deletes expired verification or password reset tokens for a given user.
     * An email verification token is considered expired if it's older than 30 minutes and the user is not yet verified.
     * A password reset token is cleared if it exists.
     *
     * @param user The user whose tokens are to be checked and potentially deleted.
     */
    @Transactional
    public void deleteVerificationCode(User user) {
        User updatedUser = userRepository.findById(user.getId()).orElse(null);

        if (updatedUser != null) {
            if (updatedUser.getVerificationToken() != null && !updatedUser.isVerified()) {
                String code = updatedUser.getVerificationToken();
                long timestamp = Long.parseLong(code.substring(36));
                LocalDateTime creationDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());

                if (creationDateTime.plusMinutes(30).isBefore(LocalDateTime.now())) { // 30-minute expiry for verification token
                    updatedUser.setVerificationToken(null);
                    userRepository.save(updatedUser);
                }
            }
            // Password reset tokens are often single-use or have their own expiry logic,
            // here it's being cleared if it exists.
            // The original code also cleared resetPasswordToken here, so keeping that logic.
            if (updatedUser.getResetPasswordToken() != null) {
                 // Check expiry for reset token if needed, for now, clearing it if present
                // Example: if (resetTokenCreationTime.plusMinutes(15).isBefore(LocalDateTime.now()))
                updatedUser.setResetPasswordToken(null);
                userRepository.save(updatedUser);
            }
        }
    }

    /**
     * Scheduled task that runs every minute to clean up expired verification codes for all users.
     * If a user's email verification token is older than 30 minutes and they are not verified,
     * the token is deleted and a notification email is sent.
     * Also clears any existing password reset tokens for users.
     */
    @Scheduled(fixedRate = 60 * 1000) // Runs every 60 seconds
    @Transactional
    public void cleanUpExpiredVerificationCodes() {
        LocalDateTime now = LocalDateTime.now();
        List<User> users = userRepository.findAll();

        for (User user : users) {
            boolean modified = false;
            if (user.getVerificationToken() != null && !user.isVerified()) {
                String code = user.getVerificationToken();
                // Ensure the token string is long enough before substring
                if (code.length() > 36) {
                    try {
                        long timestamp = Long.parseLong(code.substring(36));
                        LocalDateTime creationDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());

                        if (creationDateTime.plusMinutes(30).isBefore(now)) { // 30-minute expiry
                            emailService.sendNotificationDeletedCode(user.getEmail());
                            user.setVerificationToken(null);
                            modified = true;
                        }
                    } catch (NumberFormatException e) {
                        // Log invalid token format if necessary
                        user.setVerificationToken(null); // Clear malformed token
                        modified = true;
                    }
                } else {
                     user.setVerificationToken(null); // Clear malformed token
                     modified = true;
                }
            }
            // Password reset tokens might also have an expiry, but current logic clears them if found during this scan
            if (user.getResetPasswordToken() != null) {
                user.setResetPasswordToken(null);
                modified = true;
            }

            if (modified) {
                userRepository.save(user);
            }
        }
    }
}
