package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Note;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

/**
 * Adds or removes a note for a specified person in the address book.
 * A note replaces the existing one and must be under 100 characters.
 */
public class NoteCommand extends Command {

    public static final String COMMAND_WORD = "note";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the Note of the person identified "
            + "by the index number used in the last person listing. "
            + "All Note must be less than 100 characters. "
            + "Existing Note will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_NOTE + "[NOTE]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NOTE + "Likes to swim.";

    public static final String MESSAGE_ADD_NOTE_SUCCESS = "Added Note to Person: %1$s";
    public static final String MESSAGE_DELETE_NOTE_SUCCESS = "Removed Note from Person: %1$s";

    private static final Logger logger = Logger.getLogger(NoteCommand.class.getName());

    private final Index index;
    private final Note note;

    /**
     * @param index of the person in the filtered person list to edit the Note
     * @param note of the person to be updated to
     */
    public NoteCommand(Index index, Note note) {
        requireAllNonNull(index, note);

        this.index = index;
        this.note = note;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        assert model != null : "Model cannot be null";

        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        logger.info("Executing NoteCommand for person: " + personToEdit.getName());

        Person editedPerson = createEditedPerson(personToEdit, note);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * Creates a copy of {@code personToEdit} with the updated note.
     */
    private Person createEditedPerson(Person personToEdit, Note updatedNote) {
        requireAllNonNull(personToEdit, updatedNote);

        if (personToEdit instanceof Student student) {
            return new Student(
                    student.getName(),
                    student.getPhone(),
                    student.getEmail(),
                    student.getAddress(),
                    updatedNote,
                    student.getSchedule(),
                    student.getCost(),
                    student.getPaymentStatus(),
                    student.getTags()
            );
        } else if (personToEdit instanceof Parent parent) {
            return new Parent(
                    parent.getName(),
                    parent.getPhone(),
                    parent.getEmail(),
                    parent.getAddress(),
                    updatedNote,
                    parent.getCost(),
                    parent.getPaymentStatus(),
                    parent.getTags()
            );
        }

        // Defensive coding: should never reach here if all Person types are handled
        assert false : "Unhandled Person subtype in NoteCommand";
        return personToEdit;
    }

    /**
     * Generates a command execution success message based on whether the Note is added to or removed from
     * {@code personToEdit}.
     */
    private String generateSuccessMessage(Person personToEdit) {
        String message = !note.value.isEmpty() ? MESSAGE_ADD_NOTE_SUCCESS : MESSAGE_DELETE_NOTE_SUCCESS;
        return String.format(message, personToEdit.getName());
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof NoteCommand
                && index.equals(((NoteCommand) other).index)
                && note.equals(((NoteCommand) other).note));
    }
}
