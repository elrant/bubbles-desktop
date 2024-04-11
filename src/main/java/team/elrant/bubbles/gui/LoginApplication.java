package team.elrant.bubbles.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

/**
 * The LoginApplication class is the entry point for the login GUI application.
 * It loads the LoginView.fxml file, sets up the login scene, and displays the login window.
 */
public class LoginApplication extends Application {
    private static final Logger logger = LogManager.getLogger(LoginApplication.class);

    /**
     * The main method is the entry point of the LoginApplication.
     * It launches the JavaFX application.
     *
     * @param args The command-line arguments (not used in this context).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method is called when the JavaFX application is launched.
     * It sets up the login scene, loads the LoginView.fxml file, and displays the login window.
     *
     * @param stage The primary stage for this application, onto which the application scene can be set.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(@NotNull Stage stage) throws Exception {
        try {
            @NotNull FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
            @NotNull Scene scene = new Scene(fxmlLoader.load(), 280, 200);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("fluent-light.css")).toExternalForm());
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            logger.error("Error starting LoginApplication: {}", e.getMessage());
            throw e;
        }
    }
}
