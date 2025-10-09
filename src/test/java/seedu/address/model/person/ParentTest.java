package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class ParentTest {

    private Parent makeParent(String name) {
        return new Parent(
                new Name(name),
                new Phone("98765432"),
                new Email("bob.parent@example.com"),
                new Address("45, Jurong West St 91, #04-56"),
                new Note(""),
                new HashSet<>()
        );
    }

    @Test
    public void constructor_setsTypeToParent() {
        Parent p = makeParent("Bob");
        assertEquals("p", p.getType().toString(), "Parent must carry type 'p'");
    }

    @Test
    public void equals_sameValues_true() {
        Parent p1 = makeParent("Bob");
        Parent p2 = makeParent("Bob");
        assertEquals(p1, p2);
    }

    @Test
    public void equals_differentName_false() {
        Parent p1 = makeParent("Bob");
        Parent p2 = makeParent("Charlie");
        assertNotEquals(p1, p2);
    }
}
