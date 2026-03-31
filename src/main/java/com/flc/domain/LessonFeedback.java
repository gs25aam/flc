package com.flc.domain;

public record LessonFeedback(int rating, String review) {
    public LessonFeedback {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        if (review == null) {
            throw new IllegalArgumentException("Review must not be null.");
        }
    }
}
