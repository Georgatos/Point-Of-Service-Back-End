package dev.andreasgeorgatos.pointofservice.enums;

import lombok.Getter;

@Getter
public enum CategoryType {

    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    DESERT("Desert"),
    Meat("Meat"),
    Pita("Pita"),
    Burger("Burger"),
    SANDWICH("Sandwich"),
    FRIES("Fries"),
    SAUCES("Sauces");

    private final String category;

    CategoryType(String category) {
        this.category = category;
    }
}
