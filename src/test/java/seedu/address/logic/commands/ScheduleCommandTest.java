package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Schedule;
import seedu.address.model.person.Student;
import seedu.address.testutil.PersonBuilder;

public class ScheduleCommandTest {

    private static final String SCHEDULE_STUB = "Monday 14:00-16:00";

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_addScheduleUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Ensure we are editing a student
        Student studentToEdit = (Student) new PersonBuilder(firstPerson).withType("s").build();
        model.setPerson(firstPerson, studentToEdit);

        Student editedStudent = (Student) new PersonBuilder(studentToEdit).withSchedule(SCHEDULE_STUB).build();

        ScheduleCommand scheduleCommand =
                new ScheduleCommand(INDEX_FIRST_PERSON, new Schedule(editedStudent.getSchedule().value));

        String expectedMessage = String.format(ScheduleCommand.MESSAGE_ADD_SCHEDULE_SUCCESS, editedStudent);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(studentToEdit, editedStudent);

        assertCommandSuccess(scheduleCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        ScheduleCommand scheduleCommand = new ScheduleCommand(outOfBoundIndex, new Schedule(SCHEDULE_STUB));

        assertCommandFailure(scheduleCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_nonStudent_failure() {
        Person parentPerson = new PersonBuilder().withType("p").build(); // Parent
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), parentPerson);

        ScheduleCommand scheduleCommand = new ScheduleCommand(INDEX_FIRST_PERSON, new Schedule(SCHEDULE_STUB));

        assertCommandFailure(scheduleCommand, model, ScheduleCommand.MESSAGE_NOT_STUDENT);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Student studentToEdit = (Student) new PersonBuilder(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased())).withType("s").build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), studentToEdit);

        Student editedStudent = (Student) new PersonBuilder(studentToEdit).withSchedule(SCHEDULE_STUB).build();

        ScheduleCommand scheduleCommand =
                new ScheduleCommand(INDEX_FIRST_PERSON, new Schedule(editedStudent.getSchedule().value));

        String expectedMessage = String.format(ScheduleCommand.MESSAGE_ADD_SCHEDULE_SUCCESS, editedStudent);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(studentToEdit, editedStudent);

        assertCommandSuccess(scheduleCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        final ScheduleCommand standardCommand = new ScheduleCommand(INDEX_FIRST_PERSON, new Schedule(SCHEDULE_STUB));

        // same values -> returns true
        ScheduleCommand commandWithSameValues = new ScheduleCommand(INDEX_FIRST_PERSON, new Schedule(SCHEDULE_STUB));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new ScheduleCommand(INDEX_SECOND_PERSON, new Schedule(SCHEDULE_STUB))));

        // different schedule -> returns false
        assertFalse(standardCommand.equals(new ScheduleCommand(INDEX_FIRST_PERSON, new Schedule("Tuesday 16:00-18:00"))));
    }
}
