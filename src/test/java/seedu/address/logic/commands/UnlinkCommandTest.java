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
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Parent;
import seedu.address.testutil.PersonBuilder;

public class UnlinkCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        UnlinkCommand unlinkCommand = new UnlinkCommand(outOfBoundIndex, INDEX_SECOND_PERSON);

        assertCommandFailure(unlinkCommand, model, UnlinkCommand.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void execute_nonStudentOrParent_failure() {
        // Replace first person with a parent (wrong type)
        Parent wrongParent = (Parent) new PersonBuilder().withType("p").build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), wrongParent);

        UnlinkCommand unlinkCommand = new UnlinkCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        assertCommandFailure(unlinkCommand, model, UnlinkCommand.MESSAGE_WRONG_TYPE);
    }

    @Test
    public void equals() {
        final UnlinkCommand standardCommand = new UnlinkCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        // same values -> returns true
        UnlinkCommand commandWithSameValues = new UnlinkCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new UnlinkCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON)));
    }
}
