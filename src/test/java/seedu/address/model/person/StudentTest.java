package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class StudentTest {

    private Student makeStudent(String name) {
        return new Student(
                new Name(name),
                new Phone("91234567"),
                new Email("alice@example.com"),
                new Address("123, Clementi Ave 3, #12-34"),
                new Note(""),
                new HashSet<>() // no tags
        );
    }

    @Test
    public void constructor_setsTypeToStudent() {
        Student s = makeStudent("Alice");
        assertEquals("s", s.getType().toString(), "Student must carry type 's'");
    }

    @Test
    public void equals_sameValues_true() {
        Student s1 = makeStudent("Alice");
        Student s2 = makeStudent("Alice");
        assertEquals(s1, s2);
    }

    @Test
    public void equals_differentName_false() {
        Student s1 = makeStudent("Alice");
        Student s2 = makeStudent("Bob");
        assertNotEquals(s1, s2);
    }
}
