package dev.andreasgeorgatos.pointofservice.service.user;

import dev.andreasgeorgatos.pointofservice.configuration.SecurityConfig;
import dev.andreasgeorgatos.pointofservice.dto.users.EmployeeDTO;
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

@Service
@EnableScheduling
public class UserService implements UserDetailsService {

    private final SecurityConfig securityConfig;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MembershipCardRepository membershipCardRepository;
    private final EmailService emailService;
    private final AddressService addressService;

    @Autowired
    public UserService(SecurityConfig securityConfig, UserRepository userRepository, RoleRepository roleRepository, MembershipCardRepository membershipCardRepository, AddressRepository addressRepository, EmailService emailService, AddressService addressService) {
        this.securityConfig = securityConfig;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.membershipCardRepository = membershipCardRepository;
        this.emailService = emailService;
        this.addressService = addressService;
    }


    public ResponseEntity<?> getAllEmployees() {
        Optional<List<Object[]>> optionalEmployees = userRepository.getAllEmployees();

        if (optionalEmployees.isEmpty() || optionalEmployees.get().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<Long, EmployeeDTO> employeeMap = new HashMap<>();

        for (Object[] result : optionalEmployees.get()) {
            Long userId = (Long) result[0];
            String firstName = (String) result[1];
            String lastName = (String) result[2];
            String imageUrl = (String) result[3];
            String userName = (String) result[4];
            String role = (String) result[5];

            EmployeeDTO employeeDTO = employeeMap.get(userId);

            if (employeeDTO == null) {
                employeeDTO = new EmployeeDTO();
                employeeDTO.setUserId(userId);
                employeeDTO.setFirstName(firstName);
                employeeDTO.setLastName(lastName);
                employeeDTO.setImageUrl(imageUrl);
                employeeDTO.setUserName(userName);
                employeeDTO.setRoles(new ArrayList<>());
                employeeMap.put(userId, employeeDTO);
            }

            employeeDTO.getRoles().add(role);
        }
        return ResponseEntity.ok(employeeMap);
    }

    public ResponseEntity<?> getUserDTO(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        UserDTO userDTO = new UserDTO();

        userDTO.setUserName(user.getUserName());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPassword("This is classified information.");
        userDTO.setEmail(user.getEmail());
        userDTO.setCity(user.getAddress().getCity());
        userDTO.setAddress(user.getAddress().getAddress());
        userDTO.setPostalCode(user.getAddress().getPostalCode());
        userDTO.setDoorRingBellName(user.getAddress().getPostalCode());
        userDTO.setAddressNumber(user.getAddress().getAddressNumber());
        userDTO.setStoryLevel(user.getAddress().getStoryLevel());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setBirthDate(user.getBirthDate());

        return ResponseEntity.ok(userDTO);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByUsername(userName);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with user name: " + userName);
        }

        User user = userOptional.get();

        return new POSUser(user);
    }

    public boolean isSameUser(String principal, long id) {
        Optional<User> optionalUser = userRepository.findUserByUsername(principal);

        if (optionalUser.isEmpty()) {
            return false;
        }
        User user = optionalUser.get();
        return user.getId() == id;
    }

    public Map<String, Object> getClaims(POSUser userDetails) {

        Map<String, Object> claims = new HashMap<>();

        List<String> authoritiesNames = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        claims.put("auth", authoritiesNames);

        return claims;
    }

    public boolean loginUser(String userName, String password) {
        if (userRepository.findUserByUsername(userName).isEmpty()) {
            return false;
        }
        User user = userRepository.findUserByUsername(userName).get();
        return securityConfig.delegatingPasswordEncoder().matches(password, user.getPassword());
    }

