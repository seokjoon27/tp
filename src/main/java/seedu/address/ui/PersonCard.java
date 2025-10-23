package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label type;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private Label tagsLine;
    @FXML
    private Label note;
    @FXML
    private Label schedule;
    @FXML
    private Label cost;
    @FXML
    private CheckBox paidStatus;
    @FXML
    private VBox parentsContainer;
    @FXML
    private FlowPane parents;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        type.setText(formatField("Type", person.getType().isStudent() ? "Student" : "Parent"));
        setRow(phone, "Phone", person.getPhone().value);
        setRow(address, "Address", person.getAddress().value);
        setRow(email, "Email", person.getEmail().value);
        setRow(note, "Note", person.getNote() == null ? null : person.getNote().value);
        setRow(cost, "Cost", person.getCost() == null ? null : person.getCost().toString());


        boolean isPaid = person.getPaymentStatus().isPaid();
        paidStatus.setSelected(isPaid);
        paidStatus.setText(isPaid ? "[Paid]" : "[Unpaid]");

        tags.getChildren().clear();

        String chipText = person.getType().isStudent() ? "Student" : "Parent";
        Label typeChip = new Label(chipText);
        typeChip.getStyleClass().addAll("chip", person.getType().isStudent() ? "chip-student" : "chip-parent");

        tags.getChildren().add(typeChip);
        setRow(tagsLine, "Tags", joinTags(person));
        parents.getChildren().clear();


        if (person instanceof Student student) {
            schedule.setText(student.getSchedule() != null
                    ? formatField("Schedule", student.getSchedule().value)
                    : formatField("Schedule", ""));
            parentsContainer.setManaged(true);
            parentsContainer.setVisible(true);

            student.getParents().stream()
                    .sorted(Comparator.comparing(parent -> parent.getName().fullName))
                    .forEach(parent -> parents.getChildren().add(new Label(parent.getName().fullName)));
        } else {
            setRow(schedule, null, null);

            parentsContainer.setManaged(false);
            parentsContainer.setVisible(false);
            parents.getChildren().clear();
        }
    }

    private String formatField(String label, String value) {
        return "[" + label + "] " + (value == null ? "" : value);
    }

    /** Show a label row only when value is present; otherwise hide & unmanage (no layout gap). */
    private void setRow(Label label, String field, String value) {
        boolean hasValue = value != null && !value.isBlank();
        label.setManaged(hasValue);
        label.setVisible(hasValue);
        if (hasValue) {
            label.setText(formatField(field, value));
        } else {
            label.setText("");
        }
    }

    /** Returns a comma-joined list of tags or null if empty (so the row is hidden). */
    private String joinTags(Person p) {
        if (p.getTags() == null || p.getTags().isEmpty()) {
            return "none";
        }
        return p.getTags().stream()
                .map(t -> t.tagName)
                .sorted(String::compareToIgnoreCase)
                .collect(java.util.stream.Collectors.joining(", "));

    }
}
