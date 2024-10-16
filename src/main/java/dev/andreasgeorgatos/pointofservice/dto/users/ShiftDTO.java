package dev.andreasgeorgatos.pointofservice.dto.users;

import dev.andreasgeorgatos.pointofservice.model.user.ShiftLog;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class ShiftDTO {

    private long userId;
    private long startedBy;
    private long endedBy;
    private long roleId;
    private Set<ShiftLog> pastShifts;

    private LocalDate shiftStart;
    private LocalDate shiftEnd;

    private String startedByMethod;
    private String endedByMethod;


}
