package com.flc.service;

import com.flc.domain.Booking;
import com.flc.domain.BookingStatus;
import com.flc.domain.Lesson;
import com.flc.exception.BookingStateException;
import com.flc.exception.CapacityExceededException;
import com.flc.exception.DuplicateBookingException;
import com.flc.exception.EntityNotFoundException;
import com.flc.exception.TimeSlotConflictException;
import com.flc.repository.BookingRepository;

import java.util.List;

public final class BookingService {
    private static final String BOOKING_ID_PATTERN = "B\\d+";

    private final BookingRepository bookingRepository;
    private final MemberService memberService;
    private final TimetableService timetableService;

    public BookingService(
            BookingRepository bookingRepository,
            MemberService memberService,
            TimetableService timetableService
    ) {
        this.bookingRepository = bookingRepository;
        this.memberService = memberService;
        this.timetableService = timetableService;
    }

    public Booking bookLesson(String memberId, String lessonId) {
        memberService.getMember(memberId);
        Lesson lesson = timetableService.getLesson(lessonId);

        validateDuplicate(memberId, lessonId, null);
        validateTimeSlotConflict(memberId, lesson, null);
        validateCapacity(lessonId);

        String bookingId = nextBookingId();
        Booking booking = new Booking(bookingId, memberId, lessonId, BookingStatus.BOOKED);
        return bookingRepository.save(booking);
    }

    public Booking changeBooking(String bookingId, String newLessonId) {
        Booking booking = getBooking(bookingId);
        ensureMutable(booking, "changed");

        if (booking.lessonId().equals(newLessonId)) {
            throw new DuplicateBookingException("Booking is already assigned to lesson " + newLessonId + ".");
        }

        Lesson newLesson = timetableService.getLesson(newLessonId);
        validateDuplicate(booking.memberId(), newLessonId, booking.id());
        validateTimeSlotConflict(booking.memberId(), newLesson, booking.id());
        validateCapacity(newLessonId);

        Booking changedBooking = booking.changeLesson(newLessonId);
        return bookingRepository.save(changedBooking);
    }

    public Booking cancelBooking(String bookingId) {
        Booking booking = getBooking(bookingId);
        ensureMutable(booking, "cancelled");
        Booking cancelledBooking = booking.cancel();
        return bookingRepository.save(cancelledBooking);
    }

    public Booking getBooking(String bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found: " + bookingId));
    }

    public List<Booking> listBookingsForMember(String memberId) {
        memberService.getMember(memberId);
        return bookingRepository.findByMemberId(memberId);
    }

    public List<Booking> listAllBookings() {
        return bookingRepository.findAll();
    }

    private void validateCapacity(String lessonId) {
        Lesson lesson = timetableService.getLesson(lessonId);
        long activeBookings = bookingRepository.findByLessonId(lessonId).stream()
                .filter(booking -> booking.status().countsTowardsCapacity())
                .count();

        if (activeBookings >= lesson.capacity()) {
            throw new CapacityExceededException("Lesson " + lessonId + " is already full.");
        }
    }

    private void validateDuplicate(String memberId, String lessonId, String excludedBookingId) {
        boolean duplicateExists = bookingRepository.findByMemberId(memberId).stream()
                .filter(booking -> excludedBookingId == null || !booking.id().equals(excludedBookingId))
                .filter(booking -> booking.status().countsTowardsCapacity())
                .anyMatch(booking -> booking.lessonId().equals(lessonId));

        if (duplicateExists) {
            throw new DuplicateBookingException(
                    "Member " + memberId + " already has a booking for lesson " + lessonId + "."
            );
        }
    }

    private void validateTimeSlotConflict(String memberId, Lesson targetLesson, String excludedBookingId) {
        boolean conflictExists = bookingRepository.findByMemberId(memberId).stream()
                .filter(booking -> excludedBookingId == null || !booking.id().equals(excludedBookingId))
                .filter(booking -> booking.status().countsTowardsCapacity())
                .map(booking -> timetableService.getLesson(booking.lessonId()))
                .anyMatch(existingLesson ->
                        existingLesson.date().equals(targetLesson.date())
                                && existingLesson.slot() == targetLesson.slot()
                );

        if (conflictExists) {
            throw new TimeSlotConflictException(
                    "Member %s already has a lesson in the %s slot on %s."
                            .formatted(memberId, targetLesson.slot().displayName(), targetLesson.date())
            );
        }
    }

    private void ensureMutable(Booking booking, String action) {
        if (!booking.status().canBeUpdated()) {
            throw new BookingStateException(
                    "Booking %s cannot be %s because it is %s."
                            .formatted(booking.id(), action, booking.status())
            );
        }
    }

    private String nextBookingId() {
        int nextNumber = bookingRepository.findAll().stream()
                .map(Booking::id)
                .mapToInt(this::bookingNumber)
                .max()
                .orElse(0) + 1;
        return "B%03d".formatted(nextNumber);
    }

    private int bookingNumber(String bookingId) {
        if (!bookingId.matches(BOOKING_ID_PATTERN)) {
            return 0;
        }
        return Integer.parseInt(bookingId.substring(1));
    }
}
