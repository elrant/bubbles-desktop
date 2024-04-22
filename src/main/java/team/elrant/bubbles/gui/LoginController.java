package team.elrant.bubbles.gui;

import javafx.fxml.FXML;
import javafx.scene.AccessibleRole;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
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
    private PasswordField password_field_hidden;
    @FXML
    private TextField password_field_visible;
    @FXML
    private Button submitButton;
    @FXML
    private Label failedLoginLabel;
    @FXML
    private Label successfulLoginLabel;
    @FXML
    private CheckBox rememberPassword;
    @FXML
    private CheckBox seePasswordCheckbox;
    private @Nullable ConnectedUser connectedUser;

    final String contact = "testuser";


    /**
     * Handles the action when submit button is clicked.
     * It attempts to log in the user using the provided credentials and navigates to the main application upon successful login.
     */
    @FXML
    protected void onSubmitButtonClick() {
        try {
            if (seePasswordCheckbox.isSelected())
                connectedUser = new ConnectedUser(username_field.getText(), password_field_visible.getText(), "bubbles.elrant.team");
            else
                connectedUser = new ConnectedUser(username_field.getText(), password_field_hidden.getText(), "bubbles.elrant.team");
            connectedUser.initializeConnection();
            connectedUser.saveUserToFile("user.dat", rememberPassword.isSelected());
        } catch (Exception e) {
            logger.error("Error during login: {}", e.getMessage());
            failedLoginLabel.setVisible(true);
        }
        if (connectedUser != null && connectedUser.isLoggedIn()) {
            successfulLoginLabel.setVisible(true);
            openChatWindow();
        }
    }

    @FXML
    protected void onSeePasswordCheckBoxClick (){
        if (seePasswordCheckbox.isSelected()) {
            password_field_hidden.setVisible(false);
            password_field_visible.setText(password_field_hidden.getText());
            password_field_visible.setVisible(true);
        } else {
            password_field_visible.setVisible(false);
            password_field_hidden.setText(password_field_visible.getText());
            password_field_hidden.setVisible(true);
        }
    }

    /**
     * Initializes the login form.
     * It loads the username from a file and populates the username field, if available.
     */
    @FXML
    public void initialize() {
        try {
            ConnectedUser userFromFile = new ConnectedUser("user.dat");
            if (!userFromFile.getUsername().equals("uninit")) {
                username_field.setText(userFromFile.getUsername());
            }
            if (!userFromFile.passwordUnInit()){
                userFromFile.setPasswordField(password_field_hidden);
                rememberPassword.setSelected(true);
            }
        } catch (Exception ignored) {
            logger.warn("Failed to load user information from file.");
        }
        password_field_hidden.setOnKeyPressed(event ->{
            if(event.getCode() == KeyCode.ENTER) //when Enter is pressed, call onSubmitButton
                onSubmitButtonClick();
        });
        password_field_visible.setOnKeyPressed(event ->{
            if(event.getCode() == KeyCode.ENTER) //when Enter is pressed, call onSubmitButton
                onSubmitButtonClick();
        });
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
                ChatViewApplication chatViewApplication = new ChatViewApplication(connectedUser, contact+"@bubbles.elrant.team");
                chatViewApplication.start(new Stage());
                closeLoginWindow();
            }
        } catch (Exception e) {
            logger.error("Error opening chat window: {}", e.getMessage());
        }
    }
}
