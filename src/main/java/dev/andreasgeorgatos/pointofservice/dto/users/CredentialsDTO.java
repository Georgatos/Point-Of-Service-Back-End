package dev.andreasgeorgatos.pointofservice.dto.users;

import lombok.Data;
import lombok.Getter;

@Data
public class CredentialsDTO {
    private String userName;
    private String password;
}
