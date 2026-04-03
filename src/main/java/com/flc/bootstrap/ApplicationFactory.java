package com.flc.bootstrap;

import com.flc.repository.BookingRepository;
import com.flc.repository.LessonRepository;
import com.flc.repository.MemberRepository;
import com.flc.repository.inmemory.InMemoryBookingRepository;
import com.flc.repository.inmemory.InMemoryLessonRepository;
import com.flc.repository.inmemory.InMemoryMemberRepository;
import com.flc.seed.SeedDataFactory;
import com.flc.service.AttendanceService;
import com.flc.service.BookingService;
import com.flc.service.FlcFacade;
import com.flc.service.MemberService;
import com.flc.service.ReportService;
import com.flc.service.TimetableService;
import com.flc.ui.cli.CliApplication;
import com.flc.ui.swing.SwingLauncher;

public final class ApplicationFactory {
    public BootstrapContext create() {
        MemberRepository memberRepository = new InMemoryMemberRepository();
        LessonRepository lessonRepository = new InMemoryLessonRepository();
        BookingRepository bookingRepository = new InMemoryBookingRepository();

        MemberService memberService = new MemberService(memberRepository);
        TimetableService timetableService = new TimetableService(lessonRepository);
        BookingService bookingService = new BookingService(bookingRepository, memberService, timetableService);
        AttendanceService attendanceService = new AttendanceService(bookingRepository, bookingService);
        ReportService reportService = new ReportService(lessonRepository, bookingRepository);
        FlcFacade facade = new FlcFacade(
                memberService,
                timetableService,
                bookingService,
                attendanceService,
                reportService
        );

        new SeedDataFactory().seed(memberRepository, lessonRepository, bookingService, attendanceService);

        return new BootstrapContext(facade, new CliApplication(facade), new SwingLauncher(facade));
    }
}
