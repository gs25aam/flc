package com.flc.support;

import com.flc.domain.ExerciseType;
import com.flc.domain.Lesson;
import com.flc.domain.LessonSlot;
import com.flc.domain.Member;
import com.flc.repository.BookingRepository;
import com.flc.repository.LessonRepository;
import com.flc.repository.MemberRepository;
import com.flc.repository.inmemory.InMemoryBookingRepository;
import com.flc.repository.inmemory.InMemoryLessonRepository;
import com.flc.repository.inmemory.InMemoryMemberRepository;
import com.flc.service.AttendanceService;
import com.flc.service.BookingService;
import com.flc.service.MemberService;
import com.flc.service.ReportService;
import com.flc.service.TimetableService;

import java.time.LocalDate;

public final class ServiceTestContext {
    private final MemberRepository memberRepository = new InMemoryMemberRepository();
    private final LessonRepository lessonRepository = new InMemoryLessonRepository();
    private final BookingRepository bookingRepository = new InMemoryBookingRepository();

    private final MemberService memberService = new MemberService(memberRepository);
    private final TimetableService timetableService = new TimetableService(lessonRepository);
    private final BookingService bookingService = new BookingService(bookingRepository, memberService, timetableService);
    private final AttendanceService attendanceService = new AttendanceService(bookingRepository, bookingService);
    private final ReportService reportService = new ReportService(lessonRepository, bookingRepository);

    public Member addMember(String id) {
        Member member = new Member(id, "Member " + id);
        return memberRepository.save(member);
    }

    public Lesson addLesson(String id, LocalDate date, LessonSlot slot, ExerciseType type) {
        Lesson lesson = new Lesson(id, date, slot, type);
        return lessonRepository.save(lesson);
    }

    public BookingService bookingService() {
        return bookingService;
    }

    public AttendanceService attendanceService() {
        return attendanceService;
    }

    public ReportService reportService() {
        return reportService;
    }
}
