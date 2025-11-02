package seedu.address.ui;

import java.util.Comparator;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final double PAID_STATUS_WIDTH = 120.0;
    private static final double PAID_STATUS_SCALE = 1.0;
    private static final String PAID_CHECKBOX_STYLE =
            "-fx-background-color: transparent; -fx-font-size: 24px; -fx-mark-color: #ff8f00;";
    private static final String UNPAID_CHECKBOX_STYLE =
            "-fx-background-color: transparent; -fx-font-size: 24px; -fx-mark-color: #1f1f1f;";
    private static final String PAID_CAPTION_STYLE = "-fx-text-fill: black; -fx-font-weight: bold;";

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
    private Label paidCaption;
    @FXML
    private VBox parentsContainer;
    @FXML
    private FlowPane parents;
    @FXML
    private VBox childrenContainer;
    @FXML
    private FlowPane children;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        setRow(phone, "Phone", person.getPhone().value);
        setRow(address, "Address", person.getAddress().value);
        setRow(email, "Email", person.getEmail().value);
        setRow(note, "Note", person.getNote() == null ? null : person.getNote().value);
        setRow(cost, "Cost", person.getCost() == null ? null : person.getCost().toString());

        paidCaption.setStyle(PAID_CAPTION_STYLE);

        boolean isPaid = person.getPaymentStatus().isPaid();
        paidStatus.setMinWidth(PAID_STATUS_WIDTH);
        paidStatus.setPrefWidth(PAID_STATUS_WIDTH);
        paidStatus.setMaxWidth(PAID_STATUS_WIDTH);
        paidStatus.setScaleX(PAID_STATUS_SCALE);
        paidStatus.setScaleY(PAID_STATUS_SCALE);
        paidStatus.setSelected(isPaid);
        paidStatus.setText("");
        paidStatus.setStyle(isPaid ? PAID_CHECKBOX_STYLE : UNPAID_CHECKBOX_STYLE);

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

            String parentNames = student.getParents().stream()
                    .sorted(Comparator.comparing(parent -> parent.getName().fullName))
                    .map(parent -> parent.getName().fullName)
                    .collect(Collectors.joining(", "));
            if (parentNames.isEmpty() || parentNames.isBlank()) {
                parentsContainer.setManaged(false);
                parentsContainer.setVisible(false);
            }
            parents.getChildren().clear();
            parents.getChildren().add(new Label(parentNames));
            childrenContainer.setManaged(false);
            childrenContainer.setVisible(false);
            children.getChildren().clear();
        } else {
            assert person instanceof Parent : "Person is neither Student nor Parent";
            Parent parent = (Parent) person;
            setRow(schedule, null, null);

            parentsContainer.setManaged(false);
            parentsContainer.setVisible(false);
            parents.getChildren().clear();
            childrenContainer.setManaged(true);
            childrenContainer.setVisible(true);
            String childNames = parent.getChildren().stream()
                    .sorted(Comparator.comparing(child -> child.getName().fullName))
                    .map(child -> child.getName().fullName)
                    .collect(Collectors.joining(", "));
            if (childNames.isEmpty() || childNames.isBlank()) {
                childrenContainer.setManaged(false);
                childrenContainer.setVisible(false);
            }
            children.getChildren().clear();
            children.getChildren().add(new Label(childNames));
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
            return "  ";
        }
        return p.getTags().stream()
                .map(t -> t.tagName)
                .sorted(String::compareToIgnoreCase)
                .collect(java.util.stream.Collectors.joining(", "));

    }
}
