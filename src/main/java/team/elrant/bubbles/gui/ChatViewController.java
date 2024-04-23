package team.elrant.bubbles.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jxmpp.jid.EntityBareJid;
import team.elrant.bubbles.xmpp.ConnectedUser;

import java.io.*;

/**
 * The ChatViewController class controls the chat view functionality in the GUI.
 * It handles sending and displaying messages in the chat interface.
 */
public class ChatViewController {
    private static final Logger logger = LogManager.getLogger(ChatViewController.class);
    private final @NotNull ConnectedUser connectedUser;
    private final @NotNull EntityBareJid bareContactJid;

    @FXML
    private TextArea chatTextArea;

    @FXML
    private TextArea messageTextArea;

    @FXML
    private Button sendButton;

    @FXML
    private Label failedSendMessageLabel;

    /**
     * Constructs a new instance of ChatViewController with the specified connected user and contact JID.
     *
     * @param connectedUser  The connected user instance.
     * @param bareContactJid The JID of the contact with whom the user is communicating.
     */
    public ChatViewController(@NotNull ConnectedUser connectedUser, @NotNull EntityBareJid bareContactJid) {
        this.connectedUser = connectedUser;
        this.bareContactJid = bareContactJid;
    }

    /**
     * Initializes the chat view controller.
     * It adds an incoming message listener to the connected user and sets up the action event for the "send" button.
     */
    @FXML
    protected void initialize() {
        if (verifyFile() != 0) {
            readFromFile();
        }
        connectedUser.addIncomingMessageListener(bareContactJid, this::updateChatDisplay);
        messageTextArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                sendMessage();
        });
        sendButton.setOnKeyPressed(event -> sendMessage());
        chatTextArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                sendMessage();
        });
        messageTextArea.setWrapText(true);
        chatTextArea.setEditable(false);
        chatTextArea.setWrapText(true);
    }

    /**
     * Verifies if the message log file exists and returns its size.
     *
     * @return The size of the message log file, or 0 if the file does not exist.
     */
    protected int verifyFile() {
        File file = new File("messageLogs" + bareContactJid + ".txt");
        return (int) file.length();
    }

    /**
     * Saves the current chat messages to a file.
     */
    protected void saveToFile() {
        try (PrintWriter file = new PrintWriter(new FileWriter("messageLogs" + bareContactJid + ".txt", false))) {
            String[] lines = chatTextArea.getText().split("\\n");
            for (String line : lines) {
                file.println(line);
            }
        } catch (IOException e) {
            logger.error("Error saving messages: {}", e.getMessage());
        }
    }

    /**
     * Reads chat messages from a file and appends them to the chat area.
     */
    protected void readFromFile() {
        try (BufferedReader file = new BufferedReader(new FileReader("messageLogs" + bareContactJid + ".txt"))) {
            String line;
            while ((line = file.readLine()) != null) {
                chatTextArea.appendText(line + "\n");
            }
        } catch (IOException e) {
            logger.error("Error reading from file: {}", e.getMessage());
        }
    }

    /**
     * Updates the chat display with the incoming message.
     *
     * @param message The incoming message to display.
     */
    private void updateChatDisplay(@NotNull String message) {
        chatTextArea.appendText("\n" + bareContactJid + ": " + message + "\n");
    }

    /**
     * Sends a message to the contact and updates the chat display.
     */
    @FXML
    protected void sendMessage() {
        try {
            String message = messageTextArea.getText().trim();
            if (!message.isEmpty()) {
                connectedUser.sendMessage(bareContactJid, message);
                chatTextArea.appendText("Me: " + message + "\n");
                messageTextArea.clear();
            } else {
                logger.warn("Empty message not sent.");
            }
        } catch (Exception e) {
            failedSendMessageLabel.setVisible(true);
            logger.error("Error sending message: {}", e.getMessage());
        }
    }
}
