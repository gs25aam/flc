package com.flc.ui.swing;

import com.flc.domain.ExerciseType;
import com.flc.service.FlcFacade;
import com.flc.service.dto.LessonReportEntry;
import com.flc.service.dto.MonthlyChampionReport;
import com.flc.service.dto.MonthlyLessonReport;
import com.flc.ui.UiErrorHandler;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.Month;

public final class ReportPanel extends JPanel {
    private final FlcFacade facade;
    private final JTextArea outputArea = new JTextArea();
    private final JComboBox<Month> monthBox = new JComboBox<>(Month.values());

    public ReportPanel(FlcFacade facade) {
        this.facade = facade;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton lessonReportButton = new JButton("Lesson Report");
        JButton championReportButton = new JButton("Champion Report");

        lessonReportButton.addActionListener(event -> showLessonReport());
        championReportButton.addActionListener(event -> showChampionReport());

        controls.add(new JLabel("Month"));
        controls.add(monthBox);
        controls.add(lessonReportButton);
        controls.add(championReportButton);

        outputArea.setEditable(false);
        add(controls, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
    }

    private void showLessonReport() {
        try {
            MonthlyLessonReport report = facade.getMonthlyLessonReport((Month) monthBox.getSelectedItem());
            StringBuilder builder = new StringBuilder(report.month().toString()).append(" lesson report\n");
            for (LessonReportEntry entry : report.entries()) {
                builder.append("- ")
                        .append(entry.lessonId())
                        .append(" | ")
                        .append(entry.date())
                        .append(" | ")
                        .append(entry.slot().displayName())
                        .append(" | ")
                        .append(entry.exerciseType().displayName())
                        .append(" | attended=")
                        .append(entry.attendedCount())
                        .append(" | avgRating=")
                        .append(entry.averageRating() == null ? "N/A" : String.format("%.2f", entry.averageRating()))
                        .append('\n');
            }
            outputArea.setText(builder.toString());
        } catch (RuntimeException exception) {
            outputArea.setText(UiErrorHandler.messageFor(exception));
        }
    }

    private void showChampionReport() {
        try {
            MonthlyChampionReport report = facade.getMonthlyChampionReport((Month) monthBox.getSelectedItem());
            StringBuilder builder = new StringBuilder(report.month().toString()).append(" champion report\n");
            report.incomeByExercise().forEach((type, income) ->
                    builder.append("- ").append(type.displayName()).append(" | income=GBP ").append(income).append('\n')
            );
            if (report.champions().isEmpty()) {
                builder.append("No champion for this month.\n");
            } else {
                builder.append("Champion(s): ");
                for (int index = 0; index < report.champions().size(); index++) {
                    ExerciseType type = report.champions().get(index);
                    if (index > 0) {
                        builder.append(", ");
                    }
                    builder.append(type.displayName());
                }
                builder.append(" at GBP ").append(report.highestIncome()).append('\n');
            }
            outputArea.setText(builder.toString());
        } catch (RuntimeException exception) {
            outputArea.setText(UiErrorHandler.messageFor(exception));
        }
    }
}
