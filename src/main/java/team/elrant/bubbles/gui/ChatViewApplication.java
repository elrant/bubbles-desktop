package team.elrant.bubbles.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.impl.JidCreate;
import team.elrant.bubbles.xmpp.ConnectedUser;

import java.util.Objects;

/**
 * The ChatViewApplication class is responsible for launching the chat application GUI.
 * It initializes the primary stage with the ChatView.fxml layout and sets up the ChatViewController.
 */
public class ChatViewApplication extends Application {
    private static final Logger logger = LogManager.getLogger(ChatViewApplication.class);

    private final @NotNull ConnectedUser connectedUser;
    private final @NotNull String contactJid;

    /**
     * Constructs a ChatViewApplication with the specified connected user and contact JID.
     *
     * @param connectedUser The connected user object.
     * @param contactJid    The JID of the contact to chat with.
     */
    public ChatViewApplication(@NotNull ConnectedUser connectedUser, @NotNull String contactJid) {
        this.connectedUser = connectedUser;
        this.contactJid = contactJid;
    }

    /**
     * The main method launches the JavaFX application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method initializes the primary stage of the chat application.
     * It loads the ChatView.fxml layout, sets up the ChatViewController, and displays the stage.
     *
     * @param stage The primary stage of the application.
     * @throws Exception If an error occurs during the initialization.
     */
    @Override
    public void start(@NotNull Stage stage) throws Exception {
        try {
            @NotNull FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChatView.fxml"));

            // Set custom controller factory
            fxmlLoader.setControllerFactory(param -> {
                try {
                    // Instantiate the controller with the required properties
                    BareJid bareContactJid = JidCreate.bareFrom(contactJid);
                    return new ChatViewController(connectedUser, bareContactJid);
                } catch (Exception e) {
                    logger.error("Error creating ChatViewController: {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            });

            AnchorPane root = fxmlLoader.load();
            @NotNull Scene scene = new Scene(root, 800, 700);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("fluent-light.css")).toExternalForm());
            stage.setTitle("Chat");
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            logger.error("Error starting ChatViewApplication: {}", e.getMessage());
            throw e;
        }
    }
}
