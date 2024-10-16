package dev.andreasgeorgatos.pointofservice.model.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "shifts")
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shifts_id")
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role roleId;

    @Column(name = "shift_start")
    private LocalDate shiftStart;


    @Column(name = "shift_end")
    private LocalDate shiftEnd;

    @ManyToOne
    @JoinColumn(name = "started_by")
    private User startedBy;

    @Column(name = "started_by_method")
    private String startedByMethod;

    @ManyToOne
    @JoinColumn(name = "ended_by")
    private User endedBy;

    @Column(name = "ended_by_method")
    private String endedByMethod;

    @OneToMany
    @Column(name = "past_shifts_id")
    private Set<ShiftLog> pastShiftLogsId;

    public Shift(User userId, Role roleId, LocalDate shiftStart, LocalDate shiftEnd, User startedBy, String startedByMethod, User endedBy, String endedByMethod, Set<ShiftLog> pastShiftLogsId) {
        this.userId = userId;
        this.roleId = roleId;
        this.shiftStart = shiftStart;
        this.shiftEnd = shiftEnd;
        this.startedBy = startedBy;
        this.startedByMethod = startedByMethod;
        this.endedBy = endedBy;
        this.endedByMethod = endedByMethod;
        this.pastShiftLogsId = pastShiftLogsId;
    }
}
