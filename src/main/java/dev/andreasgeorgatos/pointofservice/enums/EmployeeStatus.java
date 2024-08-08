package dev.andreasgeorgatos.pointofservice.enums;

import lombok.Getter;

@Getter
public enum EmployeeStatus {
    ON_CLOCK("On_clock"),
    ON_BREAK("On_break"),
    WORKING("Working"),
    RESIGNED("Resigned"),
    FIRED("Fired"),
    ON_VACATION("On_vacation");

    private final String value;

    EmployeeStatus(String value) {
        this.value = value;
    }
}
