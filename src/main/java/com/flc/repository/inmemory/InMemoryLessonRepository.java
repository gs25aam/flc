package com.flc.repository.inmemory;

import com.flc.domain.ExerciseType;
import com.flc.domain.Lesson;
import com.flc.repository.LessonRepository;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InMemoryLessonRepository implements LessonRepository {
    private static final Comparator<Lesson> LESSON_ORDER =
            Comparator.comparing(Lesson::date).thenComparing(lesson -> lesson.slot().ordinal());

    private final Map<String, Lesson> lessons = new LinkedHashMap<>();

    @Override
    public Lesson save(Lesson lesson) {
        lessons.put(lesson.id(), lesson);
        return lesson;
    }

    @Override
    public List<Lesson> saveAll(List<Lesson> newLessons) {
        newLessons.forEach(this::save);
        return findAll();
    }

    @Override
    public Optional<Lesson> findById(String lessonId) {
        return Optional.ofNullable(lessons.get(lessonId));
    }

    @Override
    public List<Lesson> findAll() {
        List<Lesson> result = new ArrayList<>(lessons.values());
        result.sort(LESSON_ORDER);
        return result;
    }

    @Override
    public List<Lesson> findByDay(DayOfWeek dayOfWeek) {
        return findAll().stream()
                .filter(lesson -> lesson.dayOfWeek() == dayOfWeek)
                .toList();
    }

    @Override
    public List<Lesson> findByExerciseType(ExerciseType exerciseType) {
        return findAll().stream()
                .filter(lesson -> lesson.exerciseType() == exerciseType)
                .toList();
    }
}
