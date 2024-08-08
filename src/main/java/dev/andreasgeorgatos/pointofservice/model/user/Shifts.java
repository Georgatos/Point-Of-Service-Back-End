package dev.andreasgeorgatos.pointofservice.model.user;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "shifts")
public class Shifts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shifts_id")
    private long id;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private User employeeId;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role roleId;

    @Column(name="shift_start")
    private LocalDate shiftStart;


    @Column(name="shift_end")
    private LocalDate shiftEnd;

}
