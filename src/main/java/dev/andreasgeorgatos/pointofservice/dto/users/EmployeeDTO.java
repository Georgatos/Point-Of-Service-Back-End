package dev.andreasgeorgatos.pointofservice.dto.users;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeDTO {
    private long id;
    private String userName;
    private List<String> roles;
}
