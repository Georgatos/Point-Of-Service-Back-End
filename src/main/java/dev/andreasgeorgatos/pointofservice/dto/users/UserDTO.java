package dev.andreasgeorgatos.pointofservice.dto.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
public class UserDTO {

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private String city;
    private String street;
    private String postalCode;
    private String doorRingBellName;
    private String phoneNumber;
    private String number;

    private long storyLevel;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}
