package dev.andreasgeorgatos.pointofservice.service.user;

import dev.andreasgeorgatos.pointofservice.DTO.UserDTO;
import dev.andreasgeorgatos.pointofservice.configuration.SecurityConfig;
import dev.andreasgeorgatos.pointofservice.model.address.Address;
import dev.andreasgeorgatos.pointofservice.model.rewards.MembershipCard;
import dev.andreasgeorgatos.pointofservice.model.user.User;
import dev.andreasgeorgatos.pointofservice.model.user.UserType;
import dev.andreasgeorgatos.pointofservice.repository.rewards.MembershipCardRepository;
import dev.andreasgeorgatos.pointofservice.repository.users.AddressRepository;
import dev.andreasgeorgatos.pointofservice.repository.users.UserRepository;
import dev.andreasgeorgatos.pointofservice.repository.users.UserTypeRepository;
import dev.andreasgeorgatos.pointofservice.service.email.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final SecurityConfig securityConfig;
    private final MembershipCardRepository membershipCardRepository;
    private final AddressRepository addressRepository;
    private final EmailService emailService;
    private final AddressService addressService;

    @Autowired
    public UserService(UserRepository userRepository, UserTypeRepository userTypeRepository, SecurityConfig securityConfig, MembershipCardRepository membershipCardRepository, AddressRepository addressRepository, EmailService emailService, AddressService addressService) {
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
        this.securityConfig = securityConfig;
        this.membershipCardRepository = membershipCardRepository;
        this.addressRepository = addressRepository;
        this.emailService = emailService;
        this.addressService = addressService;
    }

    public ResponseEntity<?> getUserDTO(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserDTO userDTO = new UserDTO();

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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        User user = userOptional.get();

        return new TsilikosUserDetails(user);
    }

    public boolean isSameUser(String principal, long id) {
        Optional<User> optionalUser = userRepository.findUserByEmail(principal);

        if (optionalUser.isEmpty()) {
            return false;
        }
        User user = optionalUser.get();
        return user.getId() == id;
    }

    public Map<String, Object> getClaims(TsilikosUserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        List<String> authoritiesNames = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        claims.put("auth", authoritiesNames);

        return claims;
    }

    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(users);
    }

    public boolean loginUser(String email, String password) {
        if (userRepository.findUserByEmail(email).isEmpty()) {
            return false;
        }
        User user = userRepository.findUserByEmail(email).get();
        return securityConfig.delegatingPasswordEncoder().matches(password, user.getPassword());
    }

    @Transactional
    public ResponseEntity<?> registerUser(UserDTO userDTO) {

        if (userDTO == null || userRepository.findUserByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Optional<UserType> userType = userTypeRepository.findById(1L);

        if (userType.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The user type is empty, please specify.");
        }

        Optional<MembershipCard> optionalMembershipCard = membershipCardRepository.findById(1L);

        MembershipCard membershipCard = null;
        if (!optionalMembershipCard.isEmpty()) {
            membershipCard = optionalMembershipCard.get();
        }

        Address address = addressService.createAddress(new Address(userDTO.getCity(), userDTO.getAddress(), userDTO.getAddressNumber(), userDTO.getPostalCode(), userDTO.getStoryLevel(), userDTO.getDoorRingBellName())).getBody();

        if (address == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The address is empty, please specify.: " + address);
        }

        String verificationToken = generateVerificationToken();

        User newUser = new User();

        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setBirthDate(userDTO.getBirthDate());
        newUser.setPhoneNumber(userDTO.getPhoneNumber());
        newUser.setAddress(address);
        newUser.setMembershipCard(membershipCard);
        newUser.setUserType(userType.get());
        newUser.setPassword(securityConfig.delegatingPasswordEncoder().encode(userDTO.getPassword()));
        newUser.setVerified(false);
        newUser.setVerificationToken(verificationToken);

        userRepository.save(newUser);
        emailService.sendRegistrationEmail(newUser.getEmail(), verificationToken);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
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

        return new ResponseEntity<>(user, HttpStatus.OK);
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
    public ResponseEntity<User> editUserById(Long id, User editedUser) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User oldUser = optionalUser.get();

        oldUser.setUserType(editedUser.getUserType());
        oldUser.setPhoneNumber(editedUser.getPhoneNumber());
        oldUser.setPassword(securityConfig.delegatingPasswordEncoder().encode(editedUser.getPassword()));
        oldUser.setFirstName(editedUser.getFirstName());
        oldUser.setLastName(editedUser.getLastName());

        return ResponseEntity.ok(oldUser);
    }

    private String generateVerificationToken() {
        return UUID.randomUUID() + String.valueOf(new Date().toInstant().toEpochMilli());
    }
}
