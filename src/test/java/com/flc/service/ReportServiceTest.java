package com.flc.service;

import com.flc.domain.ExerciseType;
import com.flc.domain.LessonSlot;
import com.flc.service.dto.LessonReportEntry;
import com.flc.service.dto.MonthlyChampionReport;
import com.flc.service.dto.MonthlyLessonReport;
import com.flc.support.ServiceTestContext;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportServiceTest {
    @Test
    void monthlyLessonReportCountsOnlyAttendedBookings() {
        ServiceTestContext context = new ServiceTestContext();
        context.addMember("M001");
        context.addMember("M002");
        context.addMember("M003");
        context.addLesson("L001", LocalDate.of(2026, 5, 2), LessonSlot.MORNING, ExerciseType.YOGA);

        String bookingOne = context.bookingService().bookLesson("M001", "L001").id();
        String bookingTwo = context.bookingService().bookLesson("M002", "L001").id();
        context.bookingService().bookLesson("M003", "L001");

        context.attendanceService().attendBooking(bookingOne, 4, "Good.");
        context.attendanceService().attendBooking(bookingTwo, 5, "Excellent.");

        MonthlyLessonReport report = context.reportService().getMonthlyLessonReport(Month.MAY);
        LessonReportEntry entry = report.entries().stream()
                .filter(candidate -> candidate.lessonId().equals("L001"))
                .findFirst()
                .orElseThrow();

        assertEquals(2, entry.attendedCount());
        assertEquals(4.5, entry.averageRating(), 0.001);
    }

    @Test
    void monthlyChampionReportReturnsAllTiedChampions() {
        ServiceTestContext context = new ServiceTestContext();
        for (int index = 1; index <= 5; index++) {
            context.addMember("M%03d".formatted(index));
        }

        context.addLesson("L001", LocalDate.of(2026, 5, 2), LessonSlot.MORNING, ExerciseType.ZUMBA);
        context.addLesson("L002", LocalDate.of(2026, 5, 2), LessonSlot.AFTERNOON, ExerciseType.ZUMBA);
        context.addLesson("L003", LocalDate.of(2026, 5, 3), LessonSlot.MORNING, ExerciseType.ZUMBA);
        context.addLesson("L004", LocalDate.of(2026, 5, 3), LessonSlot.AFTERNOON, ExerciseType.BOX_FIT);
        context.addLesson("L005", LocalDate.of(2026, 5, 9), LessonSlot.MORNING, ExerciseType.BOX_FIT);

        context.bookingService().bookLesson("M001", "L001");
        context.bookingService().bookLesson("M002", "L002");
        context.bookingService().bookLesson("M003", "L003");
        context.bookingService().bookLesson("M004", "L004");
        context.bookingService().bookLesson("M005", "L005");

        MonthlyChampionReport report = context.reportService().getMonthlyChampionReport(Month.MAY);

        assertEquals(new BigDecimal("30.00"), report.highestIncome());
        assertEquals(2, report.champions().size());
        assertTrue(report.champions().contains(ExerciseType.ZUMBA));
        assertTrue(report.champions().contains(ExerciseType.BOX_FIT));
    }

    @Test
    void monthlyChampionReportListsAllExerciseTypes() {
        ServiceTestContext context = new ServiceTestContext();
        context.addMember("M001");
        context.addLesson("L001", LocalDate.of(2026, 5, 2), LessonSlot.MORNING, ExerciseType.YOGA);
        context.bookingService().bookLesson("M001", "L001");

        MonthlyChampionReport report = context.reportService().getMonthlyChampionReport(Month.MAY);

        assertEquals(ExerciseType.values().length, report.incomeByExercise().size());
        assertTrue(report.incomeByExercise().containsKey(ExerciseType.BODY_BLITZ));
        assertEquals(BigDecimal.ZERO, report.incomeByExercise().get(ExerciseType.BODY_BLITZ));
    }
}
