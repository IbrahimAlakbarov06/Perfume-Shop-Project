package org.perfume.enums;

public enum FragranceFamily {
    FLORAL("Floral"),
    ORIENTAL("Oriental"),
    WOODY("Woody"),
    FRESH("Fresh"),
    FRUITY("Fruity"),
    SPICY("Spicy"),
    CITRUS("Citrus"),
    AQUATIC("Aquatic"),
    GREEN("Green"),
    GOURMAND("Gourmand");

    private final String displayName;

    FragranceFamily(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}