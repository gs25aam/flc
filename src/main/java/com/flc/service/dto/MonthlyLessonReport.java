package com.flc.service.dto;

import java.time.Month;
import java.util.List;

public record MonthlyLessonReport(Month month, List<LessonReportEntry> entries) {
}
