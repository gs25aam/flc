package com.flc.service;

import com.flc.domain.Booking;
import com.flc.domain.BookingStatus;
import com.flc.domain.ExerciseType;
import com.flc.domain.LessonSlot;
import com.flc.exception.InvalidRatingException;
import com.flc.support.ServiceTestContext;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AttendanceServiceTest {
    @Test
    void attendBookingStoresFeedbackAndStatus() {
        ServiceTestContext context = new ServiceTestContext();
        context.addMember("M001");
        context.addLesson("L001", LocalDate.of(2026, 5, 2), LessonSlot.MORNING, ExerciseType.YOGA);

        Booking booking = context.bookingService().bookLesson("M001", "L001");
        Booking attendedBooking = context.attendanceService().attendBooking(booking.id(), 4, "Great session.");

        assertEquals(BookingStatus.ATTENDED, attendedBooking.status());
        assertEquals(4, attendedBooking.feedback().rating());
        assertEquals("Great session.", attendedBooking.feedback().review());
    }

    @Test
    void attendBookingRejectsRatingBelowRange() {
        ServiceTestContext context = new ServiceTestContext();
        context.addMember("M001");
        context.addLesson("L001", LocalDate.of(2026, 5, 2), LessonSlot.MORNING, ExerciseType.YOGA);
        Booking booking = context.bookingService().bookLesson("M001", "L001");

        assertThrows(
                InvalidRatingException.class,
                () -> context.attendanceService().attendBooking(booking.id(), 0, "Invalid.")
        );
    }

    @Test
    void attendBookingRejectsRatingAboveRange() {
        ServiceTestContext context = new ServiceTestContext();
        context.addMember("M001");
        context.addLesson("L001", LocalDate.of(2026, 5, 2), LessonSlot.MORNING, ExerciseType.YOGA);
        Booking booking = context.bookingService().bookLesson("M001", "L001");

        assertThrows(
                InvalidRatingException.class,
                () -> context.attendanceService().attendBooking(booking.id(), 6, "Invalid.")
        );
    }
}
