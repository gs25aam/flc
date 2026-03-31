package com.flc.domain;

import java.math.BigDecimal;
import java.util.Arrays;

public enum ExerciseType {
    YOGA("Yoga", new BigDecimal("12.00")),
    ZUMBA("Zumba", new BigDecimal("10.00")),
    AQUACISE("Aquacise", new BigDecimal("14.00")),
    BOX_FIT("Box Fit", new BigDecimal("15.00")),
    BODY_BLITZ("Body Blitz", new BigDecimal("13.00"));

    private final String displayName;
    private final BigDecimal price;

    ExerciseType(String displayName, BigDecimal price) {
        this.displayName = displayName;
        this.price = price;
    }

    public String displayName() {
        return displayName;
    }

    public BigDecimal price() {
        return price;
    }

    public static ExerciseType fromDisplayName(String value) {
        return Arrays.stream(values())
                .filter(type -> type.displayName.equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown exercise type: " + value));
    }
}
