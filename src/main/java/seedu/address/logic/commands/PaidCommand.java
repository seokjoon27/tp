package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.Parent;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

/**
 * Marks a person as having completed payment.
 */
public class PaidCommand extends Command {

    public static final String COMMAND_WORD = "paid";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the person identified by the name as paid.\n"
            + "Parameters: " + PREFIX_NAME + "NAME\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "Alex Yeoh";

    public static final String MESSAGE_MARK_PAID_SUCCESS = "Marked as paid: %1$s";
    public static final String MESSAGE_ALREADY_PAID = "%1$s has already been marked as paid.";
    public static final String MESSAGE_PERSON_NOT_FOUND = "No person with the name %1$s was found.";

    private final Name name;

    public PaidCommand(Name name) {
        this.name = name;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> personList = model.getAddressBook().getPersonList();

        Person personToMark = personList.stream()
                .filter(person -> person.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND, name)));

        if (personToMark.getPaymentStatus().isPaid()) {
            throw new CommandException(String.format(MESSAGE_ALREADY_PAID, name));
        }

        Person markedPerson;
        PaymentStatus paidStatus = new PaymentStatus(true);
        if (personToMark.getType().isStudent()) {
            Student studentToMark = (Student) personToMark; // safe cast
            markedPerson = new Student(studentToMark.getName(), studentToMark.getPhone(), studentToMark.getEmail(),
                    studentToMark.getAddress(), studentToMark.getNote(), studentToMark.getSchedule(), studentToMark.getCost(), paidStatus,
                    studentToMark.getTags());
        } else {
            markedPerson = new Parent(personToMark.getName(), personToMark.getPhone(), personToMark.getEmail(),
                    personToMark.getAddress(), personToMark.getNote(), personToMark.getCost(), paidStatus,
                    personToMark.getTags());
        }

        model.setPerson(personToMark, markedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_MARK_PAID_SUCCESS, Messages.format(markedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof PaidCommand
                && name.equals(((PaidCommand) other).name));
    }
}
