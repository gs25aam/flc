package com.flc.domain;

import com.flc.exception.BookingStateException;

public final class Booking {
    private final String id;
    private final String memberId;
    private final String lessonId;
    private final BookingStatus status;
    private final LessonFeedback feedback;

    public Booking(String id, String memberId, String lessonId, BookingStatus status) {
        this(id, memberId, lessonId, status, null);
    }

    private Booking(String id, String memberId, String lessonId, BookingStatus status, LessonFeedback feedback) {
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
        if (status == BookingStatus.ATTENDED && feedback == null) {
            throw new IllegalArgumentException("Attended bookings must include lesson feedback.");
        }
        if (status != BookingStatus.ATTENDED && feedback != null) {
            throw new IllegalArgumentException("Only attended bookings can include lesson feedback.");
        }
        this.id = id;
        this.memberId = memberId;
        this.lessonId = lessonId;
        this.status = status;
        this.feedback = feedback;
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

    public Booking changeLesson(String newLessonId) {
        ensureCanBeUpdated("changed");
        return new Booking(id, memberId, newLessonId, BookingStatus.CHANGED, null);
    }

    public Booking cancel() {
        ensureCanBeUpdated("cancelled");
        return new Booking(id, memberId, lessonId, BookingStatus.CANCELLED, null);
    }

    public Booking attend(LessonFeedback lessonFeedback) {
        ensureCanBeUpdated("attended");
        return new Booking(id, memberId, lessonId, BookingStatus.ATTENDED, lessonFeedback);
    }

    private void ensureCanBeUpdated(String action) {
        if (!status.canBeUpdated()) {
            throw new BookingStateException(
                    "Booking %s cannot be %s because it is %s."
                            .formatted(id, action, status)
            );
        }
    }
}
