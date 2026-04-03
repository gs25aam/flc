package com.flc.service;

import com.flc.domain.Booking;
import com.flc.domain.ExerciseType;
import com.flc.domain.Lesson;
import com.flc.domain.Member;
import com.flc.service.dto.BookingSummary;
import com.flc.service.dto.MonthlyChampionReport;
import com.flc.service.dto.MonthlyLessonReport;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;

public final class FlcFacade {
    private final MemberService memberService;
    private final TimetableService timetableService;
    private final BookingService bookingService;
    private final AttendanceService attendanceService;
    private final ReportService reportService;

    public FlcFacade(
            MemberService memberService,
            TimetableService timetableService,
            BookingService bookingService,
            AttendanceService attendanceService,
            ReportService reportService
    ) {
        this.memberService = memberService;
        this.timetableService = timetableService;
        this.bookingService = bookingService;
        this.attendanceService = attendanceService;
        this.reportService = reportService;
    }

    public List<Lesson> listLessonsByDay(DayOfWeek dayOfWeek) {
        return timetableService.listLessonsByDay(dayOfWeek);
    }

    public List<Lesson> listLessonsByExercise(ExerciseType exerciseType) {
        return timetableService.listLessonsByExercise(exerciseType);
    }

    public List<Member> listMembers() {
        return memberService.listMembers();
    }

    public Member registerMember(String name) {
        return memberService.registerMember(name);
    }

    public List<BookingSummary> listBookingsForMember(String memberId) {
        return bookingService.listBookingsForMember(memberId).stream()
                .map(this::toSummary)
                .toList();
    }

    public BookingSummary bookLesson(String memberId, String lessonId) {
        return toSummary(bookingService.bookLesson(memberId, lessonId));
    }

    public BookingSummary changeBooking(String bookingId, String newLessonId) {
        return toSummary(bookingService.changeBooking(bookingId, newLessonId));
    }

    public BookingSummary cancelBooking(String bookingId) {
        return toSummary(bookingService.cancelBooking(bookingId));
    }

    public BookingSummary attendBooking(String bookingId, int rating, String review) {
        return toSummary(attendanceService.attendBooking(bookingId, rating, review));
    }

    public MonthlyLessonReport getMonthlyLessonReport(Month month) {
        return reportService.getMonthlyLessonReport(month);
    }

    public MonthlyChampionReport getMonthlyChampionReport(Month month) {
        return reportService.getMonthlyChampionReport(month);
    }

    private BookingSummary toSummary(Booking booking) {
        Member member = memberService.getMember(booking.memberId());
        Lesson lesson = timetableService.getLesson(booking.lessonId());

        Integer rating = booking.feedback() == null ? null : booking.feedback().rating();
        String review = booking.feedback() == null ? null : booking.feedback().review();

        return new BookingSummary(
                booking.id(),
                member.id(),
                member.name(),
                lesson.id(),
                lesson.exerciseType(),
                lesson.date(),
                lesson.slot(),
                booking.status(),
                rating,
                review
        );
    }
}
