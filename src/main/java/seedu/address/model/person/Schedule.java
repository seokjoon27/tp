package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a schedule for a lesson.
 * Internally stores as string for backward compatibility.
 * Supports two formats:
 * "Mon 14:00"
 * "10-20-2025 16:00"
 */
public class Schedule {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public final String value;

    /**
     * Constructs a {@code value}.
     *
     * @param value A valid date and time.
     */
    public Schedule(String value) {
        requireNonNull(value);
        value = value.strip();

        if (!value.isEmpty()) {
            // validate input
            parse(value);
        }

        this.value = value;
    }

    /**
     * Parses the schedule string to check validity.
     * Returns true if valid, throws IllegalArgumentException if invalid.
     */
    public static void parse(String input) {
        input = input.strip();

        if (input.isEmpty()) {
            return;
        }

        String[] parts = input.split(" ");
        if (parts.length == 2) {
            try {
                DayOfWeek.valueOf(parts[0].toUpperCase());
                LocalTime.parse(parts[1], TIME_FORMAT);
                return;
            } catch (IllegalArgumentException | DateTimeParseException e) {
                System.out.println("Parse failed, " + e.getMessage());
            }

            try {
                LocalDate.parse(parts[0], DATE_FORMAT);
                LocalTime.parse(parts[1], TIME_FORMAT);
                return;
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid schedule format. Use either: "
                        + "Mon 14:00, 10-20-2025 16:00");
            }
        }

        throw new IllegalArgumentException("Invalid schedule format. Use either: "
                + "Mon 14:00, 10-20-2025 16:00");
    }

    @Override
    public String toString() {
        return value.isEmpty() ? "No schedule set" : value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Schedule
                && value.equals(((Schedule) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
