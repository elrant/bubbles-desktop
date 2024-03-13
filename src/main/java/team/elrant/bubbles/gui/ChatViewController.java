package team.elrant.bubbles.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import team.elrant.bubbles.xmpp.ConnectedUser;

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

    public ChatViewController() {
        // Default constructor
    }


    public ChatViewController(ConnectedUser connectedUser, String contactJid){
        this.connectedUser = connectedUser;
        this.contactJid = contactJid;
    }

    @FXML
    protected void initialize(){

        connectedUser.addIncomingMessageListener();

        sendButton.setOnAction(actionEvent -> sendMessage());
    }
    @FXML
    protected void sendMessage(){
        try{
            String message = messageTextArea.getText();
            connectedUser.getSendMessage(contactJid,message);
            chatTextArea.appendText("Me: "+ message + "\n");
            messageTextArea.clear();
        }
        catch (Exception e){
            failedLoginLabel.setVisible(true);
        }
    }
}
