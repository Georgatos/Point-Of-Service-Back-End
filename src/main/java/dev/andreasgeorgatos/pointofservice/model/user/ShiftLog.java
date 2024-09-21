package dev.andreasgeorgatos.pointofservice.model.user;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "shifts_log")
public class ShiftLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shifts_id")
    private long id;

    @Column(name = "trigger_action_at")
    private LocalDate triggeredActionAt;

    @Column(name = "action")
    private String action;

    @ManyToOne
    @JoinColumn(name = "performed_by")
    private User performedBy;

    @Column(name = "method")
    private String method;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;
}
