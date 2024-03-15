package team.elrant.bubbles.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import team.elrant.bubbles.xmpp.ConnectedUser;
import team.elrant.bubbles.xmpp.User;

/**
 * The LoginController class controls the login functionality in the GUI.
 * It handles user authentication and navigation to the main application.
 */
public class LoginController {
    @FXML
    private TextField username_field;
    @FXML
    private PasswordField password_field;
    @FXML
    private Button submitButton; // Reference the button
    @FXML
    private AnchorPane formPane; // Reference the form pane
    @FXML
    private Label loginLabel; // Reference the login label
    @FXML
    private Label failedLoginLabel; // Reference the "Wrong username or password" text
    @FXML
    private Label successfulLoginLabel; // Reference the "Successful login" text
    private ConnectedUser connectedUser = null;

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
            // e.printStackTrace(); // Only for debugging purposes
            failedLoginLabel.setVisible(true);
        }

        if (connectedUser != null && connectedUser.isLoggedIn()) {
            // Close the login window and proceed to the main application
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close(); // Close the login window

            // Open the chat window
            try {
                ChatViewApplication chatViewApplication = new ChatViewApplication(connectedUser, "lucadg@elrant.team");
                chatViewApplication.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace(); // Replace with more robust error handling in the future
            }
        }
    }

    /**
     * Initializes the login form.
     * It loads the username from a file and populates the username field, if available.
     */
    @FXML
    public void initialize() {
        // Styling goes here
        try {
            User userFromFile = new User("user.dat");
            if( (userFromFile.getUsername() != null) && !(userFromFile.getUsername().isEmpty()) ) {
                username_field.setText(userFromFile.getUsername());
            }
        } catch (Exception ignored) {
            // Do nothing
        }
    }
}
