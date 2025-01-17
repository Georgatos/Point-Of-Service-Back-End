package dev.andreasgeorgatos.pointofservice.dto.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Data
public class UserNameDTO {
    @NotBlank(message = "UserName is mandatory.")
    private String userName;
}