    @Transactional
    public ResponseEntity<?> registerUser(UserDTO userDTO) {
        if (userDTO == null || userRepository.findUserByEmail(userDTO.getEmail()).isPresent() || userRepository.findUserByUsername(userDTO.getUserName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Optional<Role> potentialRole = roleRepository.findById(1L);

        if (potentialRole.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found");
        }

        List<Role> currentRoles = new ArrayList<>();
        currentRoles.add(potentialRole.get());


        Optional<MembershipCard> optionalMembershipCard = membershipCardRepository.findById(1L);

        MembershipCard membershipCard = null;

        if (optionalMembershipCard.isPresent()) {
            membershipCard = optionalMembershipCard.get();
        }

        Address address = addressService.createAddress(new Address(userDTO.getCity(), userDTO.getAddress(), userDTO.getAddressNumber(), userDTO.getPostalCode(), userDTO.getStoryLevel(), userDTO.getDoorRingBellName())).getBody();

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
        newUser.setAddress(address);
        newUser.setMembershipCard(membershipCard);
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

    public ResponseEntity<?> forgotPassword(String email) {
        if (email == null || email.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser = userRepository.findUserByUsername(email);

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
        if (!user.getVerificationToken().substring(0, 36).equals(verificationCode)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Date emailSentDate = Date.from(Instant.ofEpochMilli(Long.parseLong(user.getVerificationToken().substring(36))));

        Instant tokenInstantTime = emailSentDate.toInstant().plus(5, ChronoUnit.MINUTES);

        if (tokenInstantTime.isBefore(Instant.now())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        user.setVerified(true);
        userRepository.save(user);
        emailService.sendSuccessfulVerificationEmail(user.getEmail());

        return new ResponseEntity<>(HttpStatus.OK);
    }

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

        if (user.getResetPasswordToken() != null && !user.getResetPasswordToken().equals(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(securityConfig.delegatingPasswordEncoder().encode(password));
        user.setResetPasswordToken(null);
        emailService.passwordChanged(user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<User> deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

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
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.getAddress().setAddress(userDTO.getAddress());
        user.getAddress().setCity(userDTO.getCity());
        user.getAddress().setPostalCode(userDTO.getPostalCode());
        user.getAddress().setDoorRingBellName(userDTO.getDoorRingBellName());
        user.setPhoneNumber(user.getPhoneNumber());

        user.getAddress().setAddressNumber(userDTO.getAddressNumber());
        user.getAddress().setStoryLevel(userDTO.getStoryLevel());


        return ResponseEntity.ok(userDTO);
    }

    private String generateVerificationToken() {
        return UUID.randomUUID() + String.valueOf(new Date().toInstant().toEpochMilli());
    }

    @Transactional
    public void deleteVerificationCode(User user) {
        User updatedUser = userRepository.findById(user.getId()).orElse(null);

        if (updatedUser != null) {
            if (updatedUser.getVerificationToken() != null && !updatedUser.isVerified()) {
                String code = updatedUser.getVerificationToken();
                long timestamp = Long.parseLong(code.substring(36));
                LocalDateTime creationDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());

                if (creationDateTime.plusMinutes(30).isBefore(LocalDateTime.now())) {
                    updatedUser.setVerificationToken(null);
                    userRepository.save(updatedUser);
                }
            }
            if (updatedUser.getResetPasswordToken() != null) {
                updatedUser.setResetPasswordToken(null);
                userRepository.save(updatedUser);
            }
        }
    }

    @Scheduled(fixedRate = 60 * 1000)
    @Transactional
    public void cleanUpExpiredVerificationCodes() {
        LocalDateTime now = LocalDateTime.now();
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.getVerificationToken() != null && !user.isVerified()) {
                String code = user.getVerificationToken();
                long timestamp = Long.parseLong(code.substring(36));
                LocalDateTime creationDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());

                if (creationDateTime.plusMinutes(30).isBefore(now)) {
                    emailService.sendNotificationDeletedCode(user.getEmail());
                    deleteVerificationCode(user);
                }
            }
            if (user.getResetPasswordToken() != null) {
                deleteVerificationCode(user);
            }
        }
    }
}
