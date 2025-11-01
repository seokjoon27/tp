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
            "Invalid schedule format. Use either: DAY HH:mm-HH:mm or MM-DD-YYYY HH:mm-HH:mm " +
                    "Example: 'Monday 14:00-16:00', '12-10-2025 14:00-16:00' End time must be after start time" +
                    " and cannot cross midnight.";
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
        this.date = parsed.getDate();
        this.dayOfWeek = parsed.getDayOfWeek();
        this.startTime = parsed.getStartTime();
        this.endTime = parsed.getEndTime();

        if (this.dayOfWeek != null) {
            String dayStr = this.dayOfWeek.toString().substring(0, 1).toUpperCase()
                    + this.dayOfWeek.toString().substring(1).toLowerCase();
            this.value = dayStr + " " + TIME_FORMAT.format(this.startTime) + "-" + TIME_FORMAT.format(this.endTime);
        } else if (this.date != null) {
            this.value = DATE_FORMAT.format(this.date) + " " + TIME_FORMAT.format(this.startTime)
                    + "-" + TIME_FORMAT.format(this.endTime);
        } else {
            this.value = "";
        }
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
     * Parses and validates a schedule string into a {@link ParsedSchedule}.
     * Supports formats: "DAY HH:mm-HH:mm" or "MM-DD-YYYY HH:mm-HH:mm".
     * @param input The schedule string to parse.
     * @return A {@link ParsedSchedule} object with parsed date/day and times.
     * @throws IllegalArgumentException if input is invalid.
     */
    private static ParsedSchedule parse(String input) {
        input = normalizeInput(input);
        if (input.isEmpty()) {
            return new ParsedSchedule();
        }

        String dayOrDate = extractDayOrDate(input);
        String timeRange = extractTimeRange(input);

        ParsedSchedule result = new ParsedSchedule();
        parseTimeRange(timeRange, result);
        parseDayOrDate(dayOrDate, result);

        return result;
    }

    /**
     * Normalizes input by trimming and replacing multiple spaces with a single space.
     * @param input Raw schedule string.
     * @return Normalized schedule string.
     */
    private static String normalizeInput(String input) {
        return input.strip().replaceAll("\\s+", " ");
    }

    /**
     * Extracts the first part of the schedule string representing day or date.
     * @param input Normalized schedule string.
     * @return Day of week or date portion of the input.
     * @throws IllegalArgumentException if input does not contain a space separator.
     */
    private static String extractDayOrDate(String input) {
        int firstSpace = input.indexOf(' ');
        if (firstSpace == -1) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        return input.substring(0, firstSpace);
    }

    /**
     * Extracts the time range portion from the schedule string.
     * @param input Normalized schedule string.
     * @return Time range string (e.g., "14:00-16:00").
     */
    private static String extractTimeRange(String input) {
        int firstSpace = input.indexOf(' ');
        return input.substring(firstSpace + 1).strip();
    }

    /**
     * Parses a time range string into start and end times, and stores them in the result.
     * @param timeRange Time range string (format "HH:mm-HH:mm").
     * @param result ParsedSchedule object to store start and end times.
     * @throws IllegalArgumentException if time format is invalid or end time is before start time.
     */
    private static void parseTimeRange(String timeRange, ParsedSchedule result) {
        String[] parts = timeRange.split("\\s*-\\s*");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid time range format. Use HH:mm-HH:mm (e.g., 14:00-16:00).");
        }
        try {
            result.startTime = LocalTime.parse(parts[0], TIME_FORMAT);
            result.endTime = LocalTime.parse(parts[1], TIME_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format. Time must be in HH:mm (24-hour) format.");
        }
        if (!result.endTime.isAfter(result.startTime)) {
            throw new IllegalArgumentException("Invalid time format. End time must be after start time " +
                    "and cannot cross midnight (e.g., 14:00-16:00).");
        }
    }

    /**
     * Parses a string representing a day or date and stores it in the result.
     * @param dayOrDate String representing a day (e.g., "Monday") or date (e.g., "12-10-2025").
     * @param result ParsedSchedule object to store the dayOfWeek or date.
     * @throws IllegalArgumentException if the string is not a valid day or date.
     */
    private static void parseDayOrDate(String dayOrDate, ParsedSchedule result) {
        try {
            result.dayOfWeek = DayOfWeek.valueOf(dayOrDate.toUpperCase());
            result.date = null;
        } catch (IllegalArgumentException ignored) {
            try {
                result.date = LocalDate.parse(dayOrDate, DATE_FORMAT);
                result.dayOfWeek = null;
            } catch (DateTimeParseException ex) {
                if (dayOrDate.matches(".*\\d.*")) {
                    throw new IllegalArgumentException("Invalid date format. Use MM-DD-YYYY (e.g., 10-20-2025).");
                } else {
                    throw new IllegalArgumentException("Invalid day format. Use full day name (e.g., Monday).");
                }
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
        if (other == this) {
            return true;
        }

        if (!(other instanceof Schedule)) {
            return false;
        }

        Schedule otherSchedule = (Schedule) other;
        boolean isSameValue = value.equals(otherSchedule.value);

        return isSameValue;
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
