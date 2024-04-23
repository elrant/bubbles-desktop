package team.elrant.bubbles.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import team.elrant.bubbles.xmpp.ConnectedUser;
import team.elrant.bubbles.xmpp.User;

import java.util.Set;

/**
 * The SideViewController class controls the user list functionality in the GUI.
 * It handles displaying the list of online users and opening chat windows.
 */
public class SideViewController {
    private static final Logger logger = LogManager.getLogger(SideViewController.class);
    @FXML
    private VBox userList;
    private ConnectedUser connectedUser;

    /**
     * Sets the connected user for the side view controller.
     *
     * @param connectedUser The connected user to set.
     */
    public void setConnectedUser(ConnectedUser connectedUser) {
        this.connectedUser = connectedUser;
    }

    /**
     * Initializes the user list view.
     * Populates the list with online users and adds event handlers to open chat windows.
     */
    @FXML
    public void initialize() {
        Roster roster = ConnectedUser.getRoster();
        Set<RosterEntry> rosterEntries = roster.getEntries();
        for (RosterEntry entry : rosterEntries) {
            String jid = entry.getJid().toString();
            String[] parts = jid.split("@");
            String username = parts[0];
            String serviceName = parts[1];

            // Create a User object
            User user = new User(username, serviceName);

            // Create UI components
            HBox userItem = new HBox();
            Label userName = new Label(user.getUsername());

            // Add event handler to open ChatPage
            userName.setOnMouseClicked(event -> openChatPage(jid));

            userItem.getChildren().addAll(userName);
            userList.getChildren().add(userItem);
        }
    }

    /**
     * Opens a chat window for the selected user.
     *
     * @param jid The JID of the user to open a chat window for.
     */
    private void openChatPage(String jid) {
        try {
            ChatViewApplication chatViewApplication = new ChatViewApplication(connectedUser, jid);
            chatViewApplication.start(new Stage());
        } catch (Exception e) {
            logger.error("Error opening chat window: {}", e.getMessage());
        }
    }
}
