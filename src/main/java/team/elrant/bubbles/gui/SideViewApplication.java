package team.elrant.bubbles.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import team.elrant.bubbles.xmpp.ConnectedUser;

import java.util.Objects;

/**
 * The SideViewApplication class is responsible for launching the side view of the chat application GUI.
 * It initializes the primary stage with the SideView.fxml layout and sets up the SideViewController.
 */
public class SideViewApplication extends Application {

    private static final Logger logger = LogManager.getLogger(SideViewApplication.class);
    private final ConnectedUser connectedUser;

    /**
     * Constructs a SideViewApplication with the specified connected user.
     *
     * @param connectedUser The connected user object.
     */
    public SideViewApplication(ConnectedUser connectedUser) {
        this.connectedUser = connectedUser;
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
     * The start method initializes the primary stage of the side view application.
     * It loads the SideView.fxml layout and sets up the SideViewController.
     *
     * @param stage The primary stage of the application.
     * @throws Exception If an error occurs during the initialization.
     */
    @Override
    public void start(@NotNull Stage stage) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/SideView.fxml"));
            AnchorPane root = fxmlLoader.load();

            // Pass the connectedUser to the controller
            SideViewController controller = fxmlLoader.getController();
            controller.setConnectedUser(connectedUser);

            Scene scene = new Scene(root, 320, 720);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling/fluent-light.css")).toExternalForm());
            stage.setTitle("Chat");
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            logger.error("Error starting SideViewApplication: {}", e.getMessage());
            throw e;
        }
    }
}
