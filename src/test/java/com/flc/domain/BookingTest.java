package com.flc.domain;

import com.flc.exception.BookingStateException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingTest {
    @Test
    void changeLessonReturnsUpdatedBookingWithoutMutatingOriginal() {
        Booking booking = new Booking("B001", "M001", "L001", BookingStatus.BOOKED);

        Booking changedBooking = booking.changeLesson("L002");

        assertEquals("L001", booking.lessonId());
        assertEquals(BookingStatus.BOOKED, booking.status());
        assertEquals("L002", changedBooking.lessonId());
        assertEquals(BookingStatus.CHANGED, changedBooking.status());
    }

    @Test
    void attendedBookingRequiresFeedback() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Booking("B001", "M001", "L001", BookingStatus.ATTENDED)
        );
    }

    @Test
    void attendedBookingCannotBeChangedAgain() {
        Booking attendedBooking = new Booking("B001", "M001", "L001", BookingStatus.BOOKED)
                .attend(new LessonFeedback(5, "Great session."));

        assertThrows(BookingStateException.class, () -> attendedBooking.changeLesson("L002"));
    }
}
