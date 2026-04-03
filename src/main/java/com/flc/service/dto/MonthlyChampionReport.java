package com.flc.service.dto;

import com.flc.domain.ExerciseType;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.Map;

public record MonthlyChampionReport(
        Month month,
        Map<ExerciseType, BigDecimal> incomeByExercise,
        BigDecimal highestIncome,
        List<ExerciseType> champions
) {
}
