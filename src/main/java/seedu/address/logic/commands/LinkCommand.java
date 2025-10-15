package seedu.address.logic.commands;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class LinkCommand extends Command {
    public static final String COMMAND_WORD = "link";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Links a student to a parent by their respective"
            + " index numbers used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " student/ 1 parent/ 2";
    public static final String MESSAGE_LINK_SUCCESS = "Linked student %1$s to parent %2$s.";
    public static final String MESSAGE_INVALID_INDEX = "Invalid parent or student index.";
    public static final String MESSAGE_WRONG_TYPE = "Please ensure one student and one parent is input respectively.";

    private final Index parentIndex;
    private final Index studentIndex;

    public LinkCommand(Index student, Index parent) {
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
        Person parentToLink = lastShownList.get(parentIndex.getZeroBased());
        Person studentToLink = lastShownList.get(studentIndex.getZeroBased());
        System.out.println("Student index points to: " + studentToLink.getName() + " (" + studentToLink.getType().value + ")");
        System.out.println("Parent index points to: " + parentToLink.getName() + " (" + parentToLink.getType().value + ")");

        if (!(parentToLink instanceof Parent) || !(studentToLink instanceof Student)) {
            throw new CommandException(MESSAGE_WRONG_TYPE);
        }

        Parent parent = (Parent) parentToLink;
        Student student = (Student) studentToLink;

        parent.addChild(student);
        student.addParent(parent);

        model.setPerson(parentToLink, parent);
        model.setPerson(studentToLink, student);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_LINK_SUCCESS, student.getName(), parent.getName()));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof LinkCommand)) {
            return false;
        }

        // state check
        LinkCommand otherLinkCommand = (LinkCommand) other;
        return parentIndex.equals(otherLinkCommand.parentIndex)
                && studentIndex.equals(otherLinkCommand.studentIndex);
    }
}
