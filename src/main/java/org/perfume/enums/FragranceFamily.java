package org.perfume.enums;

public enum FragranceFamily {
    CITRUS("Citrus"),
    FLORAL("Floral"),
    ORIENTAL("Oriental"),
    WOODY("Woody"),
    FRESH("Fresh"),
    AROMATIC("Aromatic"),
    FOUGERE("Fougère"),
    CHYPRE("Chypre"),
    GOURMAND("Gourmand"),
    LEATHER("Leather"),
    AQUATIC("Aquatic"),
    GREEN("Green"),
    POWDERY("Powdery"),
    MUSKY("Musky"),
    SPICY("Spicy");

    private final String displayName;

    FragranceFamily(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}