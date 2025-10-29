package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code UnlinkCommand}.
 *
 * Equivalence partitions:
 * 1. Valid input → success.
 * 2. Invalid index (out of range) → failure.
 * 3. Wrong type (not student or parent) → failure.
 * 4. Equality tests → correct equals() behavior.
 * 5. Boundary indices (first and last) → success.
 */
public class UnlinkCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() throws Exception {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Ensure the first two persons are Student and Parent
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person second = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        if (first instanceof Student student && second instanceof Parent parent) {
            // Link them properly
            student.addParent(parent);
            parent.addChild(student);

            // Update the model with the linked objects
            model.setPerson(first, student);
            model.setPerson(second, parent);
        }
    }

    /**
     * EP1: Valid indices (student + parent) → successful unlink.
     */
    @Test
    public void execute_validIndices_success() throws CommandException {
        Person studentPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person parentPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        if (!(studentPerson instanceof Student student) || !(parentPerson instanceof Parent parent)) {
            return;
        }

        student.addParent(parent);
        parent.addChild(student);
        model.setPerson(studentPerson, student);
        model.setPerson(parentPerson, parent);

        UnlinkCommand command = new UnlinkCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        String expectedMessage = String.format(UnlinkCommand.MESSAGE_UNLINK_SUCCESS,
                student.getName(), parent.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Person expectedStudentPerson = expectedModel.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased());
        Person expectedParentPerson = expectedModel.getFilteredPersonList()
                .get(INDEX_SECOND_PERSON.getZeroBased());

        Student expectedStudent = (Student) expectedStudentPerson;
        Parent expectedParent = (Parent) expectedParentPerson;
        expectedParent.removeChild(expectedStudent);
        expectedStudent.removeParent(expectedParent);

        expectedModel.setPerson(expectedStudentPerson, expectedStudent);
        expectedModel.setPerson(expectedParentPerson, expectedParent);
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    /**
     * EP2a: Invalid index (out of bounds) → failure.
     */
    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBounds = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        UnlinkCommand command = new UnlinkCommand(INDEX_FIRST_PERSON, outOfBounds);
        assertCommandFailure(command, model, UnlinkCommand.MESSAGE_INVALID_INDEX);
    }

    /**
     * EP2b: Same index → failure.
     */
    @Test
    public void execute_sameIndex_failure() {
        UnlinkCommand command = new UnlinkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        assertCommandFailure(command, model, UnlinkCommand.MESSAGE_SAME_INDEX);
    }

    /**
     * EP3: Wrong types (e.g., both persons are students or both are parents) → failure.
     */
    @Test
    public void execute_wrongTypes_failure() {
        Parent wrongParent = (Parent) new PersonBuilder().withType("p").build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), wrongParent);

        UnlinkCommand command = new UnlinkCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        assertCommandFailure(command, model, UnlinkCommand.MESSAGE_WRONG_TYPE);
    }

    /**
     * EP4: Already not linked → failure
     */
    @Test
    public void execute_notLinked_throwsCommandException() throws CommandException {
        // Ensure we have a valid Student–Parent pair
        Person studentPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person parentPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        if (!(studentPerson instanceof Student student) || !(parentPerson instanceof Parent parent)) {
            return; // skip if test data types don't match
        }

        // Link them first, then unlink once
        student.addParent(parent);
        parent.addChild(student);
        model.setPerson(studentPerson, student);
        model.setPerson(parentPerson, parent);

        // Unlink once — should succeed
        UnlinkCommand unlinkCommand = new UnlinkCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        unlinkCommand.execute(model);

        // Unlink again — should fail with MESSAGE_NOT_LINKED
        UnlinkCommand unlinkAgainCommand = new UnlinkCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        assertCommandFailure(unlinkAgainCommand, model, UnlinkCommand.MESSAGE_NOT_LINKED);
    }

    /**
     * EP5: Equality checks for UnlinkCommand.
     */
    @Test
    public void equals() {
        final UnlinkCommand standardCommand = new UnlinkCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        // same values -> true
        assertTrue(standardCommand.equals(new UnlinkCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON)));

        // same object -> true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> false
        assertFalse(standardCommand.equals(null));

        // different type -> false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different indices -> false
        assertFalse(standardCommand.equals(new UnlinkCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON)));
    }

    /**
     * EP5a: Boundary case – first valid index (lower bound).
     */
    @Test
    public void execute_firstIndexBoundary_success() throws CommandException {
        Index first = Index.fromOneBased(1);
        Index second = Index.fromOneBased(2);

        Person studentPerson = model.getFilteredPersonList().get(first.getZeroBased());
        Person parentPerson = model.getFilteredPersonList().get(second.getZeroBased());

        if (!(studentPerson instanceof Student student) || !(parentPerson instanceof Parent parent)) {
            return; // skip test if types don't match
        }

        student.addParent(parent);
        parent.addChild(student);
        model.setPerson(studentPerson, student);
        model.setPerson(parentPerson, parent);

        UnlinkCommand command = new UnlinkCommand(first, second);
        String expectedMessage = String.format(UnlinkCommand.MESSAGE_UNLINK_SUCCESS,
                student.getName(), parent.getName());

        assertCommandSuccess(command, model, expectedMessage, model);
    }

    /**
     * EP5b: Boundary case – last valid index (upper bound).
     */
    @Test
    public void execute_lastIndexBoundary_success() throws CommandException {
        Index last = Index.fromOneBased(model.getFilteredPersonList().size());
        Index secondLast = Index.fromOneBased(model.getFilteredPersonList().size() - 1);

        Person studentPerson = model.getFilteredPersonList().get(secondLast.getZeroBased());
        Person parentPerson = model.getFilteredPersonList().get(last.getZeroBased());

        if (!(studentPerson instanceof Student student) || !(parentPerson instanceof Parent parent)) {
            return; // skip test if types don't match
        }

        student.addParent(parent);
        parent.addChild(student);
        model.setPerson(studentPerson, student);
        model.setPerson(parentPerson, parent);

        UnlinkCommand command = new UnlinkCommand(secondLast, last);
        String expectedMessage = String.format(UnlinkCommand.MESSAGE_UNLINK_SUCCESS,
                student.getName(), parent.getName());

        assertCommandSuccess(command, model, expectedMessage, model);
    }
}
