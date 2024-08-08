package dev.andreasgeorgatos.pointofservice.enums;

import lombok.Getter;

@Getter
public enum Allergenes {
    GLUTEN("Gluten"),
    DAIRY("Dairy"),
    EGGS("eggs"),
    SOY("Soy"),
    FISH("Fish"),
    SHELLFISH("Shell fish"),
    PEANUTS("Peanuts"),
    TREE_NUTS("Tree nuts"),
    WHEAT("Wheat"),
    SESAME("Sesame"),
    MUSTARD("Mustard"),
    SULFITES("Sulfites");

    private String value;

    Allergenes(String value) {
        this.value = value;
    }
}
