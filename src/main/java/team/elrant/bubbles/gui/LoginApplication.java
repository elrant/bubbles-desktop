package team.elrant.bubbles.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * The LoginApplication class is the entry point for the login GUI application.
 * It loads the LoginView.fxml file, sets up the login scene, and displays the login window.
 */
public class LoginApplication extends Application {

    /**
     * The main method is the entry point of the LoginApplication.
     * It launches the JavaFX application.
     *
     * @param args The command-line arguments (not used in this context).
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * The start method is called when the JavaFX application is launched.
     * It sets up the login scene, loads the LoginView.fxml file, and displays the login window.
     *
     * @param stage The primary stage for this application, onto which the application scene can be set.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(@NotNull Stage stage) throws IOException {
        @NotNull FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("LoginView.fxml"));
        @NotNull Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }
}
