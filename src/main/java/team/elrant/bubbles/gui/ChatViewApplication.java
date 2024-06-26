package team.elrant.bubbles.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jxmpp.jid.EntityBareJid;
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
            if (contactJid.isEmpty()) {
                throw new IllegalArgumentException("Contact JID is empty.");
            }

            logger.debug("Contact JID before parsing: {}", contactJid);

            EntityBareJid bareContactJid = JidCreate.entityBareFrom(contactJid);

            logger.debug("Parsed bare contact JID: {}", bareContactJid);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/ChatView.fxml"));

            // Set custom controller factory
            fxmlLoader.setControllerFactory(param -> new ChatViewController(connectedUser, bareContactJid));

            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root, 800, 700);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling/fluent-light.css")).toExternalForm());
            stage.setTitle("Chat");
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();
            stage.setOnCloseRequest(windowEvent -> {
                try {
                    ChatViewController chatViewController = fxmlLoader.getController();
                    chatViewController.saveToFile();
                } catch (Exception e) {
                    logger.error("Error: {}", e.getMessage());
                }
            });
        } catch (Exception e) {
            logger.error("Error starting ChatViewApplication: {}", e.getMessage());
            throw e;
        }
    }

}
