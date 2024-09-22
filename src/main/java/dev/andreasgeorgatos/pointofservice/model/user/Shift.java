package dev.andreasgeorgatos.pointofservice.model.user;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
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
}
