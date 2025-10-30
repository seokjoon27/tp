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
 * Unlinks a student and a parent identified using their displayed indexes from the address book.
 */
public class UnlinkCommand extends Command {

    public static final String COMMAND_WORD = "unlink";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unlinks a student and a parent by their respective "
            + "index numbers shown in the displayed person list.\n"
            + "Parameters: student/STUDENT_INDEX parent/PARENT_INDEX (must be positive integers)\n"
            + "Example: " + COMMAND_WORD + " student/1 parent/2";

    public static final String MESSAGE_UNLINK_SUCCESS = "Unlinked %2$s from %1$s.";
    public static final String MESSAGE_INVALID_INDEX = "Invalid student or parent index.";
    public static final String MESSAGE_WRONG_TYPE = "Please ensure one student and one parent are input respectively.";
    public static final String MESSAGE_SAME_INDEX = "You cannot unlink a person to themselves.";
    public static final String MESSAGE_NOT_LINKED = "These two people are already not linked.";

    private final Index studentIndex;
    private final Index parentIndex;

    /**
     * Constructs an {@code UnlinkCommand} with the specified student and parent indexes.
     *
     * @param studentIndex the {@link Index} of the student to be unlinked
     * @param parentIndex the {@link Index} of the parent to be unlinked
     */
    public UnlinkCommand(Index studentIndex, Index parentIndex) {
        requireNonNull(studentIndex);
        requireNonNull(parentIndex);
        this.studentIndex = studentIndex;
        this.parentIndex = parentIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Check index bounds
        if (studentIndex.getZeroBased() >= lastShownList.size()
                || parentIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_INDEX);
        }

        if (studentIndex.equals(parentIndex)) {
            throw new CommandException(MESSAGE_SAME_INDEX);
        }

        Person studentPerson = lastShownList.get(studentIndex.getZeroBased());
        Person parentPerson = lastShownList.get(parentIndex.getZeroBased());

        // Validate types
        if (!(studentPerson instanceof Student) || !(parentPerson instanceof Parent)) {
            throw new CommandException(MESSAGE_WRONG_TYPE);
        }

        Student student = (Student) studentPerson;
        Parent parent = (Parent) parentPerson;

        // Check if they are currently linked
        if (!student.getParents().contains(parent) || !parent.getChildren().contains(student)) {
            throw new CommandException(MESSAGE_NOT_LINKED);
        }

        // Perform unlink
        parent.removeChild(student);
        student.removeParent(parent);

        // Update model
        model.setPerson(parentPerson, parent);
        model.setPerson(studentPerson, student);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_UNLINK_SUCCESS, student.getName(), parent.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnlinkCommand)) {
            return false;
        }

        UnlinkCommand otherUnlinkCommand = (UnlinkCommand) other;
        return parentIndex.equals(otherUnlinkCommand.parentIndex)
                && studentIndex.equals(otherUnlinkCommand.studentIndex);
    }
}
