package dev.andreasgeorgatos.pointofservice.model.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.andreasgeorgatos.pointofservice.enums.TableStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "dine_table")
public class DineInTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "table_number", unique = true)
    private int tableNumber;

    @Column(name = "status")
    private TableStatus status;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate updatedAt;
}
