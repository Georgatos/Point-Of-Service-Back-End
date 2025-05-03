package dev.andreasgeorgatos.pointofservice.utils;

import dev.andreasgeorgatos.pointofservice.dto.users.UserDTO;
import dev.andreasgeorgatos.pointofservice.model.user.User;

public class UtilClass {
    private UtilClass() {
    }
    public static UserDTO convertUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setUserName(user.getUserName());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPassword("This is classified information.");
        userDTO.setEmail(user.getEmail());
        userDTO.setCountry(user.getAddressId().getCountry());
        userDTO.setStreet(user.getAddressId().getStreet());
        userDTO.setCity(user.getAddressId().getCity());
        userDTO.setPostalCode(user.getAddressId().getPostalCode());
        userDTO.setDoorRingBellName(user.getAddressId().getPostalCode());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setNumber(user.getAddressId().getNumber());
        userDTO.setStoryLevel(user.getAddressId().getStoryLevel());

        userDTO.setBirthDate(user.getBirthDate());

        return userDTO;
    }

}
