package com.flc.service;

import com.flc.domain.Booking;
import com.flc.domain.BookingStatus;
import com.flc.domain.ExerciseType;
import com.flc.domain.Lesson;
import com.flc.repository.BookingRepository;
import com.flc.repository.LessonRepository;
import com.flc.service.dto.LessonReportEntry;
import com.flc.service.dto.MonthlyChampionReport;
import com.flc.service.dto.MonthlyLessonReport;

import java.math.BigDecimal;
import java.time.Month;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ReportService {
    private final LessonRepository lessonRepository;
    private final BookingRepository bookingRepository;

    public ReportService(LessonRepository lessonRepository, BookingRepository bookingRepository) {
        this.lessonRepository = lessonRepository;
        this.bookingRepository = bookingRepository;
    }

    public MonthlyLessonReport getMonthlyLessonReport(Month month) {
        List<LessonReportEntry> entries = lessonRepository.findAll().stream()
                .filter(lesson -> lesson.month() == month)
                .map(this::toLessonReportEntry)
                .toList();
        return new MonthlyLessonReport(month, entries);
    }

    public MonthlyChampionReport getMonthlyChampionReport(Month month) {
        Map<ExerciseType, BigDecimal> incomeByExercise = new EnumMap<>(ExerciseType.class);
        for (ExerciseType type : ExerciseType.values()) {
            incomeByExercise.put(type, BigDecimal.ZERO);
        }

        List<Booking> monthlyBookings = bookingRepository.findAll().stream()
                .filter(booking -> booking.status() != BookingStatus.CANCELLED)
                .filter(booking -> lessonRepository.findById(booking.lessonId())
                        .map(lesson -> lesson.month() == month)
                        .orElse(false))
                .toList();

        monthlyBookings.forEach(booking -> {
            Lesson lesson = lessonRepository.findById(booking.lessonId()).orElseThrow();
            ExerciseType type = lesson.exerciseType();
            incomeByExercise.put(type, incomeByExercise.get(type).add(type.price()));
        });

        BigDecimal highestIncome = incomeByExercise.values().stream()
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        List<ExerciseType> champions = monthlyBookings.isEmpty()
                ? List.of()
                : incomeByExercise.entrySet().stream()
                .filter(entry -> entry.getValue().compareTo(highestIncome) == 0)
                .map(Map.Entry::getKey)
                .toList();

        Map<ExerciseType, BigDecimal> orderedIncome = new LinkedHashMap<>();
        for (ExerciseType exerciseType : ExerciseType.values()) {
            orderedIncome.put(exerciseType, incomeByExercise.get(exerciseType));
        }

        return new MonthlyChampionReport(
                month,
                Collections.unmodifiableMap(orderedIncome),
                highestIncome,
                champions
        );
    }

    private LessonReportEntry toLessonReportEntry(Lesson lesson) {
        List<Booking> attendedBookings = bookingRepository.findByLessonId(lesson.id()).stream()
                .filter(booking -> booking.status() == BookingStatus.ATTENDED)
                .toList();

        Double averageRating = attendedBookings.isEmpty()
                ? null
                : attendedBookings.stream()
                .collect(Collectors.averagingInt(booking -> booking.feedback().rating()));

        return new LessonReportEntry(
                lesson.id(),
                lesson.date(),
                lesson.slot(),
                lesson.exerciseType(),
                attendedBookings.size(),
                averageRating
        );
    }
}
