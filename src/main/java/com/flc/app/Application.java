package com.flc.app;

import com.flc.bootstrap.ApplicationFactory;
import com.flc.bootstrap.BootstrapContext;

public final class Application {
    private Application() {
    }

    public static void main(String[] args) {
        BootstrapContext context = new ApplicationFactory().create();
        if (args.length > 0 && "--ui=swing".equalsIgnoreCase(args[0])) {
            context.swingLauncher().launch();
            return;
        }
        context.cliApplication().run();
    }
}
