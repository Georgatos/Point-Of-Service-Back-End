package dev.andreasgeorgatos.pointofservice.enums;

public enum MembershipType {
    LOCAL("Local"),
    TOURIST("Tourist"),
    BENEFACTOR("Benefactor"),
    FAMILY("Family"),
    FRIEND("Friend"),
    EMPLOYEE("Employee");

    private final String value;

    MembershipType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
