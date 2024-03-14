package team.elrant.bubbles.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import team.elrant.bubbles.xmpp.ConnectedUser;

/**
 * The ChatViewController class controls the chat view functionality in the GUI.
 * It handles sending and displaying messages in the chat interface.
 */
public class ChatViewController {
    @FXML
    private TextArea chatTextArea;
    @FXML
    private TextArea messageTextArea;
    @FXML
    private Button sendButton;
    @FXML
    private Label failedLoginLabel;
    private ConnectedUser connectedUser;
    private String contactJid;

    /**
     * Constructs a new instance of ChatViewController.
     * This constructor is used when no parameters are provided.
     */
    public ChatViewController() {
        // Default constructor
    }

    /**
     * Constructs a new instance of ChatViewController with the specified connected user and contact JID.
     * @param connectedUser The connected user instance.
     * @param contactJid The JID of the contact with whom the user is communicating.
     */
    public ChatViewController(ConnectedUser connectedUser, String contactJid){
        this.connectedUser = connectedUser;
        this.contactJid = contactJid;
    }

    /**
     * Initializes the chat view controller.
     * It adds an incoming message listener to the connected user and sets up the action event for the send button.
     */
    @FXML
    protected void initialize(){
        connectedUser.addIncomingMessageListener();
        sendButton.setOnAction(actionEvent -> sendMessage());
    }

    /**
     * Sends a message to the contact and updates the chat display.
     */
    @FXML
    protected void sendMessage(){
        try{
            String message = messageTextArea.getText();
            connectedUser.sendMessage(contactJid,message);
            chatTextArea.appendText("Me: "+ message + "\n");
            messageTextArea.clear();
        }
        catch (Exception e){
            failedLoginLabel.setVisible(true);
        }
    }
}
