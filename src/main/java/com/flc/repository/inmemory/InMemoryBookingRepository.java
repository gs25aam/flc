package com.flc.repository.inmemory;

import com.flc.domain.Booking;
import com.flc.repository.BookingRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InMemoryBookingRepository implements BookingRepository {
    private final Map<String, Booking> bookings = new LinkedHashMap<>();

    @Override
    public Booking save(Booking booking) {
        bookings.put(booking.id(), booking);
        return booking;
    }

    @Override
    public Optional<Booking> findById(String bookingId) {
        return Optional.ofNullable(bookings.get(bookingId));
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> result = new ArrayList<>(bookings.values());
        result.sort(Comparator.comparing(Booking::id));
        return result;
    }

    @Override
    public List<Booking> findByMemberId(String memberId) {
        return findAll().stream()
                .filter(booking -> booking.memberId().equals(memberId))
                .toList();
    }

    @Override
    public List<Booking> findByLessonId(String lessonId) {
        return findAll().stream()
                .filter(booking -> booking.lessonId().equals(lessonId))
                .toList();
    }
}
