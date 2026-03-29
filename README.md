# FLC Booking System

Java 17 coursework project for managing Furzefield Leisure Centre lesson bookings.

## Features

- View the weekend timetable by day or exercise type
- Register members
- Book, change, cancel, and attend lessons
- Record ratings and reviews after attendance
- Print monthly lesson and champion exercise reports
- Run the same core logic through either CLI or a thin Swing UI

## Project Structure

- `src/main/java/com/flc/domain`: core entities and enums
- `src/main/java/com/flc/service`: application services, facade, and reporting
- `src/main/java/com/flc/ui/cli`: command-line workflow
- `src/main/java/com/flc/ui/swing`: thin desktop wrapper
- `src/test/java/com/flc/service`: focused JUnit tests for rules and reports

## Build And Run

The project is configured for Maven and Java 17.

```bash
mvn test
mvn package
java -jar target/flc-booking-system-1.0.0.jar
java -jar target/flc-booking-system-1.0.0.jar --ui=swing
```

If Maven is not installed locally yet, install it first and then run the commands above.
