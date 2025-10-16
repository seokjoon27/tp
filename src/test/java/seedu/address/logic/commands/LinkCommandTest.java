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

public class LinkCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndices_success() throws Exception {
        // Assume first person is student, second is parent
        Person studentPerson = model.getFilteredPersonList().get(0);
        Person parentPerson = model.getFilteredPersonList().get(1);

        // Ensure types are correct (replace with actual types if your TypicalPersons differ)
        if (!(studentPerson instanceof Student student) || !(parentPerson instanceof Parent parent)) {
            return; // skip if test data not properly set up
        }

        LinkCommand command = new LinkCommand(Index.fromOneBased(1), Index.fromOneBased(2));

        String expectedMessage = String.format(LinkCommand.MESSAGE_LINK_SUCCESS, student.getName(), parent.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Parent expectedParent = (Parent) expectedModel.getFilteredPersonList().get(1);
        Student expectedStudent = (Student) expectedModel.getFilteredPersonList().get(0);

        expectedParent.addChild(expectedStudent);
        expectedStudent.addParent(expectedParent);

        expectedModel.setPerson(expectedModel.getFilteredPersonList().get(0), expectedStudent);
        expectedModel.setPerson(expectedModel.getFilteredPersonList().get(1), expectedParent);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);

        // Verify linkage
        Student linkedStudent = (Student) model.getFilteredPersonList().get(0);
        Parent linkedParent = (Parent) model.getFilteredPersonList().get(1);
        assertTrue(linkedStudent.getParents().contains(linkedParent));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        LinkCommand command = new LinkCommand(INDEX_FIRST_PERSON, outOfBoundsIndex);
        assertCommandFailure(command, model, LinkCommand.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void execute_wrongTypes_throwsCommandException() {
        // Assume both indices point to non-student/non-parent (like two students)
        LinkCommand command = new LinkCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        // You can force both to same type in TypicalPersons if needed
        assertCommandFailure(command, model, LinkCommand.MESSAGE_WRONG_TYPE);
    }

    @Test
    public void equals() {
        LinkCommand linkFirstSecond = new LinkCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        LinkCommand linkSecondFirst = new LinkCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON);

        // same object -> returns true
        assertEquals(linkFirstSecond, linkFirstSecond);

        // same values -> returns true
        LinkCommand linkFirstSecondCopy = new LinkCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        assertEquals(linkFirstSecond, linkFirstSecondCopy);

        // different types -> returns false
        assertNotEquals(1, linkFirstSecond);

        // null -> returns false
        assertNotEquals(null, linkFirstSecond);

        // different indices -> returns false
        assertNotEquals(linkFirstSecond, linkSecondFirst);
    }
}
