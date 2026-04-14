package com.flc.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public record Lesson(String id, LocalDate date, LessonSlot slot, ExerciseType exerciseType, int capacity) {
    public static final int DEFAULT_CAPACITY = 4;

    public Lesson(String id, LocalDate date, LessonSlot slot, ExerciseType exerciseType) {
        this(id, date, slot, exerciseType, DEFAULT_CAPACITY);
    }

    public Lesson {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Lesson id must not be blank.");
        }
        if (date == null) {
            throw new IllegalArgumentException("Lesson date must not be null.");
        }
        if (slot == null) {
            throw new IllegalArgumentException("Lesson slot must not be null.");
        }
        if (exerciseType == null) {
            throw new IllegalArgumentException("Exercise type must not be null.");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Lesson capacity must be positive.");
        }
    }

    public DayOfWeek dayOfWeek() {
        return date.getDayOfWeek();
    }

    public Month month() {
        return date.getMonth();
    }

    public String displayLabel() {
        return String.format(
                "%s | %s | %s | %s | GBP %s",
                id,
                date,
                slot.displayName(),
                exerciseType.displayName(),
                exerciseType.price()
        );
    }
}
