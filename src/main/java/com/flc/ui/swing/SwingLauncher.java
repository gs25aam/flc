package com.flc.ui.swing;

import com.flc.service.FlcFacade;

import javax.swing.SwingUtilities;

public final class SwingLauncher {
    private final FlcFacade facade;

    public SwingLauncher(FlcFacade facade) {
        this.facade = facade;
    }

    public void launch() {
        SwingUtilities.invokeLater(() -> new MainFrame(facade).setVisible(true));
    }
}
