package dev.andreasgeorgatos.pointofservice.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class UserDTO {
    private String firstName;
    private String lastName;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDoorRingBellName() {
        return doorRingBellName;
    }

    public void setDoorRingBellName(String doorRingBellName) {
        this.doorRingBellName = doorRingBellName;
    }

    public long getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(long addressNumber) {
        this.addressNumber = addressNumber;
    }

    public long getStoryLevel() {
        return storyLevel;
    }

    public void setStoryLevel(long storyLevel) {
        this.storyLevel = storyLevel;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
