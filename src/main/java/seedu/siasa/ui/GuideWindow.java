package seedu.siasa.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seedu.siasa.commons.core.LogsCenter;

public class GuideWindow extends UiPart<Stage> {

    /**
     * Enum class storing the strings for all the pages in the current guide.
     */
    private enum Page {
        ONE("User Guide",
            "Welcome to Siasa! "
                + "This is a guide that teaches you all the basic commands to use this application.\n"
                + "For a more detailed guide, visit: https://ay2122s1-cs2103-f10-4.github.io/tp/UserGuide.html",
            new Image("file:src/main/resources/images/guide/page_one.png")),
        TWO("User Guide - Persons", "addclient: Adds a contact to your Siasa address book.\n"
            + "Format: addclient n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]\n"
            + "Example: addclient n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01",
            new Image("file:src/main/resources/images/guide/page_two.png")),
        THREE("User Guide - Persons",
            "deleteclient: Removes a contact from your Siasa address book based on the index provided.\n"
                + "Format: deleteclient INDEX\n"
                + "Example: deleteclient 2",
            new Image("file:src/main/resources/images/guide/page_three.png")),
        FOUR("User Guide - Persons",
            "editclient: Edit a contact from your Siasa address book based on the index provided. "
                + "At least one of the optional fields should be filled.\n"
                + "Format: edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…\n"
                + "Example: edit 2 p/91234567 e/johndoe@example.com",
            new Image("file:src/main/resources/images/guide/page_four.png")),
        FIVE("User Guide - Policies",
            "addpolicy: Adds a policy that is linked to a contact to your Siasa address book.\n"
                + "addpolicy n/NAME_OF_POLICY p/PMT_AMOUNT [PMT_FREQ] [NUM_OF_PMT] c/COMMISSION_% [NUM_OF_PMT] cl/PERSON_INDEX [t/TAGS] [e/COVERAGE_EXPIRY_DATE]\n"
                + "Example: addpolicy n/Life Policy e/2021-12-13 p/1000 12 50 c/20 10 cl/1 ",
            new Image("file:src/main/resources/images/guide/page_five.png")),
        SIX("User Guide - Policies",
            "deletepolicy: Removes a policy from your Siasa address book based on the index provided.\n"
                + "deletepolicy INDEX\n"
                + "Example: deletepolicy 2",
            new Image("file:src/main/resources/images/guide/page_six.png")),
        SEVEN("User Guide - Policies",
                "editpolicy: Edits a policy from your Siasa address book based on the index provided. "
                + "At least one of the optional fields should be filled.\n"
                + "Format: editpolicy INDEX [n/NAME_OF_POLICY] [p/PMT_AMOUNT [PMT_FREQ] [NUM_OF_PMT]] "
                + "[c/COMMISSION_% [NUM_OF_PMT]] [cl/PERSON_INDEX] [t/TAGS] [e/COVERAGE_EXPIRY_DATE]\n"
                + "Example: editpolicy 2 n/Life Policy 2 c/20 20 e/2022-10-22",
            new Image("file:src/main/resources/images/guide/page_seven.png")),
        EIGHT("User Guide - Policies",
            "clientpolicy: List the policies belonging to the client based on the index provided.\n"
                + "clientpolicy INDEX\n"
                + "Example: clientpolicy 1",
            new Image("file:src/main/resources/images/guide/page_eight.png")),
        NINE("User Guide - Policies",
            "listpolicy: List all policies in your Siasa address book.\n"
                + "listpolicy\n"
                + "Example: listpolicy",
            new Image("file:src/main/resources/images/guide/page_nine.png"));

        private final String title;
        private final String description;
        private final Image screenshot;

        Page(String title, String description, Image screenshot) {
            this.title = title;
            this.description = description;
            this.screenshot = screenshot;
        }
    }

    private static final Logger logger = LogsCenter.getLogger(GuideWindow.class);
    private static final String FXML = "GuideWindow.fxml";
    private Page currentPage;


    @FXML
    private Button nextButton;

    @FXML
    private Button backButton;

    @FXML
    private Text textBox;

    @FXML
    private ImageView screenshot;

    @FXML
    private Label title;

    /**
     * Creates a new Guide Window.
     *
     * @param root Stage to use as the root of the GuideWindow.
     */
    public GuideWindow(Stage root) {
        super(FXML, root);
    }

    /**
     * Creates a new GuideWindow.
     */
    public GuideWindow() {
        this(new Stage());
        currentPage = Page.ONE;
        showPage();
    }

    /**
     * Shows the GuideWindow.
     */
    public void show() {
        logger.fine("Showing walkthrough page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the guide window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the guide window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the guide window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Replaces the current texts and image with the values stored in the currentPage enum.
     */
    private void showPage() {
        title.setText(currentPage.title);
        textBox.setText(currentPage.description);
        screenshot.setImage(currentPage.screenshot);
    }

    /**
     * Show the next page of the guide.
     */
    @FXML
    private void handleNext() {
        switch (currentPage) {
        case ONE:
            currentPage = Page.TWO;
            showPage();
            return;
        case TWO:
            currentPage = Page.THREE;
            showPage();
            return;
        case THREE:
            currentPage = Page.FOUR;
            showPage();
            return;
        case FOUR:
            currentPage = Page.FIVE;
            showPage();
            return;
        case FIVE:
            currentPage = Page.SIX;
            showPage();
            return;
        case SIX:
            currentPage = Page.SEVEN;
            showPage();
            return;
        case SEVEN:
            currentPage = Page.EIGHT;
            showPage();
            return;
        default:
        }

    }

    /**
     * Show the previous page of the guide.
     */
    @FXML
    private void handleBack() {
        switch (currentPage) {
        case ONE:
            return;
        case TWO:
            currentPage = Page.ONE;
            showPage();
            return;
        case THREE:
            currentPage = Page.TWO;
            showPage();
            return;
        case FOUR:
            currentPage = Page.THREE;
            showPage();
            return;
        case FIVE:
            currentPage = Page.FOUR;
            showPage();
            return;
        case SIX:
            currentPage = Page.FIVE;
            showPage();
            return;
        case SEVEN:
            currentPage = Page.SIX;
            showPage();
            return;
        case EIGHT:
            currentPage = Page.SEVEN;
            showPage();
            return;
        default:
        }

    }
}
