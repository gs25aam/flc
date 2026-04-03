package com.flc.bootstrap;

import com.flc.service.FlcFacade;
import com.flc.ui.cli.CliApplication;
import com.flc.ui.swing.SwingLauncher;

public record BootstrapContext(
        FlcFacade facade,
        CliApplication cliApplication,
        SwingLauncher swingLauncher
) {
}
