package com.flc.ui.cli;

import com.flc.service.FlcFacade;

import java.io.PrintStream;
import java.util.Scanner;

public final class CliApplication {
    private final MenuRouter menuRouter;
    private final PrintStream out;
    private final ConsolePrompts prompts;

    public CliApplication(FlcFacade facade) {
        this.out = System.out;
        this.prompts = new ConsolePrompts(new Scanner(System.in), out);
        this.menuRouter = new MenuRouter(facade, prompts, out);
    }

    public void run() {
        out.println("FLC Booking System");
        boolean running = true;
        while (running) {
            printMenu();
            int choice = prompts.readMenuChoice();
            running = menuRouter.handle(choice);
            out.println();
        }
        out.println("Goodbye.");
    }

    private void printMenu() {
        out.println("1. View timetable by day");
        out.println("2. View timetable by exercise");
        out.println("3. Register member");
        out.println("4. List members");
        out.println("5. Book lesson");
        out.println("6. Change booking");
        out.println("7. Cancel booking");
        out.println("8. Attend lesson");
        out.println("9. View member bookings");
        out.println("10. Monthly lesson report");
        out.println("11. Monthly champion report");
        out.println("0. Exit");
    }
}
