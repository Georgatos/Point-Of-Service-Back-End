package dev.andreasgeorgatos.pointofservice.service.user;

import dev.andreasgeorgatos.pointofservice.dto.users.ShiftDTO;
import dev.andreasgeorgatos.pointofservice.model.user.Role;
import dev.andreasgeorgatos.pointofservice.model.user.Shift;
import dev.andreasgeorgatos.pointofservice.model.user.ShiftLog;
import dev.andreasgeorgatos.pointofservice.model.user.User;
import dev.andreasgeorgatos.pointofservice.repository.users.RoleRepository;
import dev.andreasgeorgatos.pointofservice.repository.users.ShiftLogRepository;
import dev.andreasgeorgatos.pointofservice.repository.users.ShiftRepository;
import dev.andreasgeorgatos.pointofservice.repository.users.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ShiftLogRepository shiftLogRepository;

    @Autowired
    public ShiftService(ShiftRepository shiftRepository, UserRepository userRepository, RoleRepository roleRepository, ShiftLogRepository shiftLogRepository) {
        this.shiftRepository = shiftRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.shiftLogRepository = shiftLogRepository;
    }

    public ResponseEntity<List<Shift>> getAllShifts() {
        List<Shift> shifts = shiftRepository.findAll();

        if (shifts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(shifts);
    }

    public ResponseEntity<Shift> getShiftById(long id) {
        Optional<Shift> optionalShift = shiftRepository.findById(id);

        return optionalShift.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<Shift> createShift(ShiftDTO shiftDTO) {
        User employee = userRepository.getReferenceById(shiftDTO.getUserId());
        User startedBy = userRepository.getReferenceById(shiftDTO.getStartedBy());
        User endedBy = userRepository.getReferenceById(shiftDTO.getEndedBy());

        Role role = roleRepository.getReferenceById(shiftDTO.getRoleId());

        LocalDate shiftStart = shiftDTO.getShiftStart();
        LocalDate shiftEnd = shiftDTO.getShiftEnd();

        String startedByMethod = shiftDTO.getStartedByMethod();
        String endedByMethod = shiftDTO.getEndedByMethod();

        Set<ShiftLog> pastShifts = shiftDTO.getPastShifts();

        Shift shift = new Shift(employee, role, shiftStart, shiftEnd, startedBy, startedByMethod, endedBy, endedByMethod, pastShifts);

        return ResponseEntity.ok(shiftRepository.save(shift));
    }

    @Transactional
    public ResponseEntity<Shift> editShiftById(long id, Shift shift) {
        Optional<Shift> foundShift = shiftRepository.findById(id);

        if (foundShift.isPresent()) {
            Shift oldShift = foundShift.get();

            oldShift.setShiftStart(shift.getShiftStart());
            oldShift.setShiftEnd(shift.getShiftEnd());
            oldShift.setUserId(shift.getUserId());
            oldShift.setRoleId(shift.getRoleId());

            return ResponseEntity.ok(shiftRepository.save(oldShift));
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<Shift> deleteReviewById(long id) {
        Optional<Shift> optionalShift = shiftRepository.findById(id);

        if (optionalShift.isPresent()) {
            shiftRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
