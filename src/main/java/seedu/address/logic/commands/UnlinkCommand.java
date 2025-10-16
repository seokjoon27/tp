package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

/**
 * Unlinks a student and parent identified using it's displayed index from the address book.
 */
public class UnlinkCommand extends Command {
    public static final String COMMAND_WORD = "unlink";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": unlinks a student to a parent by their respective"
            + " index numbers used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " student/ 1 parent/ 2";
    public static final String MESSAGE_UNLINK_SUCCESS = "Unlinked student %1$s to parent %2$s.";
    public static final String MESSAGE_INVALID_INDEX = "Invalid parent or student index.";
    public static final String MESSAGE_WRONG_TYPE = "Please ensure one student and one parent is input respectively.";

    private final Index parentIndex;
    private final Index studentIndex;

    public UnlinkCommand(Index student, Index parent) {
        this.studentIndex = student;
        this.parentIndex = parent;

    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        if (parentIndex.getZeroBased() >= lastShownList.size()
                || studentIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_INDEX);
        }
        Person parentToUnlink = lastShownList.get(parentIndex.getZeroBased());
        Person studentToUnlink = lastShownList.get(studentIndex.getZeroBased());
        if (!(parentToUnlink instanceof Parent) || !(studentToUnlink instanceof Student)) {
            throw new CommandException(MESSAGE_WRONG_TYPE);
        }

        Parent parent = (Parent) parentToUnlink;
        Student student = (Student) studentToUnlink;

        parent.removeChild(student);
        student.removeParent(parent);

        model.setPerson(parentToUnlink, parent);
        model.setPerson(studentToUnlink, student);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_UNLINK_SUCCESS, student.getName(), parent.getName()));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnlinkCommand)) {
            return false;
        }

        // state check
        UnlinkCommand otherUnlinkCommand = (UnlinkCommand) other;
        return parentIndex.equals(otherUnlinkCommand.parentIndex)
                && studentIndex.equals(otherUnlinkCommand.studentIndex);
    }
}
