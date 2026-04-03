package com.flc.seed;

import com.flc.domain.ExerciseType;
import com.flc.domain.Lesson;
import com.flc.domain.LessonSlot;
import com.flc.domain.Member;
import com.flc.repository.LessonRepository;
import com.flc.repository.MemberRepository;
import com.flc.service.AttendanceService;
import com.flc.service.BookingService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class SeedDataFactory {
    public void seed(
            MemberRepository memberRepository,
            LessonRepository lessonRepository,
            BookingService bookingService,
            AttendanceService attendanceService
    ) {
        seedMembers(memberRepository);
        seedLessons(lessonRepository);
        seedBookings(bookingService, attendanceService);
    }

    private void seedMembers(MemberRepository memberRepository) {
        List<String> names = List.of(
                "Alice Brown",
                "Ben Carter",
                "Chloe Davies",
                "Daniel Evans",
                "Ella Foster",
                "Finley Green",
                "Grace Hall",
                "Harry Irwin",
                "Isla Jones",
                "Jacob King",
                "Lily Morgan",
                "Noah Patel"
        );

        for (int index = 0; index < names.size(); index++) {
            memberRepository.save(new Member("M%03d".formatted(index + 1), names.get(index)));
        }
    }

    private void seedLessons(LessonRepository lessonRepository) {
        List<LocalDate> weekendDays = List.of(
                LocalDate.of(2026, 5, 2),
                LocalDate.of(2026, 5, 3),
                LocalDate.of(2026, 5, 9),
                LocalDate.of(2026, 5, 10),
                LocalDate.of(2026, 5, 16),
                LocalDate.of(2026, 5, 17),
                LocalDate.of(2026, 5, 23),
                LocalDate.of(2026, 5, 24),
                LocalDate.of(2026, 6, 6),
                LocalDate.of(2026, 6, 7),
                LocalDate.of(2026, 6, 13),
                LocalDate.of(2026, 6, 14),
                LocalDate.of(2026, 6, 20),
                LocalDate.of(2026, 6, 21),
                LocalDate.of(2026, 6, 27),
                LocalDate.of(2026, 6, 28)
        );

        ExerciseType[][] exercisePatterns = {
                {ExerciseType.YOGA, ExerciseType.ZUMBA, ExerciseType.BOX_FIT},
                {ExerciseType.AQUACISE, ExerciseType.BODY_BLITZ, ExerciseType.YOGA},
                {ExerciseType.ZUMBA, ExerciseType.BOX_FIT, ExerciseType.AQUACISE},
                {ExerciseType.BODY_BLITZ, ExerciseType.YOGA, ExerciseType.ZUMBA},
                {ExerciseType.BOX_FIT, ExerciseType.AQUACISE, ExerciseType.BODY_BLITZ},
                {ExerciseType.YOGA, ExerciseType.ZUMBA, ExerciseType.AQUACISE},
                {ExerciseType.BODY_BLITZ, ExerciseType.BOX_FIT, ExerciseType.YOGA},
                {ExerciseType.AQUACISE, ExerciseType.YOGA, ExerciseType.ZUMBA}
        };

        List<Lesson> lessons = new ArrayList<>();
        int lessonCounter = 1;
        for (int dayIndex = 0; dayIndex < weekendDays.size(); dayIndex++) {
            ExerciseType[] pattern = exercisePatterns[dayIndex % exercisePatterns.length];
            lessons.add(new Lesson("L%03d".formatted(lessonCounter++), weekendDays.get(dayIndex), LessonSlot.MORNING, pattern[0]));
            lessons.add(new Lesson("L%03d".formatted(lessonCounter++), weekendDays.get(dayIndex), LessonSlot.AFTERNOON, pattern[1]));
            lessons.add(new Lesson("L%03d".formatted(lessonCounter++), weekendDays.get(dayIndex), LessonSlot.EVENING, pattern[2]));
        }

        lessonRepository.saveAll(lessons);
    }

    private void seedBookings(BookingService bookingService, AttendanceService attendanceService) {
        List<AttendedSeed> attendedSeeds = List.of(
                new AttendedSeed("M001", "L001", 5, "Excellent instructor and pace."),
                new AttendedSeed("M002", "L001", 4, "Relaxing and well structured."),
                new AttendedSeed("M003", "L002", 4, "Fun music and good energy."),
                new AttendedSeed("M004", "L002", 5, "Great session for beginners."),
                new AttendedSeed("M005", "L003", 5, "Intense workout and clear coaching."),
                new AttendedSeed("M006", "L003", 4, "Challenging but enjoyable."),
                new AttendedSeed("M007", "L004", 4, "Water session felt fresh and balanced."),
                new AttendedSeed("M008", "L004", 5, "Very supportive instructor."),
                new AttendedSeed("M009", "L005", 3, "Tough class but good variety."),
                new AttendedSeed("M010", "L005", 4, "Solid full-body workout."),
                new AttendedSeed("M011", "L006", 5, "Loved the evening yoga flow."),
                new AttendedSeed("M012", "L006", 4, "Calm atmosphere and clear cues."),
                new AttendedSeed("M001", "L007", 4, "Zumba had strong energy."),
                new AttendedSeed("M002", "L008", 5, "Box Fit pushed me in a good way."),
                new AttendedSeed("M003", "L009", 4, "Aquacise felt well paced."),
                new AttendedSeed("M004", "L010", 5, "Body Blitz was intense and fun."),
                new AttendedSeed("M005", "L011", 4, "Helpful yoga corrections."),
                new AttendedSeed("M006", "L012", 5, "Zumba playlist was excellent."),
                new AttendedSeed("M007", "L013", 4, "Box Fit combo work was clear."),
                new AttendedSeed("M008", "L014", 4, "Aquacise class was easy to follow."),
                new AttendedSeed("M009", "L015", 5, "Body Blitz kept the whole room moving."),
                new AttendedSeed("M010", "L016", 4, "Yoga session helped with recovery."),
                new AttendedSeed("M011", "L017", 5, "Morning Box Fit was energising."),
                new AttendedSeed("M012", "L018", 4, "Aquacise instructor explained every drill.")
        );

        for (AttendedSeed seed : attendedSeeds) {
            String bookingId = bookingService.bookLesson(seed.memberId(), seed.lessonId()).id();
            attendanceService.attendBooking(bookingId, seed.rating(), seed.review());
        }

        bookingService.bookLesson("M001", "L025");
        bookingService.bookLesson("M002", "L026");
        bookingService.bookLesson("M003", "L027");
        bookingService.bookLesson("M004", "L028");

        String changedBookingId = bookingService.bookLesson("M005", "L029").id();
        bookingService.changeBooking(changedBookingId, "L032");

        String cancelledBookingId = bookingService.bookLesson("M006", "L030").id();
        bookingService.cancelBooking(cancelledBookingId);
    }

    private record AttendedSeed(String memberId, String lessonId, int rating, String review) {
    }
}
