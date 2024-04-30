package dev.andreasgeorgatos.tsilikos.service.user;

import dev.andreasgeorgatos.tsilikos.configuration.SecurityConfig;
import dev.andreasgeorgatos.tsilikos.model.address.Address;
import dev.andreasgeorgatos.tsilikos.model.rewards.MembershipCard;
import dev.andreasgeorgatos.tsilikos.model.user.User;
import dev.andreasgeorgatos.tsilikos.model.user.UserType;
import dev.andreasgeorgatos.tsilikos.repository.rewards.MembershipCardRepository;
import dev.andreasgeorgatos.tsilikos.repository.users.AddressRepository;
import dev.andreasgeorgatos.tsilikos.repository.users.UserRepository;
import dev.andreasgeorgatos.tsilikos.repository.users.UserTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final SecurityConfig securityConfig;
    private final MembershipCardRepository membershipCardRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserTypeRepository userTypeRepository, SecurityConfig securityConfig, MembershipCardRepository membershipCardRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
        this.securityConfig = securityConfig;
        this.membershipCardRepository = membershipCardRepository;
        this.addressRepository = addressRepository;
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

    public ResponseEntity<User> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public boolean loginUser(String email, String password) {
        if (userRepository.findUserByEmail(email).isEmpty()) {
            return false;
        }
        User user = userRepository.findUserByEmail(email).get();
        return securityConfig.delegatingPasswordEncoder().matches(password, user.getPassword());
    }

    @Transactional
    public ResponseEntity<User> registerUser(User user) {
        Optional<UserType> userType = userTypeRepository.findById(user.getUserType().getId());

        Optional<MembershipCard> optionalMembershipCard = membershipCardRepository.findById(user.getMembershipCard().getId());

        Optional<List<Address>> optionalAddress = Optional.of(user.getAddress().stream()
                .map(address -> addressRepository.findById(address.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList()));

        if (userType.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (optionalAddress.get().isEmpty()) {
            user.setAddress(null);
        } else {
            user.setAddress(optionalAddress.get());
        }

        if (optionalMembershipCard.isEmpty()) {
            user.setMembershipCard(null);
        } else {
            user.setMembershipCard(optionalMembershipCard.get());
        }

        user.setUserType(userType.get());
        user.setPassword(securityConfig.delegatingPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.CREATED);

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


}
