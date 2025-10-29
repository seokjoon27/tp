package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Lists persons in the address book based on the provided predicate.
 * Supports:
 *   {@code list} — show all persons
 *   {@code list paid} — show only persons whose payment status is paid
 *   {@code list unpaid} — show only persons whose payment status is unpaid
 *   {@code list schedule} — show only students (who have schedules)
 *   {@code list <DAY>} — show students with schedules on a specific day (e.g., Monday)
 *   {@code list <DATE>} — show students with schedules on a specific date (e.g., 12-12-2025)
 */
public class ListCommand extends Command {

    /** Keyword used to invoke this command. */
    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = "list\n"
            + "Examples:\n"
            + "  list                (show all)\n"
            + "  list paid           (show only paid)\n"
            + "  list unpaid         (show only unpaid)\n"
            + "  list schedule       (show only contacts with schedules)\n"
            + "  list <DAY>          — show schedules on a specific day (e.g., list Monday)"
            + "  list <DATE>         — show schedules on a specific date (e.g., list 12-12-2025)";

    /** Default success message shown when listing all persons. */
    public static final String MESSAGE_SUCCESS = "Listed all persons.";

    private final Predicate<Person> predicate;
    private final String successMessage;

    /**
     * Creates a {@code ListCommand} with a filtering predicate and success message.
     *
     * @param predicate the filter condition determining which persons to show
     * @param successMessage the message to display upon successful execution
     */
    public ListCommand(Predicate<Person> predicate, String successMessage) {
        this.predicate = requireNonNull(predicate);
        this.successMessage = requireNonNull(successMessage);
    }

    /**
     * Creates a {@code ListCommand} that lists all persons.
     */
    public ListCommand() {
        this(person -> true, MESSAGE_SUCCESS);
    }

    /**
     * Executes the command by updating the model’s filtered person list
     * according to the provided predicate.
     *
     * @param model the model containing the person list
     * @return a {@code CommandResult} containing the success message
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        System.out.println("Filtered list size: " + model.getFilteredPersonList().size());
        return new CommandResult(successMessage);
    }
}
