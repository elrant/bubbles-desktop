package team.elrant.bubbles.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private Label failedLoginLabel;
    @FXML
    private Label successfulLoginLabel;
    private @Nullable ConnectedUser connectedUser;


    /**
     * Handles the action when submit button is clicked.
     * It attempts to log in the user using the provided credentials and navigates to the main application upon successful login.
     */
    @FXML
    protected void onSubmitButtonClick() {
        try {
            connectedUser = new ConnectedUser(username_field.getText(), password_field.getText(), "bubbles.elrant.team");
            connectedUser.initializeConnection();
            connectedUser.saveUserToFile("user.dat");
        } catch (Exception e) {
            logger.error("Error during login: {}", e.getMessage());
            failedLoginLabel.setVisible(true);
        }
        if (connectedUser != null && connectedUser.isLoggedIn()) {
            successfulLoginLabel.setVisible(true);
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
            if (!userFromFile.getUsername().isEmpty()) {
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
            if (connectedUser != null && connectedUser.isLoggedIn()) {
                ChatViewApplication chatViewApplication = new ChatViewApplication(connectedUser, "lucadg@bubbles.elrant.team");
                chatViewApplication.start(new Stage());
                closeLoginWindow();
            }
        } catch (Exception e) {
            logger.error("Error opening chat window: {}", e.getMessage());
        }
    }
}
