package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import seedu.address.logic.Messages;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;

public class RemarkCommand extends Command {
    public static final String COMMAND_WORD = "remark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the remark of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing remark will be overwritten by the input.\n"
            + "Parameters: INDEX (positive integer) r/REMARK\n"
            + "Example: " + COMMAND_WORD + " 1 r/Likes to swim.";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET =
            "Remark command not implemented yet";

    private final Index index;
    private final String remark;

    public RemarkCommand(Index index, String remark) {
        requireNonNull(index);
        requireNonNull(remark);
        this.index = index;
        this.remark = remark;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                new Remark(remark), // ✅ apply the new remark
                personToEdit.getTags()
        );

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult("Added remark to Person: " + personToEdit.getName());
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof RemarkCommand
                && index.equals(((RemarkCommand) other).index)
                && remark.equals(((RemarkCommand) other).remark));
    }
}
