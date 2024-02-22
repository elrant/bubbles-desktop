package team.elrant.bubbles.gui;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import team.elrant.bubbles.xmpp.ConnectedUser;

public class HelloController {
    @FXML
    private TextField username_field;
    @FXML
    private PasswordField password_field;

    @FXML
    protected void onSubmitButtonClick() {
        ConnectedUser user = new ConnectedUser(username_field.getText(), password_field.getText(), "elrant.team");
        user.initializeConnection();
        user.disconnect();
        System.exit(0);
    }
}