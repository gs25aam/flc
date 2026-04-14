package com.flc.service;

import com.flc.domain.Booking;
import com.flc.domain.BookingStatus;
import com.flc.domain.ExerciseType;
import com.flc.domain.LessonSlot;
import com.flc.exception.BookingStateException;
import com.flc.exception.CapacityExceededException;
import com.flc.exception.DuplicateBookingException;
import com.flc.exception.TimeSlotConflictException;
import com.flc.support.ServiceTestContext;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingServiceTest {
    @Test
    void bookLessonCreatesBookingWhenSpaceAvailable() {
        ServiceTestContext context = new ServiceTestContext();
        context.addMember("M001");
        context.addLesson("L001", LocalDate.of(2026, 5, 2), LessonSlot.MORNING, ExerciseType.YOGA);

        Booking booking = context.bookingService().bookLesson("M001", "L001");

        assertEquals("B001", booking.id());
        assertEquals(BookingStatus.BOOKED, booking.status());
    }

    @Test
    void bookLessonUsesNextHighestExistingBookingId() {
        ServiceTestContext context = new ServiceTestContext();
        context.addMember("M001");
        context.addMember("M002");
        context.addLesson("L001", LocalDate.of(2026, 5, 2), LessonSlot.MORNING, ExerciseType.YOGA);
        context.addBooking("B010", "M001", "L001", BookingStatus.BOOKED);

        Booking booking = context.bookingService().bookLesson("M002", "L001");

        assertEquals("B011", booking.id());
    }

    @Test
    void bookLessonRejectsWhenCapacityExceeded() {
        ServiceTestContext context = new ServiceTestContext();
        context.addLesson("L001", LocalDate.of(2026, 5, 2), LessonSlot.MORNING, ExerciseType.YOGA);
        for (int index = 1; index <= 5; index++) {
            context.addMember("M%03d".formatted(index));
        }

        context.bookingService().bookLesson("M001", "L001");
        context.bookingService().bookLesson("M002", "L001");
        context.bookingService().bookLesson("M003", "L001");
        context.bookingService().bookLesson("M004", "L001");

        assertThrows(
                CapacityExceededException.class,
                () -> context.bookingService().bookLesson("M005", "L001")
        );
    }

    @Test
    void bookLessonRejectsDuplicateBookings() {
        ServiceTestContext context = new ServiceTestContext();
        context.addMember("M001");
        context.addLesson("L001", LocalDate.of(2026, 5, 2), LessonSlot.MORNING, ExerciseType.YOGA);

        context.bookingService().bookLesson("M001", "L001");

        assertThrows(
                DuplicateBookingException.class,
                () -> context.bookingService().bookLesson("M001", "L001")
        );
    }

    @Test
    void bookLessonRejectsSameTimeslotConflict() {
        ServiceTestContext context = new ServiceTestContext();
        context.addMember("M001");
        context.addLesson("L001", LocalDate.of(2026, 5, 2), LessonSlot.MORNING, ExerciseType.YOGA);
        context.addLesson("L002", LocalDate.of(2026, 5, 2), LessonSlot.MORNING, ExerciseType.BOX_FIT);

        context.bookingService().bookLesson("M001", "L001");

        assertThrows(
                TimeSlotConflictException.class,
                () -> context.bookingService().bookLesson("M001", "L002")
        );
    }

    @Test
    void changeBookingKeepsSameBookingId() {
        ServiceTestContext context = new ServiceTestContext();
        context.addMember("M001");
        context.addLesson("L001", LocalDate.of(2026, 5, 2), LessonSlot.MORNING, ExerciseType.YOGA);
        context.addLesson("L002", LocalDate.of(2026, 5, 3), LessonSlot.AFTERNOON, ExerciseType.ZUMBA);

        Booking originalBooking = context.bookingService().bookLesson("M001", "L001");
        Booking changedBooking = context.bookingService().changeBooking(originalBooking.id(), "L002");

        assertEquals(originalBooking.id(), changedBooking.id());
        assertEquals("L002", changedBooking.lessonId());
        assertEquals(BookingStatus.CHANGED, changedBooking.status());
    }

    @Test
    void cancelledBookingFreesCapacityForAnotherMember() {
        ServiceTestContext context = new ServiceTestContext();
        context.addLesson("L001", LocalDate.of(2026, 5, 2), LessonSlot.MORNING, ExerciseType.YOGA);
        for (int index = 1; index <= 5; index++) {
            context.addMember("M%03d".formatted(index));
        }

        Booking cancelledBooking = context.bookingService().bookLesson("M001", "L001");
        context.bookingService().bookLesson("M002", "L001");
        context.bookingService().bookLesson("M003", "L001");
        context.bookingService().bookLesson("M004", "L001");
        context.bookingService().cancelBooking(cancelledBooking.id());

        Booking newBooking = context.bookingService().bookLesson("M005", "L001");

        assertEquals("B005", newBooking.id());
    }

    @Test
    void changeBookingRejectsCancelledBookings() {
        ServiceTestContext context = new ServiceTestContext();
        context.addMember("M001");
        context.addLesson("L001", LocalDate.of(2026, 5, 2), LessonSlot.MORNING, ExerciseType.YOGA);
        context.addLesson("L002", LocalDate.of(2026, 5, 3), LessonSlot.AFTERNOON, ExerciseType.ZUMBA);

        Booking booking = context.bookingService().bookLesson("M001", "L001");
        context.bookingService().cancelBooking(booking.id());

        assertThrows(
                BookingStateException.class,
                () -> context.bookingService().changeBooking(booking.id(), "L002")
        );
    }

    @Test
    void changeBookingRejectsAttendedBookings() {
        ServiceTestContext context = new ServiceTestContext();
        context.addMember("M001");
        context.addLesson("L001", LocalDate.of(2026, 5, 2), LessonSlot.MORNING, ExerciseType.YOGA);
        context.addLesson("L002", LocalDate.of(2026, 5, 3), LessonSlot.AFTERNOON, ExerciseType.ZUMBA);

        Booking booking = context.bookingService().bookLesson("M001", "L001");
        context.attendanceService().attendBooking(booking.id(), 5, "Completed.");

        assertThrows(
                BookingStateException.class,
                () -> context.bookingService().changeBooking(booking.id(), "L002")
        );
    }
}
