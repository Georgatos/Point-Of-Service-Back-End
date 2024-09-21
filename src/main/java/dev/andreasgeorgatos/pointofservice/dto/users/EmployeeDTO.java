package dev.andreasgeorgatos.pointofservice.dto.users;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeDTO {
    private long userId;
    private String imageUrl;
    private String firstName;
    private String lastName;
    private String userName;
    private List<String> roles;
}
