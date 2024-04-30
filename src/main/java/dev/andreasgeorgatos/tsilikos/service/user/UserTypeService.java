package dev.andreasgeorgatos.tsilikos.service.user;

import dev.andreasgeorgatos.tsilikos.model.user.UserType;
import dev.andreasgeorgatos.tsilikos.repository.users.UserTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserTypeService {

    private final UserTypeRepository userTypeRepository;

    @Autowired
    public UserTypeService(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    public ResponseEntity<UserType> createUserType(UserType userType) {
        UserType savedUserType = userTypeRepository.save(userType);
        return ResponseEntity.ok(savedUserType);
    }

    public ResponseEntity<List<UserType>> getAllUserTypes() {
        List<UserType> userTypes = userTypeRepository.findAll();

        if (!userTypes.isEmpty()) {
            return ResponseEntity.ok(userTypes);
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<UserType> getUserTypeById(long id) {
        Optional<UserType> userType = userTypeRepository.findById(id);

        return userType.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @Transactional
    public ResponseEntity<UserType> editUserType(Long id, String newUserType) {
        Optional<UserType> searchUserType = userTypeRepository.findById(id);

        if (searchUserType.isPresent() && (newUserType != null && !newUserType.isEmpty())) {
            UserType userType = searchUserType.get();

            userType.setUserType(newUserType);
            userTypeRepository.save(userType);

            return ResponseEntity.ok(userType);
        }

        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<UserType> deleteUserType(Long id) {
        Optional<UserType> searchUserType = userTypeRepository.findById(id);

        if (searchUserType.isPresent()) {
            userTypeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }


}
