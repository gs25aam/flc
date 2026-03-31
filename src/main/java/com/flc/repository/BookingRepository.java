package com.flc.repository;

import com.flc.domain.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Booking save(Booking booking);

    Optional<Booking> findById(String bookingId);

    List<Booking> findAll();

    List<Booking> findByMemberId(String memberId);

    List<Booking> findByLessonId(String lessonId);
}
