package dev.andreasgeorgatos.pointofservice.dto.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private String city;
    private String address;
    private String postalCode;
    private String doorRingBellName;
    private String phoneNumber;

    private long addressNumber;
    private long storyLevel;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}
