package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. "
            + "Parameters: "
            + PREFIX_TYPE + "TYPE "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_NOTE + "NOTE] "
            + "[" + PREFIX_SCHEDULE + "SCHEDULE (students only)] "
            + "[" + PREFIX_PAY + "COST] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example (Student): " + COMMAND_WORD + " "
            + PREFIX_TYPE + "s "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_NOTE + "Prefers online lessons "
            + PREFIX_SCHEDULE + "Monday 14:00-16:00 "
            + PREFIX_PAY + "100 "
            + PREFIX_TAG + "friends\n"
            + "Example (Parent): " + COMMAND_WORD + " "
            + PREFIX_TYPE + "p "
            + PREFIX_NAME + "Jane Smith "
            + PREFIX_PHONE + "87654321 "
            + PREFIX_EMAIL + "janes@example.com "
            + PREFIX_ADDRESS + "123, Clementi Road, #04-12 "
            + PREFIX_NOTE + "Mother of John";

    public static final String MESSAGE_SUCCESS =
            "New person added: %1$s";
    public static final String MESSAGE_SUCCESS_WITH_COST =
            "New person with cost information added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON =
            "This person already exists in the address book.";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}.
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        String capitalizedName = capitalizeName(toAdd.getName().fullName);
        Person capitalizedPerson = createCapitalizedCopy(toAdd, capitalizedName);
        model.addPerson(capitalizedPerson);

        final String messageTemplate = (toAdd.getCost() != null)
                ? MESSAGE_SUCCESS_WITH_COST
                : MESSAGE_SUCCESS;

        return new CommandResult(String.format(messageTemplate, toAdd.getName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof AddCommand
                && toAdd.equals(((AddCommand) other).toAdd));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }

    /**
     * Capitalizes the first letter of each word in the given name.
     */
    static String capitalizeName(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        String[] words = name.trim().split("\\s+");
        StringBuilder capitalized = new StringBuilder();

        for (String word : words) {
            if (word.length() > 1) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase());
            } else {
                capitalized.append(word.toUpperCase());
            }
            capitalized.append(" ");
        }

        return capitalized.toString().trim();
    }

    private Person createCapitalizedCopy(Person original, String newName) {
        return original instanceof Student
                ? new Student(new Name(newName),
                original.getPhone(),
                original.getEmail(),
                original.getAddress(),
                original.getNote(), ((Student) original).getSchedule(),
                original.getCost(),
                original.getPaymentStatus(),
                original.getTags())
                : new Parent(new Name(newName),
                original.getPhone(),
                original.getEmail(),
                original.getAddress(),
                original.getNote(),
                original.getCost(),
                original.getPaymentStatus(),
                original.getTags());
    }

}
