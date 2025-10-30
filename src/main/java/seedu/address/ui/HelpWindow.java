package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {
    public static final String HELP_MESSAGE = "▶ Add\n"
                    + "add type/(s/p) n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [note/NOTE] [pay/COST] "
                    + "[schedule/SCHEDULE] [t/TAG]\n\n"
                    + "▶ Clear\n"
                    + "clear\n\n"
                    + "▶ Delete\n"
                    + "delete INDEX\n\n"
                    + "▶ Edit\n"
                    + "edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [note/NOTE] [pay/COST] "
                    + "[schedule/SCHEDULE] [t/TAG]\n\n"
                    + "▶ Exit\n"
                    + "exit\n\n"
                    + "▶ Find\n"
                    + "find KEYWORD [MORE_KEYWORDS]\n\n"
                    + "▶ Help\n"
                    + "help\n\n"
                    + "▶ Link\n"
                    + "link student/INDEX parent/INDEX\n\n"
                    + "▶ List\n"
                    + "list\n\n"
                    + "▶ List Paid/Unpaid\n"
                    + "list paid\n"
                    + "list unpaid\n\n"
                    + "▶ List Schedule\n"
                    + "list schedule\n"
                    + "list DAY\n"
                    + "list DATE\n\n"
                    + "▶ Note\n"
                    + "note INDEX note/NOTE\n\n"
                    + "▶ Paid\n"
                    + "paid INDEX\n"
                    + "paid n/NAME\n\n"
                    + "▶ Reset All\n"
                    + "reset all\n\n"
                    + "▶ Schedule\n"
                    + "schedule INDEX schedule/DAY TIME-TIME\n"
                    + "schedule INDEX schedule/MM-dd-yyyy TIME-TIME\n\n"
                    + "▶ Unlink\n"
                    + "unlink student/INDEX parent/INDEX\n\n"
                    + "-----------------------------------------------------------\n"
                    + "For detailed information, refer to the User Guide below:";

    public static final String USERGUIDE_URL =
            "https://ay2526s1-cs2103t-w10-1.github.io/tp/UserGuide.html";
    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");

        Stage stage = getRoot();
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.setMaximized(false);
        stage.setMinWidth(600);
        stage.setMinHeight(450);
        stage.setWidth(720);
        stage.setHeight(560);

        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }
}
