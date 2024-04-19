package team.elrant.bubbles.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SideViewApplication extends Application {

    private static final Logger logger = LogManager.getLogger(SideViewApplication.class);

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method initializes the primary stage of the chat application.
     * It loads the SideView.fxml layout and displays the stage.
     *
     * @param stage The primary stage of the application.
     * @throws Exception If an error occurs during the initialization.
     */
    @Override
    public void start(@NotNull Stage stage) throws Exception {
        try {
            @NotNull FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/SideView.fxml"));
            AnchorPane root = fxmlLoader.load();
            @NotNull Scene scene = new Scene(root, 320, 720);
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

        