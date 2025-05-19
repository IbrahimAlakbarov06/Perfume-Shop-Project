package org.perfume.perfumebackend.enums;

public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    UNISEX("Unisex");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}