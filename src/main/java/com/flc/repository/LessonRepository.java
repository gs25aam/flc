package com.flc.repository;

import com.flc.domain.ExerciseType;
import com.flc.domain.Lesson;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface LessonRepository {
    Lesson save(Lesson lesson);

    List<Lesson> saveAll(List<Lesson> lessons);

    Optional<Lesson> findById(String lessonId);

    List<Lesson> findAll();

    List<Lesson> findByDay(DayOfWeek dayOfWeek);

    List<Lesson> findByExerciseType(ExerciseType exerciseType);
}
