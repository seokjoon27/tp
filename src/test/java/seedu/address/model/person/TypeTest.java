package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TypeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Type(null));
    }

    @Test
    public void constructor_invalid_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Type(""));
        assertThrows(IllegalArgumentException.class, () -> new Type("x"));
        assertThrows(IllegalArgumentException.class, () -> new Type("student")); // only s/p allowed
    }

    @Test
    public void validValues_normalizedToLowercase() {
        assertEquals("s", new Type("S").toString());
        assertEquals("p", new Type("P").toString());
    }

    @Test
    public void equals_hashcode() {
        Type s1 = new Type("s");
        Type s2 = new Type("S");
        Type p = new Type("p");

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotEquals(s1, p);
    }
}
