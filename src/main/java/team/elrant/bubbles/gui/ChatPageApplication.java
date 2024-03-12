package team.elrant.bubbles.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ChatPageApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(@org.jetbrains.annotations.NotNull Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatPageApplication.class.getResource("ChatView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),400,800);
        primaryStage.setTitle("Chat");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}
