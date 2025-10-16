package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Parent;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

/**
 * Resets payment status to UNPAID for all contacts.
 * Command format: "reset all".
 */
public class ResetCommand extends Command {

    public static final String COMMAND_WORD = "reset";

    public static final String MESSAGE_USAGE = "reset all: Resets payment status to UNPAID for all contacts.\n"
            + "Example: reset all";

    public static final String MESSAGE_SUCCESS = "Payment status of all contacts has been reset to unpaid.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> people = model.getAddressBook().getPersonList();

        for (Person p : people) {
            if (p.getPaymentStatus() != null && !p.getPaymentStatus().isPaid()) {
                continue;
            }

            if (p instanceof Student) {
                Student s = (Student) p;

                Student updated = new Student(
                        s.getName(),
                        s.getPhone(),
                        s.getEmail(),
                        s.getAddress(),
                        s.getNote(),
                        s.getSchedule(),
                        s.getCost(),
                        new PaymentStatus(false),
                        s.getTags()
                );

                model.setPerson(s, updated);

            } else if (p instanceof Parent) {
                Parent par = (Parent) p;

                Parent updated = new Parent(
                        par.getName(),
                        par.getPhone(),
                        par.getEmail(),
                        par.getAddress(),
                        par.getNote(),
                        par.getCost(),
                        new PaymentStatus(false),
                        par.getTags()
                );

                model.setPerson(par, updated);
            }
        }

        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof ResetCommand;
    }
}
