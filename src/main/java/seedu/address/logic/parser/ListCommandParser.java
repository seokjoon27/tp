package seedu.address.logic.parser;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.person.Student;


/**
 * Parses input arguments and creates a new {@link ListCommand}.
 * Supported forms:
 *   {@code list} — show all persons
 *   {@code list paid} — show only persons with paid status
 *   {@code list unpaid} — show only persons with unpaid status
 *   {@code list schedule} — show only students (who have schedules)
 *   {@code list <DAY>} — show students with schedules on a specific day (e.g., Monday)
 *   {@code list <DATE>} — show students with schedules on a specific date (e.g., 12-12-2025)
 * Any other argument is rejected with a {@link ParseException}.
 */
public class ListCommandParser implements Parser<ListCommand> {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    @Override
    public ListCommand parse(String args) throws ParseException {
        String trimmed = normalizeInput(args);
        if (trimmed.isEmpty()) {
            return new ListCommand(Model.PREDICATE_SHOW_ALL_PERSONS, "Listed all persons.");
        }

        // Delegate specific logic for clarity
        switch (trimmed.toLowerCase(java.util.Locale.ROOT)) {
        case "paid":
            return makePaidListCommand();
        case "unpaid":
            return makeUnpaidListCommand();
        case "schedule":
            return makeScheduleListCommand();
        default:
            return handleDayOrDateList(trimmed);
        }
    }

    /**
     * Normalizes input by trimming and replacing multiple spaces with a single space.
     */
    private String normalizeInput(String args) {
        return args == null ? "" : args.trim().replaceAll("\\s+", " ");
    }

    private ListCommand makePaidListCommand() {
        return new ListCommand(
                p -> p.getPaymentStatus() != null && p.getPaymentStatus().isPaid(),
                "Listed persons with payment status: PAID"
        );
    }

    private ListCommand makeUnpaidListCommand() {
        return new ListCommand(
                p -> p.getPaymentStatus() != null && !p.getPaymentStatus().isPaid(),
                "Listed persons with payment status: UNPAID"
        );
    }

    private ListCommand makeScheduleListCommand() {
        return new ListCommand(
                p -> p instanceof Student
                        && ((Student) p).getSchedule() != null
                        && !((Student) p).getSchedule().isEmpty(),
                "Listed students with a schedule."
        );
    }

    /**
     * Handles list filtering by day or date.
     */
    private ListCommand handleDayOrDateList(String trimmed) throws ParseException {
        DayOfWeek day = tryParseDay(trimmed);
        if (day != null) {
            return makeDayListCommand(day);
        }

        LocalDate date = tryParseDate(trimmed);
        if (date != null) {
            return makeDateListCommand(date);
        }

        throw new ParseException(getInvalidListMessage(trimmed));
    }

    /**
     * Attempts to parse input as a day of the week.
     */
    private DayOfWeek tryParseDay(String input) {
        try {
            return DayOfWeek.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Attempts to parse input as a date in MM-dd-yyyy format.
     */
    private LocalDate tryParseDate(String input) {
        try {
            return LocalDate.parse(input, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private ListCommand makeDayListCommand(DayOfWeek day) {
        return new ListCommand(
                p -> p instanceof Student
                        && ((Student) p).getSchedule() != null
                        && day.equals(((Student) p).getSchedule().getDayOfWeek()),
                "Listed students with schedule on " + capitalize(day.name()) + "."
        );
    }

    private ListCommand makeDateListCommand(LocalDate date) {
        return new ListCommand(
                p -> p instanceof Student
                        && ((Student) p).getSchedule() != null
                        && date.equals(((Student) p).getSchedule().getDate()),
                "Listed students with schedule on " + DATE_FORMAT.format(date) + "."
        );
    }

    private String getInvalidListMessage(String trimmed) {
        return "Invalid list argument: \"" + trimmed + "\"\n"
                + "Use:\n"
                + "  list          (show all)\n"
                + "  list paid     (filter paid)\n"
                + "  list unpaid   (filter unpaid)\n"
                + "  list schedule (filter schedules)\n"
                + "  list <DAY>    (e.g., list Monday)\n"
                + "  list <DATE>   (e.g., list 12-12-2025)\n"
                + "For name search, use: find n/<keywords>";
    }

    private static String capitalize(String s) {
        if (s.isEmpty()) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
