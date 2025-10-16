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
 * "Monday 14:00-16:00"
 * "10-20-2025 14:00-16:00"
 */
public class Schedule {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public final String value;

    /**
     * Constructs a {@code Schedule}.
     *
     * @param value A valid date and time range.
     */
    public Schedule(String value) {
        requireNonNull(value);
        value = value.strip();

        if (!value.isEmpty()) {
            parse(value);
        }

        this.value = value;
    }

    /**
     * Parses the schedule string to check validity.
     * Throws IllegalArgumentException if invalid.
     */
    public static void parse(String input) {
        input = input.strip();

        if (input.isEmpty()) {
            return;
        }

        String[] parts = input.split(" ");
        if (parts.length == 2) {
            String dayOrDate = parts[0];
            String timeRange = parts[1];

            // Expecting "HH:mm-HH:mm"
            String[] timeParts = timeRange.split("-");
            if (timeParts.length != 2) {
                throw new IllegalArgumentException("Invalid time range format. Use HH:mm-HH:mm");
            }

            LocalTime startTime;
            LocalTime endTime;
            try {
                startTime = LocalTime.parse(timeParts[0], TIME_FORMAT);
                endTime = LocalTime.parse(timeParts[1], TIME_FORMAT);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid time format. Use HH:mm-HH:mm");
            }

            if (!endTime.isAfter(startTime)) {
                throw new IllegalArgumentException("End time must be after start time.");
            }

            // Validate either day of week or date format
            try {
                DayOfWeek.valueOf(dayOrDate.toUpperCase());
                return;
            } catch (IllegalArgumentException e) {
                try {
                    LocalDate.parse(dayOrDate, DATE_FORMAT);
                    return;
                } catch (DateTimeParseException ex) {
                    throw new IllegalArgumentException("Invalid schedule format. Use either: "
                            + "Monday 14:00-16:00 or 10-20-2025 14:00-16:00");
                }
            }
        }

        throw new IllegalArgumentException("Invalid schedule format. Use either: "
                + "Monday 14:00-16:00 or 10-20-2025 14:00-16:00");
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
