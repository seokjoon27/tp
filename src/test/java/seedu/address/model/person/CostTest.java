package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class CostTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Cost(null));
    }

    @Test
    public void constructor_invalidCost_throwsIllegalArgumentException() {
        String invalidCost = "abc";
        assertThrows(IllegalArgumentException.class, () -> new Cost(invalidCost));
    }

    @Test
    public void isValidCost() {
        // null cost
        assertThrows(NullPointerException.class, () -> Cost.isValidCost(null));

        // invalid cost
        assertFalse(Cost.isValidCost("")); // empty string
        assertFalse(Cost.isValidCost(" ")); // spaces only
        assertFalse(Cost.isValidCost("abc")); // alphabets only
        assertFalse(Cost.isValidCost("12.")); // missing fractional digits
        assertFalse(Cost.isValidCost("12.3.4")); // multiple decimal points

        // valid cost
        assertTrue(Cost.isValidCost("0"));
        assertTrue(Cost.isValidCost("10"));
        assertTrue(Cost.isValidCost("72.5"));
    }

    @Test
    public void toString_returnsPrefixedValue() {
        assertEquals("$72.5", new Cost("72.5").toString());
    }

    @Test
    public void equals() {
        Cost cost = new Cost("10");

        // same values -> returns true
        assertTrue(cost.equals(new Cost("10")));

        // same object -> returns true
        assertTrue(cost.equals(cost));

        // null -> returns false
        assertFalse(cost.equals(null));

        // different types -> returns false
        assertFalse(cost.equals(5));

        // different values -> returns false
        assertFalse(cost.equals(new Cost("15")));
    }
}
