package com.flc.ui.swing;

import com.flc.domain.ExerciseType;
import com.flc.domain.Lesson;
import com.flc.service.FlcFacade;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.DayOfWeek;
import java.util.List;

public final class TimetablePanel extends JPanel {
    private final FlcFacade facade;
    private final JTextArea outputArea = new JTextArea();
    private final JComboBox<String> dayBox = new JComboBox<>(new String[]{"SATURDAY", "SUNDAY"});
    private final JComboBox<ExerciseType> exerciseBox = new JComboBox<>(ExerciseType.values());

    public TimetablePanel(FlcFacade facade) {
        this.facade = facade;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton byDayButton = new JButton("View By Day");
        JButton byExerciseButton = new JButton("View By Exercise");

        byDayButton.addActionListener(event -> showLessonsByDay());
        byExerciseButton.addActionListener(event -> showLessonsByExercise());

        controls.add(new JLabel("Day"));
        controls.add(dayBox);
        controls.add(byDayButton);
        controls.add(new JLabel("Exercise"));
        controls.add(exerciseBox);
        controls.add(byExerciseButton);

        outputArea.setEditable(false);
        add(controls, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        showLessonsByDay();
    }

    private void showLessonsByDay() {
        DayOfWeek dayOfWeek = DayOfWeek.valueOf((String) dayBox.getSelectedItem());
        renderLessons(facade.listLessonsByDay(dayOfWeek), dayOfWeek + " timetable");
    }

    private void showLessonsByExercise() {
        ExerciseType exerciseType = (ExerciseType) exerciseBox.getSelectedItem();
        renderLessons(facade.listLessonsByExercise(exerciseType), exerciseType.displayName() + " timetable");
    }

    private void renderLessons(List<Lesson> lessons, String title) {
        StringBuilder builder = new StringBuilder(title).append('\n');
        for (Lesson lesson : lessons) {
            builder.append("- ").append(lesson.displayLabel()).append('\n');
        }
        outputArea.setText(builder.toString());
    }
}
