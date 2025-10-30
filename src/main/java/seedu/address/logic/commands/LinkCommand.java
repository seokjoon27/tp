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
 * Links a student and parent identified using it's displayed index from the address book.
 */
public class LinkCommand extends Command {

    public static final String COMMAND_WORD = "link";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Links a student to a parent using their indices.\n"
            + "Parameters: student/STUDENT_INDEX parent/PARENT_INDEX\n"
            + "Example: " + COMMAND_WORD + " student/1 parent/4";

    public static final String MESSAGE_LINK_SUCCESS = "Linked %1$s to %2$s";
    public static final String MESSAGE_INVALID_INDEX = "Invalid student or parent index.";
    public static final String MESSAGE_WRONG_TYPE = "Please ensure one student and one parent are input respectively.";
    public static final String MESSAGE_SAME_INDEX = "You cannot link a person to themselves.";
    public static final String MESSAGE_ALREADY_LINKED = "These two people are already linked.";

    private final Index studentIndex;
    private final Index parentIndex;

    /**
     * Creates a LinkCommand to link a student and a parent.
     */
    public LinkCommand(Index studentIndex, Index parentIndex) {
        requireNonNull(studentIndex);
        requireNonNull(parentIndex);
        this.studentIndex = studentIndex;
        this.parentIndex = parentIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (studentIndex.getZeroBased() >= lastShownList.size()
                || parentIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_INDEX); // EP2
        }

        if (studentIndex.equals(parentIndex)) {
            throw new CommandException(MESSAGE_SAME_INDEX); // EP4
        }

        Person studentPerson = lastShownList.get(studentIndex.getZeroBased());
        Person parentPerson = lastShownList.get(parentIndex.getZeroBased());

        if (!(studentPerson instanceof Student)) {
            throw new CommandException(MESSAGE_WRONG_TYPE);
        }

        if (!(parentPerson instanceof Parent)) {
            throw new CommandException(MESSAGE_WRONG_TYPE);
        }

        Student student = (Student) studentPerson;
        Parent parent = (Parent) parentPerson;

        // EP5: Already linked
        if (student.getParents().contains(parent) || parent.getChildren().contains(student)) {
            throw new CommandException(MESSAGE_ALREADY_LINKED);
        }

        // Perform linking
        student.addParent(parent);
        parent.addChild(student);

        model.setPerson(student, student);
        model.setPerson(parent, parent);

        String result = String.format(MESSAGE_LINK_SUCCESS, student.getName(), parent.getName());
        return new CommandResult(result);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof LinkCommand
                && studentIndex.equals(((LinkCommand) other).studentIndex)
                && parentIndex.equals(((LinkCommand) other).parentIndex));
    }
}
