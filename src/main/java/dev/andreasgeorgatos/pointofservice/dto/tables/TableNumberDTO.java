package dev.andreasgeorgatos.pointofservice.dto.tables;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TableNumberDTO {
    @NotNull
    private long tableNumber;
}
