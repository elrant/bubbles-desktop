package team.elrant.bubbles.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jxmpp.jid.BareJid;
import team.elrant.bubbles.xmpp.ConnectedUser;

import java.io.*;
import java.security.Key;
import java.util.StringTokenizer;

/**
 * The ChatViewController class controls the chat view functionality in the GUI.
 * It handles sending and displaying messages in the chat interface.
 */
public class ChatViewController {
    private static final Logger logger = LogManager.getLogger(ChatViewController.class);
    private final @NotNull ConnectedUser connectedUser;
    private final @NotNull BareJid bareContactJid;
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
    public ChatViewController(@NotNull ConnectedUser connectedUser, @NotNull BareJid bareContactJid) {
        this.connectedUser = connectedUser;
        this.bareContactJid = bareContactJid;
    }

    /**
     * Initializes the chat view controller.
     * It adds an incoming message listener to the connected user and sets up the action event for the "send" button.
     */
    @FXML
    protected void initialize() {
        chatTextArea.setStyle("-fx-background-color: Black;");
        messageTextArea.setStyle("-fx-background-color: Black;");
        if(verifyFile() != 0){
            readFromFile();
        }
        connectedUser.addIncomingMessageListener(bareContactJid, this::updateChatDisplay);
        messageTextArea.setOnKeyPressed(event ->{
            if(event.getCode() == KeyCode.ENTER) //when Enter is pressed, call sendMessage
                sendMessage();
        });
        sendButton.setOnKeyPressed(event -> sendMessage()); //call sendMessage when button is pressed
        chatTextArea.setOnKeyPressed(event ->{
            if(event.getCode() == KeyCode.ENTER) //when Enter is pressed, call sendMessage
                sendMessage();
        });
        messageTextArea.setWrapText(true);
        chatTextArea.setEditable(false);
        chatTextArea.setWrapText(true);

    }

    protected int verifyFile(){
        File file = new File("saveMessage.txt");
        return (int)file.length();
    }

    protected void saveMessage(){
        try {
            FileWriter f = new FileWriter("saveMessage.txt",false);
            PrintWriter file = new PrintWriter(f);
            file.println(chatTextArea.getText() + ";");
            f.flush();
            f.close();
        }
        catch (IOException e){
            logger.error("Error To Save Message");
        }
    }
    protected void readFromFile(){ //Read from file and add to chatAreaText
        try {
            String line;
            FileReader f = new FileReader("saveMessage.txt");
            BufferedReader file = new BufferedReader(f);
            StringTokenizer tokenizer;
            String string = file.readLine();
            while (string != null){
                tokenizer = new StringTokenizer(string,";");
                line = "\n" + tokenizer.nextToken();
                chatTextArea.appendText(string);
                string = file.readLine();
            }
            f.close();
        }
        catch (IOException e){
            logger.error("Error To Read From File");
        }
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
            logger.error("Error sending message: {}", e.getMessage());
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
}
