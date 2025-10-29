package seedu.address.logic;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class MessagesTest {

    @Test
    public void format_personWithTags_includesTagsSection() {
        Person taggedPerson = new PersonBuilder()
                .withName("Alice Pauline")
                .withPhone("94351253")
                .withEmail("alice@example.com")
                .withAddress("123, Jurong West Ave 6, #08-111")
                .withType("s")
                .withNote("brings own materials")
                .withCost("40")
                .withTags("owesFees", "priority")
                .build();

        String formatted = Messages.format(taggedPerson);

        org.junit.jupiter.api.Assertions.assertTrue(
                formatted.contains("Alice Pauline"),
                "Name should be present");

        org.junit.jupiter.api.Assertions.assertTrue(
                formatted.contains("; Tags: "),
                "Formatted output should include '; Tags: ' prefix when tags exist");

        org.junit.jupiter.api.Assertions.assertTrue(
                formatted.contains("owesFees"),
                "First tag should appear in formatted output");

        org.junit.jupiter.api.Assertions.assertTrue(
                formatted.contains("priority"),
                "Second tag should appear in formatted output");
    }
}
