package dev.andreasgeorgatos.pointofservice.model.user;

import dev.andreasgeorgatos.pointofservice.enums.EmployeeStatus;
import dev.andreasgeorgatos.pointofservice.model.order.DineInTable;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "server_assignments")
public class ServerAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "server_assignment_id")
    private long id;


    @OneToOne
    @JoinColumn(name = "server_id")
    private User serverId;

    @OneToOne
    @JoinColumn(name = "table_id")
    private DineInTable tableId;

    @Column(name = "assignment_date")
    private LocalDate assignmentDate;

    @Column
    private EmployeeStatus status;

}
