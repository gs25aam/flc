package com.flc.ui.cli;

import com.flc.domain.ExerciseType;
import com.flc.domain.Lesson;
import com.flc.domain.Member;
import com.flc.service.FlcFacade;
import com.flc.service.dto.BookingSummary;
import com.flc.service.dto.LessonReportEntry;
import com.flc.service.dto.MonthlyChampionReport;
import com.flc.service.dto.MonthlyLessonReport;
import com.flc.ui.UiErrorHandler;

import java.io.PrintStream;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;

public final class MenuRouter {
    private final FlcFacade facade;
    private final ConsolePrompts prompts;
    private final PrintStream out;

    public MenuRouter(FlcFacade facade, ConsolePrompts prompts, PrintStream out) {
        this.facade = facade;
        this.prompts = prompts;
        this.out = out;
    }

    public boolean handle(int choice) {
        try {
            return switch (choice) {
                case 1 -> viewTimetableByDay();
                case 2 -> viewTimetableByExercise();
                case 3 -> registerMember();
                case 4 -> listMembers();
                case 5 -> bookLesson();
                case 6 -> changeBooking();
                case 7 -> cancelBooking();
                case 8 -> attendLesson();
                case 9 -> viewMemberBookings();
                case 10 -> viewMonthlyLessonReport();
                case 11 -> viewMonthlyChampionReport();
                case 0 -> false;
                default -> {
                    out.println("Unknown option.");
                    yield true;
                }
            };
        } catch (RuntimeException exception) {
            out.println(UiErrorHandler.messageFor(exception));
            return true;
        }
    }

    private boolean viewTimetableByDay() {
        DayOfWeek dayOfWeek = prompts.readWeekendDay();
        printLessons(facade.listLessonsByDay(dayOfWeek), dayOfWeek + " timetable");
        return true;
    }

    private boolean viewTimetableByExercise() {
        ExerciseType exerciseType = prompts.readExerciseType();
        printLessons(facade.listLessonsByExercise(exerciseType), exerciseType.displayName() + " timetable");
        return true;
    }

    private boolean registerMember() {
        String name = prompts.readRequired("Member name");
        Member member = facade.registerMember(name);
        out.println("Registered member: " + member.id() + " - " + member.name());
        return true;
    }

    private boolean listMembers() {
        out.println("Members");
        for (Member member : facade.listMembers()) {
            out.printf("- %s | %s%n", member.id(), member.name());
        }
        return true;
    }

    private boolean bookLesson() {
        String memberId = prompts.readRequired("Member ID");
        String lessonId = prompts.readRequired("Lesson ID");
        BookingSummary summary = facade.bookLesson(memberId, lessonId);
        printBookingSummary("Booking created", summary);
        return true;
    }

    private boolean changeBooking() {
        String bookingId = prompts.readRequired("Booking ID");
        String newLessonId = prompts.readRequired("New lesson ID");
        BookingSummary summary = facade.changeBooking(bookingId, newLessonId);
        printBookingSummary("Booking changed", summary);
        return true;
    }

    private boolean cancelBooking() {
        String bookingId = prompts.readRequired("Booking ID");
        BookingSummary summary = facade.cancelBooking(bookingId);
        printBookingSummary("Booking cancelled", summary);
        return true;
    }

    private boolean attendLesson() {
        String bookingId = prompts.readRequired("Booking ID");
        int rating = prompts.readRating();
        String review = prompts.readOptional("Review");
        BookingSummary summary = facade.attendBooking(bookingId, rating, review);
        printBookingSummary("Lesson attended", summary);
        return true;
    }

    private boolean viewMemberBookings() {
        String memberId = prompts.readRequired("Member ID");
        List<BookingSummary> bookings = facade.listBookingsForMember(memberId);
        out.println("Bookings for " + memberId);
        if (bookings.isEmpty()) {
            out.println("No bookings found.");
            return true;
        }
        for (BookingSummary booking : bookings) {
            out.printf(
                    "- %s | %s | %s | %s | %s%n",
                    booking.bookingId(),
                    booking.date(),
                    booking.slot().displayName(),
                    booking.exerciseType().displayName(),
                    booking.status()
            );
        }
        return true;
    }

    private boolean viewMonthlyLessonReport() {
        Month month = prompts.readMonth();
        MonthlyLessonReport report = facade.getMonthlyLessonReport(month);
        out.println(month + " lesson report");
        for (LessonReportEntry entry : report.entries()) {
            String average = entry.averageRating() == null ? "N/A" : String.format("%.2f", entry.averageRating());
            out.printf(
                    "- %s | %s | %s | %s | attended=%d | avgRating=%s%n",
                    entry.lessonId(),
                    entry.date(),
                    entry.slot().displayName(),
                    entry.exerciseType().displayName(),
                    entry.attendedCount(),
                    average
            );
        }
        return true;
    }

    private boolean viewMonthlyChampionReport() {
        Month month = prompts.readMonth();
        MonthlyChampionReport report = facade.getMonthlyChampionReport(month);
        out.println(month + " champion exercise report");
        report.incomeByExercise().forEach((type, income) ->
                out.printf("- %s | income=£%s%n", type.displayName(), income)
        );
        if (report.champions().isEmpty()) {
            out.println("No champion for this month because there were no active bookings.");
        } else {
            out.println("Champion(s): " + joinChampions(report.champions()) + " at £" + report.highestIncome());
        }
        return true;
    }

    private void printLessons(List<Lesson> lessons, String title) {
        out.println(title);
        for (Lesson lesson : lessons) {
            out.println("- " + lesson.displayLabel());
        }
    }

    private void printBookingSummary(String title, BookingSummary summary) {
        out.println(title);
        out.printf(
                "- %s | member=%s | lesson=%s | %s | %s | %s | %s%n",
                summary.bookingId(),
                summary.memberId(),
                summary.lessonId(),
                summary.date(),
                summary.slot().displayName(),
                summary.exerciseType().displayName(),
                summary.status()
        );
        if (summary.rating() != null) {
            out.printf("- rating=%d | review=%s%n", summary.rating(), summary.review());
        }
    }

    private String joinChampions(List<ExerciseType> champions) {
        StringBuilder builder = new StringBuilder();
        for (ExerciseType champion : champions) {
            if (!builder.isEmpty()) {
                builder.append(", ");
            }
            builder.append(champion.displayName());
        }
        return builder.toString();
    }
}
