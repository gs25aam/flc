package com.flc.ui.swing;

import com.flc.service.FlcFacade;
import com.flc.service.dto.BookingSummary;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public final class BookingPanel extends JPanel {
    private final FlcFacade facade;
    private final JTextArea outputArea = new JTextArea();
    private final JTextField memberIdField = new JTextField(6);
    private final JTextField lessonIdField = new JTextField(6);
    private final JTextField bookingIdField = new JTextField(6);
    private final JTextField newLessonIdField = new JTextField(6);
    private final JTextField ratingField = new JTextField(3);
    private final JTextField reviewField = new JTextField(18);

    public BookingPanel(FlcFacade facade) {
        this.facade = facade;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton bookButton = new JButton("Book");
        JButton changeButton = new JButton("Change");
        JButton cancelButton = new JButton("Cancel");
        JButton attendButton = new JButton("Attend");
        JButton listButton = new JButton("Show Member Bookings");

        bookButton.addActionListener(event -> bookLesson());
        changeButton.addActionListener(event -> changeBooking());
        cancelButton.addActionListener(event -> cancelBooking());
        attendButton.addActionListener(event -> attendBooking());
        listButton.addActionListener(event -> listBookings());

        controls.add(new JLabel("Member ID"));
        controls.add(memberIdField);
        controls.add(new JLabel("Lesson ID"));
        controls.add(lessonIdField);
        controls.add(bookButton);
        controls.add(new JLabel("Booking ID"));
        controls.add(bookingIdField);
        controls.add(new JLabel("New Lesson"));
        controls.add(newLessonIdField);
        controls.add(changeButton);
        controls.add(cancelButton);
        controls.add(new JLabel("Rating"));
        controls.add(ratingField);
        controls.add(new JLabel("Review"));
        controls.add(reviewField);
        controls.add(attendButton);
        controls.add(listButton);

        outputArea.setEditable(false);
        add(controls, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
    }

    private void bookLesson() {
        try {
            BookingSummary summary = facade.bookLesson(memberIdField.getText().trim(), lessonIdField.getText().trim());
            renderSummary("Booking created", summary);
        } catch (RuntimeException exception) {
            outputArea.setText("Action failed: " + exception.getMessage());
        }
    }

    private void changeBooking() {
        try {
            BookingSummary summary = facade.changeBooking(bookingIdField.getText().trim(), newLessonIdField.getText().trim());
            renderSummary("Booking changed", summary);
        } catch (RuntimeException exception) {
            outputArea.setText("Action failed: " + exception.getMessage());
        }
    }

    private void cancelBooking() {
        try {
            BookingSummary summary = facade.cancelBooking(bookingIdField.getText().trim());
            renderSummary("Booking cancelled", summary);
        } catch (RuntimeException exception) {
            outputArea.setText("Action failed: " + exception.getMessage());
        }
    }

    private void attendBooking() {
        try {
            int rating = Integer.parseInt(ratingField.getText().trim());
            BookingSummary summary = facade.attendBooking(bookingIdField.getText().trim(), rating, reviewField.getText());
            renderSummary("Lesson attended", summary);
        } catch (RuntimeException exception) {
            outputArea.setText("Action failed: " + exception.getMessage());
        }
    }

    private void listBookings() {
        try {
            StringBuilder builder = new StringBuilder("Bookings for ")
                    .append(memberIdField.getText().trim())
                    .append('\n');
            for (BookingSummary booking : facade.listBookingsForMember(memberIdField.getText().trim())) {
                builder.append("- ")
                        .append(booking.bookingId())
                        .append(" | ")
                        .append(booking.date())
                        .append(" | ")
                        .append(booking.slot().displayName())
                        .append(" | ")
                        .append(booking.exerciseType().displayName())
                        .append(" | ")
                        .append(booking.status())
                        .append('\n');
            }
            outputArea.setText(builder.toString());
        } catch (RuntimeException exception) {
            outputArea.setText("Action failed: " + exception.getMessage());
        }
    }

    private void renderSummary(String title, BookingSummary summary) {
        StringBuilder builder = new StringBuilder(title).append('\n');
        builder.append("- ")
                .append(summary.bookingId())
                .append(" | member=")
                .append(summary.memberId())
                .append(" | lesson=")
                .append(summary.lessonId())
                .append(" | ")
                .append(summary.date())
                .append(" | ")
                .append(summary.slot().displayName())
                .append(" | ")
                .append(summary.exerciseType().displayName())
                .append(" | ")
                .append(summary.status())
                .append('\n');
        if (summary.rating() != null) {
            builder.append("- rating=").append(summary.rating()).append(" | review=").append(summary.review()).append('\n');
        }
        outputArea.setText(builder.toString());
    }
}
