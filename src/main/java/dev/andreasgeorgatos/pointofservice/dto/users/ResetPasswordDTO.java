package dev.andreasgeorgatos.pointofservice.dto.users;

import lombok.Data;
import lombok.Getter;

@Data
public class ResetPasswordDTO {
    private String email;
    private String password;
    private String confirmPassword;
    private String token;
}
