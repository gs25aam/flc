package com.flc.domain;

public final class Booking {
    private final String id;
    private final String memberId;
    private String lessonId;
    private BookingStatus status;
    private LessonFeedback feedback;

    public Booking(String id, String memberId, String lessonId, BookingStatus status) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Booking id must not be blank.");
        }
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("Member id must not be blank.");
        }
        if (lessonId == null || lessonId.isBlank()) {
            throw new IllegalArgumentException("Lesson id must not be blank.");
        }
        if (status == null) {
            throw new IllegalArgumentException("Booking status must not be null.");
        }
        this.id = id;
        this.memberId = memberId;
        this.lessonId = lessonId;
        this.status = status;
    }

    public String id() {
        return id;
    }

    public String memberId() {
        return memberId;
    }

    public String lessonId() {
        return lessonId;
    }

    public BookingStatus status() {
        return status;
    }

    public LessonFeedback feedback() {
        return feedback;
    }

    public void changeLesson(String newLessonId) {
        this.lessonId = newLessonId;
        this.status = BookingStatus.CHANGED;
        this.feedback = null;
    }

    public void cancel() {
        this.status = BookingStatus.CANCELLED;
    }

    public void attend(LessonFeedback lessonFeedback) {
        this.feedback = lessonFeedback;
        this.status = BookingStatus.ATTENDED;
    }
}
