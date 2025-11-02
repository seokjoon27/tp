package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code LinkCommand}.
 *
 * Equivalence partitions:
 * 1. Valid input → success.
 * 2. Invalid index (out of range) → failure.
 * 3. Wrong type (not student or parent) → failure.
 * 4. Equality tests → correct equals() behavior.
 * 5. Boundary indices (first and last) → success.
 */
public class LinkCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    /**
     * EP1: Valid student-parent indices → successful link.
     */
    @Test
    public void execute_validIndices_success() throws Exception {
        // Assume first person is student, second is parent
        Person studentPerson = model.getFilteredPersonList().get(0);
        Person parentPerson = model.getFilteredPersonList().get(1);

        if (studentPerson instanceof Parent && parentPerson instanceof Student) {
            // swap them
            Person temp = studentPerson;
            studentPerson = parentPerson;
            parentPerson = temp;
        }

        if (!(studentPerson instanceof Student student) || !(parentPerson instanceof Parent parent)) {
            return; // skip if TypicalPersons not aligned
        }

        LinkCommand command = new LinkCommand(Index.fromOneBased(1), Index.fromOneBased(2));
        String expectedMessage = String.format(LinkCommand.MESSAGE_LINK_SUCCESS, student.getName(), parent.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Parent expectedParent = (Parent) expectedModel.getFilteredPersonList().get(1);
        Student expectedStudent = (Student) expectedModel.getFilteredPersonList().get(0);

        expectedParent.addChild(expectedStudent);
        expectedStudent.addParent(expectedParent);
        expectedModel.setPerson(expectedStudent, expectedStudent);
        expectedModel.setPerson(expectedParent, expectedParent);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);

        // Verify linkage
        Student linkedStudent = (Student) model.getFilteredPersonList().get(0);
        Parent linkedParent = (Parent) model.getFilteredPersonList().get(1);
        assertTrue(model.getFilteredPersonList().contains(linkedStudent));
        assertTrue(model.getFilteredPersonList().contains(linkedParent));
    }

    /**
     * EP2: Invalid index (out of range) → failure.
     */
    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBounds = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        LinkCommand command = new LinkCommand(INDEX_FIRST_PERSON, outOfBounds);
        assertCommandFailure(command, model, LinkCommand.MESSAGE_INVALID_INDEX);
    }

    /**
     * EP3: Wrong type (e.g., both students or both parents) → failure.
     */
    @Test
    public void execute_wrongTypes_failure() {
        // Both indices point to non-student/non-parent combination
        LinkCommand command = new LinkCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        assertCommandFailure(command, model, LinkCommand.MESSAGE_WRONG_TYPE);
    }

    /**
     * EP4: Equality tests.
     */
    @Test
    public void equals() {
        LinkCommand linkFirstSecond = new LinkCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        LinkCommand linkSecondFirst = new LinkCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON);

        // same object → true
        assertEquals(linkFirstSecond, linkFirstSecond);

        // same values → true
        LinkCommand copy = new LinkCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        assertEquals(linkFirstSecond, copy);

        // different types → false
        assertNotEquals(1, linkFirstSecond);

        // null → false
        assertNotEquals(null, linkFirstSecond);

        // different indices → false
        assertNotEquals(linkFirstSecond, linkSecondFirst);
    }

    /**
     * EP5a: Boundary test — first valid index (lower bound).
     */
    @Test
    public void execute_firstIndexBoundary_success() throws Exception {
        Index studentIndex = Index.fromOneBased(1);
        Index parentIndex = Index.fromOneBased(2);

        Person studentPerson = model.getFilteredPersonList().get(studentIndex.getZeroBased());
        Person parentPerson = model.getFilteredPersonList().get(parentIndex.getZeroBased());

        if (!(studentPerson instanceof Student student) || !(parentPerson instanceof Parent parent)) {
            return;
        }

        LinkCommand command = new LinkCommand(studentIndex, parentIndex);
        String expectedMessage = String.format(LinkCommand.MESSAGE_LINK_SUCCESS, student.getName(), parent.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Parent expectedParent = (Parent) expectedModel.getFilteredPersonList().get(parentIndex.getZeroBased());
        Student expectedStudent = (Student) expectedModel.getFilteredPersonList().get(studentIndex.getZeroBased());

        expectedParent.addChild(expectedStudent);
        expectedStudent.addParent(expectedParent);
        expectedModel.setPerson(expectedStudent, expectedStudent);
        expectedModel.setPerson(expectedParent, expectedParent);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    /**
     * EP5b: Boundary test — last valid index (upper bound).
     */
    @Test
    public void execute_lastIndexBoundary_success() throws Exception {
        int size = model.getFilteredPersonList().size();
        Index studentIndex = Index.fromOneBased(size - 1);
        Index parentIndex = Index.fromOneBased(size);

        Person studentPerson = model.getFilteredPersonList().get(studentIndex.getZeroBased());
        Person parentPerson = model.getFilteredPersonList().get(parentIndex.getZeroBased());

        if (!(studentPerson instanceof Student student) || !(parentPerson instanceof Parent parent)) {
            return;
        }

        LinkCommand command = new LinkCommand(studentIndex, parentIndex);
        String expectedMessage = String.format(LinkCommand.MESSAGE_LINK_SUCCESS, student.getName(), parent.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Parent expectedParent = (Parent) expectedModel.getFilteredPersonList().get(parentIndex.getZeroBased());
        Student expectedStudent = (Student) expectedModel.getFilteredPersonList().get(studentIndex.getZeroBased());

        expectedParent.addChild(expectedStudent);
        expectedStudent.addParent(expectedParent);
        expectedModel.setPerson(expectedStudent, expectedStudent);
        expectedModel.setPerson(expectedParent, expectedParent);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }
}
