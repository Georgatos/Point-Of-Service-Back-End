package dev.andreasgeorgatos.pointofservice.service.user;

import dev.andreasgeorgatos.pointofservice.model.user.Shift;
import dev.andreasgeorgatos.pointofservice.repository.users.ShiftRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShiftService {
    private final ShiftRepository shiftRepository;

    @Autowired
    public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
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
    public ResponseEntity<Shift> createShift(Shift shift) {
        Shift savedShift = shiftRepository.save(shift);

        return ResponseEntity.ok(savedShift);
    }

    @Transactional
    public ResponseEntity<Shift> editShiftById(long id, Shift shift) {
        Optional<Shift> foundShift = shiftRepository.findById(id);

        if (foundShift.isPresent()) {
            Shift oldShift = foundShift.get();

            oldShift.setShiftStart(shift.getShiftStart());
            oldShift.setShiftEnd(shift.getShiftEnd());
            oldShift.setEmployeeId(shift.getEmployeeId());
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
