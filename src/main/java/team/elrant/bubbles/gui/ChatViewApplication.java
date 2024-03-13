package team.elrant.bubbles.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import team.elrant.bubbles.xmpp.ConnectedUser;

public class ChatViewApplication extends Application {
    private ConnectedUser connectedUser = null;
    private String contactJid = null;

    public ChatViewApplication(ConnectedUser connectedUser, String contactJid) {
        this.connectedUser = connectedUser;
        this.contactJid = contactJid;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChatView.fxml"));

        // Set custom controller factory
        fxmlLoader.setControllerFactory(param -> {
            try {
                // Instantiate the controller with the required properties
                return new ChatViewController(connectedUser, contactJid);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });

        AnchorPane root = fxmlLoader.load();
        Scene scene = new Scene(root, 400, 800);

        primaryStage.setTitle("Chat");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

