package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

/**
 * Represents a schedule for a lesson.
 * Supports two formats:
 *  - "Monday 14:00-16:00"
 *  - "10-20-2025 14:00-16:00"
 */
public class Schedule implements Comparable<Schedule> {
    public static final String MESSAGE_CONSTRAINTS =
            "Invalid schedule format. Use either: 'Monday 14:00-16:00' or '10-20-2025 14:00-16:00'.";
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static final Logger logger = Logger.getLogger(Schedule.class.getName());

    public final String value;
    private final LocalDate date;
    private final DayOfWeek dayOfWeek;
    private final LocalTime startTime;
    private final LocalTime endTime;

    /**
     * Constructs a {@code Schedule}.
     *
     * @param value A valid date or day + time range.
     */
    public Schedule(String value) {
        requireNonNull(value);
        value = value.strip();

        assert value != null : "Schedule value should not be null after requireNonNull(value)";

        if (value.isEmpty()) {
            this.value = "";
            this.date = null;
            this.dayOfWeek = null;
            this.startTime = null;
            this.endTime = null;
            return;
        }

        ParsedSchedule parsed = parse(value);
        this.value = value;
        this.date = parsed.getDate();
        this.dayOfWeek = parsed.getDayOfWeek();
        this.startTime = parsed.getStartTime();
        this.endTime = parsed.getEndTime();
    }

    /**
     * Internal parsed result holder.
     */
    private static class ParsedSchedule {
        private LocalDate date;
        private DayOfWeek dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;

        public LocalDate getDate() {
            return date;
        }

        public DayOfWeek getDayOfWeek() {
            return dayOfWeek;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }
    }

    /**
     * Parses and validates the input schedule string.
     */
    private static ParsedSchedule parse(String input) {
        String[] parts = input.split(" ");
        if (parts.length != 2) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }

        String dayOrDate = parts[0];
        String timeRange = parts[1];
        String[] timeParts = timeRange.split("-");

        if (timeParts.length != 2) {
            throw new IllegalArgumentException("Invalid time range format. Use HH:mm-HH:mm");
        }

        ParsedSchedule result = new ParsedSchedule();

        try {
            result.startTime = LocalTime.parse(timeParts[0], TIME_FORMAT);
            result.endTime = LocalTime.parse(timeParts[1], TIME_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format. Use HH:mm-HH:mm");
        }

        if (!result.endTime.isAfter(result.startTime)) {
            throw new IllegalArgumentException("End time must be after start time.");
        }

        try {
            result.dayOfWeek = DayOfWeek.valueOf(dayOrDate.toUpperCase());
            result.date = null;
            return result;
        } catch (IllegalArgumentException ignored) {
            try {
                result.date = LocalDate.parse(dayOrDate, DATE_FORMAT);
                result.dayOfWeek = null;
                return result;
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
            }
        }
    }

    /**
     * Returns true if a given string is a valid schedule.
     */
    public static boolean isValidSchedule(String test) {
        if (test == null || test.strip().isEmpty()) {
            logger.fine("Empty schedule string — treating as valid.");
            return true;
        }
        try {
            parse(test.strip());
            logger.fine("Valid schedule: " + test);
            return true;
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid schedule detected: " + test + " — " + e.getMessage());
            return false;
        }
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public LocalDate getDate() {
        return date;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
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

    /**
     * Compare schedules chronologically.
     * Schedules with no date/time are treated as "after" valid ones.
     */
    @Override
    public int compareTo(Schedule other) {
        if (this.isEmpty() && other.isEmpty()) {
            return 0;
        }
        if (this.isEmpty()) {
            return 1;
        }
        if (other.isEmpty()) {
            return -1;
        }

        if (this.date != null && other.date != null) {
            int dateCompare = this.date.compareTo(other.date);
            return (dateCompare != 0) ? dateCompare : this.startTime.compareTo(other.startTime);
        }

        if (this.dayOfWeek != null && other.dayOfWeek != null) {
            int dayCompare = this.dayOfWeek.compareTo(other.dayOfWeek);
            return (dayCompare != 0) ? dayCompare : this.startTime.compareTo(other.startTime);
        }

        // If one uses date and the other uses day of week, date schedules come first
        return (this.date != null) ? -1 : 1;
    }
}
