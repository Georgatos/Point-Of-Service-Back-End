package dev.andreasgeorgatos.pointofservice.enums;

public enum TableStatus {
    OPEN("Open"),
    CLOSED("Closed"),
    RESERVED("Reserved");

    private final String value;

    TableStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}