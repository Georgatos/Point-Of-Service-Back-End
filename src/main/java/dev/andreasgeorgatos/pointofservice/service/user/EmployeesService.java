package dev.andreasgeorgatos.pointofservice.service.user;

import dev.andreasgeorgatos.pointofservice.utils.UtilClass;
import dev.andreasgeorgatos.pointofservice.dto.users.EmployeeDTO;
import dev.andreasgeorgatos.pointofservice.dto.users.UserDTO;
import dev.andreasgeorgatos.pointofservice.model.user.User;
import dev.andreasgeorgatos.pointofservice.repository.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeesService {

    private final UserRepository userRepository;

    @Autowired
    public EmployeesService(UserRepository userRepository) {
        this.userRepository = userRepository;
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


    public ResponseEntity<?> getEmployeeByUserId(long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserDTO userDTO = UtilClass.convertUserToUserDTO(user.get());

        return ResponseEntity.ok(userDTO);
    }
}