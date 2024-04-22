package team.elrant.bubbles.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import team.elrant.bubbles.xmpp.ConnectedUser;
import team.elrant.bubbles.xmpp.User;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The SideViewController class controls the login functionality in the GUI.
 * It handles ...
 */
public class SideViewController {
    private static final Logger logger = LogManager.getLogger(SideViewController.class);
    @FXML
    private VBox userList;
    private ConnectedUser connectedUser;

    // Add setter for connectedUser
    public void setConnectedUser(ConnectedUser connectedUser) {
        this.connectedUser = connectedUser;
    }

    @FXML
    public void initialize() {
        Roster roster = ConnectedUser.getRoster();
        Set <RosterEntry> rosterEntries = roster.getEntries();
        for (RosterEntry entry : rosterEntries) {
            String jid = entry.getJid().toString();
            String[] parts = jid.split("@");
            String username = parts[0];
            String serviceName = parts[1];

            // Create a User object
            User user = new User(username, serviceName);

            // Create UI components
            HBox userItem = new HBox();
            // ImageView profilePic = new ImageView(new Image(user.getProfilePictureUrl()));
            // profilePic.setFitHeight(50);
            // profilePic.setFitWidth(50);
            Label userName = new Label(user.getUsername());

            // Add event handler to open ChatPage
            userName.setOnMouseClicked(event -> openChatPage(username));

            userItem.getChildren().addAll(userName);
            userList.getChildren().add(userItem);
        }
    }

    // Method to open ChatPage
    private void openChatPage(String username) {
        try {
            ChatViewApplication chatViewApplication = new ChatViewApplication(connectedUser, username);
            chatViewApplication.start(new Stage());
        } catch (Exception e) {
            logger.error("Error opening chat window: {}", e.getMessage());
        }
    }
}
