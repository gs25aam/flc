package com.flc.service;

import com.flc.domain.Booking;
import com.flc.domain.LessonFeedback;
import com.flc.exception.BookingStateException;
import com.flc.exception.InvalidRatingException;
import com.flc.repository.BookingRepository;

public final class AttendanceService {
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    public AttendanceService(BookingRepository bookingRepository, BookingService bookingService) {
        this.bookingRepository = bookingRepository;
        this.bookingService = bookingService;
    }

    public Booking attendBooking(String bookingId, int rating, String review) {
        if (rating < 1 || rating > 5) {
            throw new InvalidRatingException("Rating must be between 1 and 5.");
        }

        Booking booking = bookingService.getBooking(bookingId);
        if (!booking.status().canBeUpdated()) {
            throw new BookingStateException(
                    "Booking %s cannot be attended because it is %s.".formatted(booking.id(), booking.status())
            );
        }

        LessonFeedback feedback = new LessonFeedback(rating, review == null ? "" : review.trim());
        Booking attendedBooking = booking.attend(feedback);
        return bookingRepository.save(attendedBooking);
    }
}
