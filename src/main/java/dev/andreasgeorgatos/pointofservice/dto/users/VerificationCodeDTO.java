package dev.andreasgeorgatos.pointofservice.dto.users;

import lombok.Data;
import lombok.Getter;

@Data
public class VerificationCodeDTO {
    private String email;
    private String verificationCode;

}
