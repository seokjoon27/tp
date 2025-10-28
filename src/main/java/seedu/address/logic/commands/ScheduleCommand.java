package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Schedule;
import seedu.address.model.person.Student;

/**
 * Changes the schedule of an existing student in the address book.
 * Only students can have schedules.
 */
public class ScheduleCommand extends Command {

    public static final String COMMAND_WORD = "schedule";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds or edits the schedule of the student identified "
            + "by the index number used in the last person listing. "
            + "Existing schedule will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_SCHEDULE + "SCHEDULE\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_SCHEDULE + "Monday 10:00-12:00, Wednesday 14:00-15:00";

    public static final String MESSAGE_ADD_SCHEDULE_SUCCESS = "Updated schedule for Student: %1$s";
    public static final String MESSAGE_DELETE_SCHEDULE_SUCCESS = "Removed schedule from Student: %1$s";
    public static final String MESSAGE_NOT_STUDENT = "This person is not a student, so a schedule cannot be added.";

    private static final Logger logger = Logger.getLogger(ScheduleCommand.class.getName());

    private final Index index;
    private final Schedule schedule;

    /**
     * @param index of the person in the filtered person list to edit the schedule
     * @param schedule the new schedule to set
     */
    public ScheduleCommand(Index index, Schedule schedule) {
        requireAllNonNull(index, schedule);
        this.index = index;
        this.schedule = schedule;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireAllNonNull(model);
        logger.info("Executing ScheduleCommand for index " + index.getOneBased());
        List<Person> lastShownList = model.getFilteredPersonList();
        assert model.getFilteredPersonList() != null : "Filtered person list should not be null";

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        assert personToEdit != null : "Person to edit should not be null";

        // Ensure only students can have schedules
        if (!(personToEdit instanceof Student)) {
            throw new CommandException(MESSAGE_NOT_STUDENT);
        }

        Student studentToEdit = (Student) personToEdit;

        // Create a new Student with the updated schedule
        Student editedStudent = studentToEdit.withSchedule(schedule);

        model.setPerson(studentToEdit, editedStudent);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(generateSuccessMessage(editedStudent));
    }

    /**
     * Generates a command execution success message based on whether the schedule
     * was added or removed from {@code studentToEdit}.
     */
    private String generateSuccessMessage(Student studentToEdit) {
        String message = !schedule.value.isEmpty()
                ? MESSAGE_ADD_SCHEDULE_SUCCESS
                : MESSAGE_DELETE_SCHEDULE_SUCCESS;
        return String.format(message, studentToEdit.getName());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ScheduleCommand)) {
            return false;
        }

        ScheduleCommand e = (ScheduleCommand) other;
        return index.equals(e.index)
                && schedule.equals(e.schedule);
    }
}
