package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Parent;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;

/**
 * Resets payment status to UNPAID for all contacts.
 * Command format: "reset all".
 */
public class ResetCommand extends Command {

    public static final String COMMAND_WORD = "reset";

    public static final String MESSAGE_USAGE = "reset all: Resets payment status to UNPAID for all contacts.\n"
            + "Example:" + COMMAND_WORD + " all (case sensitive)";

    public static final String MESSAGE_SUCCESS = "Payment status of all contacts has been reset to unpaid.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        List<Person> people = new ArrayList<>(model.getFilteredPersonList());

        for (int i = 0; i < people.size(); i++) {
            Person p = people.get(i);
            if (p instanceof Parent && p.getPaymentStatus().isPaid()) {
                try {
                    new PaidCommand(Index.fromZeroBased(i)).execute(model);
                } catch (CommandException e) {
                    Parent par = (Parent) p;
                    Parent updated = new Parent(par.getName(), par.getPhone(), par.getEmail(),
                            par.getAddress(), par.getNote(), par.getCost(),
                            new PaymentStatus(false), par.getTags());
                    model.setPerson(par, updated);
                }
            }
        }

        people = new ArrayList<>(model.getAddressBook().getPersonList());
        for (int i = 0; i < people.size(); i++) {
            Person p = people.get(i);
            if (p.getPaymentStatus().isPaid()) {
                new PaidCommand(Index.fromZeroBased(i)).execute(model);
            }
        }

        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ResetCommand;
    }
}
