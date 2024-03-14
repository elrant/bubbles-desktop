package team.elrant.bubbles.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import team.elrant.bubbles.xmpp.ConnectedUser;

/**
 * The ChatViewApplication class is responsible for launching the chat application GUI.
 * It initializes the primary stage with the ChatView.fxml layout and sets up the ChatViewController.
 */
public class ChatViewApplication extends Application {
    private final ConnectedUser connectedUser;
    private final String contactJid;

    /**
     * Constructs a ChatViewApplication with the specified connected user and contact JID.
     * @param connectedUser The connected user object.
     * @param contactJid The JID of the contact to chat with.
     */
    public ChatViewApplication(ConnectedUser connectedUser, String contactJid) {
        this.connectedUser = connectedUser;
        this.contactJid = contactJid;
    }

    /**
     * The start method initializes the primary stage of the chat application.
     * It loads the ChatView.fxml layout, sets up the ChatViewController, and displays the stage.
     * @param primaryStage The primary stage of the application.
     * @throws Exception If an error occurs during the initialization.
     */
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

    /**
     * The main method launches the JavaFX application.
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
