package com.flc.ui.cli;

import com.flc.domain.ExerciseType;

import java.io.PrintStream;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.Locale;
import java.util.Scanner;

public final class ConsolePrompts {
    private final Scanner scanner;
    private final PrintStream out;

    public ConsolePrompts(Scanner scanner, PrintStream out) {
        this.scanner = scanner;
        this.out = out;
    }

    public String readRequired(String label) {
        while (true) {
            out.print(label + ": ");
            String value = scanner.nextLine().trim();
            if (!value.isBlank()) {
                return value;
            }
            out.println("Value cannot be blank.");
        }
    }

    public int readMenuChoice() {
        while (true) {
            out.print("Choose an option: ");
            String value = scanner.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                out.println("Please enter a valid number.");
            }
        }
    }

    public int readRating() {
        while (true) {
            out.print("Rating (1-5): ");
            String value = scanner.nextLine().trim();
            try {
                int rating = Integer.parseInt(value);
                if (rating >= 1 && rating <= 5) {
                    return rating;
                }
            } catch (NumberFormatException ignored) {
            }
            out.println("Please enter a number from 1 to 5.");
        }
    }

    public DayOfWeek readWeekendDay() {
        while (true) {
            out.print("Day (Saturday/Sunday): ");
            String value = scanner.nextLine().trim().toUpperCase(Locale.UK);
            if ("SATURDAY".equals(value)) {
                return DayOfWeek.SATURDAY;
            }
            if ("SUNDAY".equals(value)) {
                return DayOfWeek.SUNDAY;
            }
            out.println("Only Saturday or Sunday are valid choices.");
        }
    }

    public ExerciseType readExerciseType() {
        while (true) {
            out.print("Exercise (" + availableExerciseTypes() + "): ");
            String value = scanner.nextLine().trim();
            try {
                return ExerciseType.fromDisplayName(value);
            } catch (IllegalArgumentException exception) {
                out.println(exception.getMessage());
            }
        }
    }

    public Month readMonth() {
        while (true) {
            out.print("Month number (1-12): ");
            String value = scanner.nextLine().trim();
            try {
                return Month.of(Integer.parseInt(value));
            } catch (Exception exception) {
                out.println("Please enter a valid month number.");
            }
        }
    }

    public String readOptional(String label) {
        out.print(label + ": ");
        return scanner.nextLine().trim();
    }

    private String availableExerciseTypes() {
        StringBuilder builder = new StringBuilder();
        for (ExerciseType exerciseType : ExerciseType.values()) {
            if (!builder.isEmpty()) {
                builder.append(", ");
            }
            builder.append(exerciseType.displayName());
        }
        return builder.toString();
    }
}
