package team.elrant.bubbles.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import team.elrant.bubbles.xmpp.ConnectedUser;

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

    @FXML
    protected void onSubmitButtonClick() {
        ConnectedUser user = new ConnectedUser(username_field.getText(), password_field.getText(), "elrant.team");
        try {
            user.initializeConnection();
            failedLoginLabel.setVisible(false);
            successfulLoginLabel.setVisible(true);
        } catch (Exception e) {
            // e.printStackTrace(); // Only for debugging purposes
            failedLoginLabel.setVisible(true);
        }

        // Placeholder
        if (user.isLoggedIn()) {
            // Close the login window and proceed to the main application
            submitButton.getScene().getWindow().hide();
        } else {
            System.out.println("Login failed. Check username and password.");
        }

        user.disconnect(); // Disconnect regardless of login result
    }

    @FXML
    public void initialize() {
        // Styling goes here
    }
}
