package com.flc.ui.swing;

import com.flc.service.FlcFacade;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.Dimension;

public final class MainFrame extends JFrame {
    public MainFrame(FlcFacade facade) {
        super("FLC Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Members", new MemberPanel(facade));
        tabs.addTab("Timetable", new TimetablePanel(facade));
        tabs.addTab("Bookings", new BookingPanel(facade));
        tabs.addTab("Reports", new ReportPanel(facade));
        add(tabs);
    }
}
