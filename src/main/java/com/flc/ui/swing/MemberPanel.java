package com.flc.ui.swing;

import com.flc.domain.Member;
import com.flc.service.FlcFacade;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public final class MemberPanel extends JPanel {
    private final FlcFacade facade;
    private final JTextArea outputArea = new JTextArea();
    private final JTextField memberNameField = new JTextField(20);

    public MemberPanel(FlcFacade facade) {
        this.facade = facade;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton registerButton = new JButton("Register Member");
        JButton refreshButton = new JButton("Refresh Members");

        registerButton.addActionListener(event -> registerMember());
        refreshButton.addActionListener(event -> refreshMembers());

        controls.add(new JLabel("Name"));
        controls.add(memberNameField);
        controls.add(registerButton);
        controls.add(refreshButton);

        outputArea.setEditable(false);
        add(controls, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        refreshMembers();
    }

    private void registerMember() {
        try {
            facade.registerMember(memberNameField.getText());
            memberNameField.setText("");
            refreshMembers();
        } catch (RuntimeException exception) {
            outputArea.setText("Action failed: " + exception.getMessage());
        }
    }

    private void refreshMembers() {
        StringBuilder builder = new StringBuilder("Members\n");
        for (Member member : facade.listMembers()) {
            builder.append("- ")
                    .append(member.id())
                    .append(" | ")
                    .append(member.name())
                    .append('\n');
        }
        outputArea.setText(builder.toString());
    }
}
