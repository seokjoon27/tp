package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code UnlinkCommand}.
 */
public class UnlinkCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // For testing, ensure ALICE and BENSON exist and are linked
        Parent parent = new Parent(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getNote(), ALICE.getCost(), ALICE.getPaymentStatus(),
                ALICE.getTags());
        Student student = new Student(BENSON.getName(), BENSON.getPhone(), BENSON.getEmail(),
                BENSON.getAddress(), BENSON.getNote(), BENSON.getCost(), BENSON.getPaymentStatus(),
                BENSON.getTags());

        parent.addChild(student);
        student.addParent(parent);

        model.setPerson(ALICE, parent);
        model.setPerson(BENSON, student);
    }

    @Test
    public void execute_validIndices_success() {
        Index studentIndex = Index.fromOneBased(model.getFilteredPersonList().indexOf(BENSON) + 1);
        Index parentIndex = Index.fromOneBased(model.getFilteredPersonList().indexOf(ALICE) + 1);

        UnlinkCommand unlinkCommand = new UnlinkCommand(studentIndex, parentIndex);

        // Expected model is a clone of the current model before unlinking
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        Parent expectedParent = (Parent) expectedModel.getFilteredPersonList()
                .get(parentIndex.getZeroBased());
        Student expectedStudent = (Student) expectedModel.getFilteredPersonList()
                .get(studentIndex.getZeroBased());

        expectedParent.removeChild(expectedStudent);
        expectedStudent.removeParent(expectedParent);
        expectedModel.setPerson(ALICE, expectedParent);
        expectedModel.setPerson(BENSON, expectedStudent);

        String expectedMessage = String.format(UnlinkCommand.MESSAGE_UNLINK_SUCCESS,
                expectedStudent.getName(), expectedParent.getName());

        assertCommandSuccess(unlinkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index invalidParentIndex = Index.fromOneBased(999);
        Index invalidStudentIndex = Index.fromOneBased(999);

        UnlinkCommand unlinkCommand = new UnlinkCommand(invalidStudentIndex, invalidParentIndex);
        assertCommandFailure(unlinkCommand, model, UnlinkCommand.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void execute_wrongType_throwsCommandException() {
        // Both ALICE and BENSON are now students, for example
        Person aliceAsStudent = new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getNote(), ALICE.getCost(), ALICE.getPaymentStatus(),
                ALICE.getTags());
        model.setPerson(ALICE, aliceAsStudent);

        Index studentIndex = Index.fromOneBased(model.getFilteredPersonList().indexOf(BENSON) + 1);
        Index parentIndex = Index.fromOneBased(model.getFilteredPersonList().indexOf(ALICE) + 1);

        UnlinkCommand unlinkCommand = new UnlinkCommand(studentIndex, parentIndex);

        assertCommandFailure(unlinkCommand, model, UnlinkCommand.MESSAGE_WRONG_TYPE);
    }

    @Test
    public void equals() {
        UnlinkCommand firstCommand = new UnlinkCommand(Index.fromOneBased(1), Index.fromOneBased(2));
        UnlinkCommand secondCommand = new UnlinkCommand(Index.fromOneBased(2), Index.fromOneBased(1));

        // same object -> returns true
        assertEquals(firstCommand, firstCommand);

        // same values -> returns true
        UnlinkCommand firstCommandCopy = new UnlinkCommand(Index.fromOneBased(1), Index.fromOneBased(2));
        assertEquals(firstCommand, firstCommandCopy);

        // different values -> returns false
        assertNotEquals(firstCommand, secondCommand);
    }
}
