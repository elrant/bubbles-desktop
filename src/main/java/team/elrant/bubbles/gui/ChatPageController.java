package team.elrant.bubbles.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import team.elrant.bubbles.xmpp.ConnectedUser;
import java.io.IOException;

public class ChatPageController{
    @FXML
    private TextArea chatTextArea;
    @FXML
    private TextArea messageTextArea;
    @FXML
    private Button sendButton;
    @FXML
    private Label failedLoginLabel;
    private ConnectedUser connectedUser;
    private  String contactJid;
    private String password;

    public ChatPageController(ConnectedUser connectedUser,String contactJid,String password){
        this.connectedUser = connectedUser;
        this.contactJid = contactJid;
        this.password = password;
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
