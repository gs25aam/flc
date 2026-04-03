package com.flc.service;

import com.flc.domain.ExerciseType;
import com.flc.domain.Lesson;
import com.flc.exception.EntityNotFoundException;
import com.flc.repository.LessonRepository;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;

public final class TimetableService {
    private final LessonRepository lessonRepository;

    public TimetableService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public List<Lesson> listLessonsByDay(DayOfWeek dayOfWeek) {
        if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Only Saturday and Sunday are valid timetable filters.");
        }
        return lessonRepository.findByDay(dayOfWeek);
    }

    public List<Lesson> listLessonsByExercise(ExerciseType exerciseType) {
        return lessonRepository.findByExerciseType(exerciseType);
    }

    public List<Lesson> listLessonsByMonth(Month month) {
        return lessonRepository.findAll().stream()
                .filter(lesson -> lesson.month() == month)
                .toList();
    }

    public Lesson getLesson(String lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found: " + lessonId));
    }
}
