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
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import team.elrant.bubbles.xmpp.ConnectedUser;
import team.elrant.bubbles.xmpp.User;

import java.io.IOException;

/**
 * The LoginController class controls the login functionality in the GUI.
 * It handles user authentication and navigation to the main application.
 */
public class LoginController {
    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
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

    private ConnectedUser connectedUser = null;

    /**
     * Handles the action when the submit button is clicked.
     * It attempts to log in the user using the provided credentials and navigates to the main application upon successful login.
     */
    @FXML
    protected void onSubmitButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            connectedUser = new ConnectedUser(username, password, "elrant.team");
            connectedUser.initializeConnection();
            connectedUser.saveUserToFile("user.dat");
            successfulLoginLabel.setVisible(true);
        } catch (IOException | InterruptedException | SmackException | XMPPException e) {
            logger.error("Error during login: " + e.getMessage());
            failedLoginLabel.setVisible(true);
        }

        if (connectedUser != null && connectedUser.isLoggedIn()) {
            closeLoginWindow();
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
            String storedUsername = userFromFile.getUsername();
            if (storedUsername != null && !storedUsername.isEmpty()) {
                usernameField.setText(storedUsername);
            }
        } catch (Exception e) {
            logger.error("Error loading user information from file: " + e.getMessage());
        }
    }

    private void closeLoginWindow() {
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

    private void openChatWindow() {
        try {
            ChatViewApplication chatViewApplication = new ChatViewApplication(connectedUser, "lucadg@elrant.team");
            chatViewApplication.start(new Stage());
        } catch (Exception e) {
            logger.error("Error opening chat window: " + e.getMessage());
        }
    }
}
