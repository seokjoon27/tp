package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_MISSING_REQUIRED_FIELDS =
            "Missing required field(s): %1$s\n%2$s";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     * Include Schedule only if the person is a student.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();

        builder.append(person.getName())
                .append(" (")
                .append(person.getType())
                .append(")")
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Email: ")
                .append(person.getEmail());

        if (person.getNote() != null && !person.getNote().value.isEmpty()) {
            builder.append("; Note: ").append(person.getNote());
        }

        if (person instanceof Student) {
            Student student = (Student) person;
            if (student.getSchedule() != null) {
                builder.append("; Schedule: ").append(student.getSchedule());
            }
        }

        builder.append("; Cost: ").append(person.getCost());

        if (!person.getTags().isEmpty()) {
            builder.append("; Tags: ");
            person.getTags().forEach(builder::append);
        }

        return builder.toString();
    }
}
