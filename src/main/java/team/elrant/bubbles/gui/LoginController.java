package team.elrant.bubbles.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.elrant.bubbles.xmpp.ConnectedUser;
import team.elrant.bubbles.xmpp.User;

/**
 * The LoginController class controls the login functionality in the GUI.
 * It handles user authentication and navigation to the main application.
 */
public class LoginController {
    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @FXML
    private TextField username_field;
    @FXML
    private PasswordField password_field;
    @FXML
    private Button submitButton;
    @FXML
    private AnchorPane formPane;
    @FXML
    private Label loginLabel;
    @FXML
    private Label failedLoginLabel;
    @FXML
    private Label successfulLoginLabel;
    private @Nullable ConnectedUser connectedUser = null;

    /**
     * Handles the action when the submit button is clicked.
     * It attempts to log in the user using the provided credentials and navigates to the main application upon successful login.
     */
    @FXML
    protected void onSubmitButtonClick() {
        try {
            connectedUser = new ConnectedUser(username_field.getText(), password_field.getText(), "elrant.team");
            connectedUser.initializeConnection();
            connectedUser.saveUserToFile("user.dat");
        } catch (Exception e) {
            logger.error("Error during login: " + e.getMessage());
            failedLoginLabel.setVisible(true);
        }

        if (connectedUser != null && connectedUser.isLoggedIn()) {
            openChatWindow();
        }
    }

    /**
     * Initializes the login form.
     * It loads the username from a file and populates the username field, if available.
     */
    @FXML
    public void initialize() {
        try {
            User userFromFile = new User("user.dat");
            if (userFromFile.getUsername() != null && !userFromFile.getUsername().isEmpty()) {
                username_field.setText(userFromFile.getUsername());
            }
        } catch (Exception ignored) {
            logger.warn("Failed to load user information from file.");
        }
    }

    /**
     * Closes the login window.
     */
    private void closeLoginWindow() {
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Opens the chat window.
     */
    private void openChatWindow() {
        try {
            ChatViewApplication chatViewApplication = new ChatViewApplication(connectedUser, "lucadg@elrant.team");
            chatViewApplication.start(new Stage());
        } catch (Exception e) {
            logger.error("Error opening chat window: " + e.getMessage());
        }
    }
}
