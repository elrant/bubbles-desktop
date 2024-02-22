package team.elrant.bubbles.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import team.elrant.bubbles.xmpp.ConnectedUser;

public class LoginController {
    @FXML
    private TextField username_field;
    @FXML
    private PasswordField password_field;
    @FXML
    private Button submitButton; // Reference the button

    @FXML
    protected void onSubmitButtonClick() {
        ConnectedUser user = new ConnectedUser(username_field.getText(), password_field.getText(), "elrant.team");
        user.initializeConnection();

        // Placeholder: Successful Login Handling
        if (user.isLoggedIn()) {
            // Close the login window and proceed to the main application
            submitButton.getScene().getWindow().hide();
            // ... (Load the main application UI)
        } else {
            // Display an error message (a simple option for now)
            System.out.println("Login failed. Check username and password.");
        }

        user.disconnect(); // Disconnect regardless of login result
    }

    @FXML
    public void initialize() { // Add an initialize method
        // Apply BootstrapFX styling
        username_field.getStyleClass().add("form-control");
        password_field.getStyleClass().add("form-control");
        submitButton.getStyleClass().addAll("btn", "btn-primary");
    }
}
