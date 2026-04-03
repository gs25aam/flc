package com.flc.service.dto;

import com.flc.domain.BookingStatus;
import com.flc.domain.ExerciseType;
import com.flc.domain.LessonSlot;

import java.time.LocalDate;

public record BookingSummary(
        String bookingId,
        String memberId,
        String memberName,
        String lessonId,
        ExerciseType exerciseType,
        LocalDate date,
        LessonSlot slot,
        BookingStatus status,
        Integer rating,
        String review
) {
}
