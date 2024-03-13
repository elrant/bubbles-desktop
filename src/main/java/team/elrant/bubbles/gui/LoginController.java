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

    @FXML
    protected void onSubmitButtonClick() {
        try {
            connectedUser = new ConnectedUser(username_field.getText(), password_field.getText(), "elrant.team");
            connectedUser.initializeConnection();
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
                ChatViewApplication chatViewApplication = new ChatViewApplication(connectedUser, "dummy@elrant.team");
                chatViewApplication.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace(); // Replace with more robust error handling in the future
            }
        }
    }

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