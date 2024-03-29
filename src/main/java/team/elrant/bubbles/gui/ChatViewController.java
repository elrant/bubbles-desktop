package team.elrant.bubbles.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jxmpp.jid.BareJid;
import team.elrant.bubbles.xmpp.ConnectedUser;

/**
 * The ChatViewController class controls the chat view functionality in the GUI.
 * It handles sending and displaying messages in the chat interface.
 */
public class ChatViewController {
    private static final Logger logger = LogManager.getLogger(ChatViewController.class);

    @FXML
    private TextArea chatTextArea;
    @FXML
    private TextArea messageTextArea;
    @FXML
    private Button sendButton;
    @FXML
    private Label failedSendMessageLabel;

    private final @NotNull ConnectedUser connectedUser;
    private final @NotNull BareJid bareContactJid;

    /**
     * Constructs a new instance of ChatViewController with the specified connected user and contact JID.
     *
     * @param connectedUser The connected user instance.
     * @param bareContactJid    The JID of the contact with whom the user is communicating.
     */
    public ChatViewController(@NotNull ConnectedUser connectedUser, @NotNull BareJid bareContactJid) {
        this.connectedUser = connectedUser;
        this.bareContactJid = bareContactJid;
    }

    /**
     * Initializes the chat view controller.
     * It adds an incoming message listener to the connected user and sets up the action event for send button.
     */
    @FXML
    protected void initialize() {
        connectedUser.addIncomingMessageListener(bareContactJid, this::updateChatDisplay);
        sendButton.setOnAction(event -> sendMessage());
    }

    /**
     * Sends a message to the contact and updates the chat display.
    */
    @FXML
    protected void sendMessage() {
        try {
            String message = messageTextArea.getText().trim(); // Trim to remove leading/trailing whitespace
            if (!message.isEmpty()) { // Check if the message is not empty
                connectedUser.sendMessage(bareContactJid, message);
                chatTextArea.appendText("Me: " + message + "\n");
                messageTextArea.clear();
            } else {
                logger.warn("Empty message not sent.");
            }
        } catch (Exception e) {
            failedSendMessageLabel.setVisible(true);
            logger.error("Error sending message: " + e.getMessage());
        }
    }

    /**
     * Updates the chat display with the incoming message.
     *
     * @param message The incoming message to display.
     */
    private void updateChatDisplay(@NotNull String message) {
        Platform.runLater(() -> chatTextArea.appendText(bareContactJid + ": " + message + "\n"));
    }
}
