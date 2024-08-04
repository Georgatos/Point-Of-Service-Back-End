package dev.andreasgeorgatos.pointofservice.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserNameDTO {
    @NotBlank(message = "UserName is mandatory.")
    private String userName;
}
