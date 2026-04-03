package com.flc.service.dto;

import com.flc.domain.ExerciseType;
import com.flc.domain.LessonSlot;

import java.time.LocalDate;

public record LessonReportEntry(
        String lessonId,
        LocalDate date,
        LessonSlot slot,
        ExerciseType exerciseType,
        long attendedCount,
        Double averageRating
) {
}
