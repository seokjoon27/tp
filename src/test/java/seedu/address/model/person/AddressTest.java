package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Address(null));
    }

    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        String invalidAddress = "";
        assertThrows(IllegalArgumentException.class, () -> new Address(invalidAddress));
    }

    @Test
    public void isValidAddress() {
        // null address
        assertThrows(NullPointerException.class, () -> Address.isValidAddress(null));

        // invalid addresses
        assertFalse(Address.isValidAddress("")); // empty string
        assertFalse(Address.isValidAddress(" ")); // spaces only

        // valid addresses
        assertTrue(Address.isValidAddress("Blk 456, Den Road, #01-355"));
        assertTrue(Address.isValidAddress("-")); // one character
        assertTrue(Address.isValidAddress("Leng Inc; 1234 Market St; San Francisco CA 2349879; USA")); // long address
    }

    @Test
    public void equals() {
        Address address = new Address("Valid Address");

        // same values -> returns true
        assertTrue(address.equals(new Address("Valid Address")));

        // same object -> returns true
        assertTrue(address.equals(address));

        // null -> returns false
        assertFalse(address.equals(null));

        // different types -> returns false
        assertFalse(address.equals(5.0f));

        // different values -> returns false
        assertFalse(address.equals(new Address("Other Valid Address")));
    }

    /** Helper: repeat a single character n times. */
    private static String repeat(char c, int n) {
        return String.valueOf(c).repeat(Math.max(0, n));
    }

    @Test
    public void constructor_maxLength_success() {
        String twoHundred = repeat('A', 200);
        assertDoesNotThrow(() -> new Address(twoHundred));
        assertEquals(200, new Address(twoHundred).toString().length());
    }

    @Test
    public void constructor_exceedsMaxLength_throwsIllegalArgumentException() {
        String twoHundredOne = repeat('B', 201);
        assertThrows(IllegalArgumentException.class, () -> new Address(twoHundredOne));
    }

    @Test
    public void isValidAddress_lengthBoundaries() {
        String twoHundred = repeat('C', 200);
        String twoHundredOne = repeat('D', 201);

        assertTrue(Address.isValidAddress(twoHundred));     // exactly 200: valid
        assertFalse(Address.isValidAddress(twoHundredOne)); // 201: invalid
    }

    @Test
    public void isValidAddress_leadingWhitespace_invalid() {
        assertFalse(Address.isValidAddress(" Leading"));   // leading space
        assertFalse(Address.isValidAddress("\tTabbed"));   // leading tab
        assertFalse(Address.isValidAddress("\nNewline"));  // leading newline
    }

    @Test
    public void isValidAddress_internalAndTrailingWhitespace_valid() {
        assertTrue(Address.isValidAddress("Blk 123, Main Road #02-01")); // internal spaces
        assertTrue(Address.isValidAddress("A" + repeat(' ', 5)));        // trailing spaces ok
    }

    @Test
    public void isValidAddress_singleCharValid() {
        assertTrue(Address.isValidAddress("-"));
        assertTrue(Address.isValidAddress("A"));
        assertTrue(Address.isValidAddress("1"));
    }

    @Test
    public void isValidAddress_unicode_valid() {
        assertTrue(Address.isValidAddress("Δelta Street 5"));
        assertTrue(Address.isValidAddress("東京 新宿区 1-2-3"));
    }

    @Test
    public void equals_hashCode_longBoundary_consistent() {
        String s200 = repeat('Z', 200);
        Address a1 = new Address(s200);
        Address a2 = new Address(s200);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    public void toString_returnsRawValue() {
        Address addr = new Address("Blk 456, Den Road, #01-355");
        assertEquals("Blk 456, Den Road, #01-355", addr.toString());
    }
}
