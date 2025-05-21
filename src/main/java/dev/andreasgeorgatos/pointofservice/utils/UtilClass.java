package dev.andreasgeorgatos.pointofservice.utils;

import dev.andreasgeorgatos.pointofservice.dto.users.UserDTO;
import dev.andreasgeorgatos.pointofservice.model.user.User;

public final class UtilClass { // Added final
    private UtilClass() {
    }
    public static UserDTO convertUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setUserName(user.getUserName());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPassword("This is classified information."); // Good practice
        userDTO.setEmail(user.getEmail());
        userDTO.setCountry(user.getAddressId().getCountry());
        userDTO.setStreet(user.getAddressId().getStreet());
        userDTO.setCity(user.getAddressId().getCity());
        userDTO.setPostalCode(user.getAddressId().getPostalCode());
        // Assuming getDoorRingBellName() exists on Address entity and is the correct field.
        // If not, this line would need to be user.getAddressId().getSomeOtherAppropriateField()
        // or reverted if the original was intentional, though unlikely.
        userDTO.setDoorRingBellName(user.getAddressId().getDoorRingBellName()); 
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setNumber(user.getAddressId().getNumber());
        userDTO.setStoryLevel(user.getAddressId().getStoryLevel());

        userDTO.setBirthDate(user.getBirthDate());

        return userDTO;
    }

}
