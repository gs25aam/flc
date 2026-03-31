package com.flc.domain;

import java.util.Arrays;

public enum LessonSlot {
    MORNING("Morning"),
    AFTERNOON("Afternoon"),
    EVENING("Evening");

    private final String displayName;

    LessonSlot(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    public static LessonSlot fromDisplayName(String value) {
        return Arrays.stream(values())
                .filter(slot -> slot.displayName.equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown lesson slot: " + value));
    }
}
