package blog.javamastery.library.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LibraryApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            // Load the FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/fxml/main.fxml")
            );
            Parent root = fxmlLoader.load();

            // Create and configure the scene
            Scene scene = new Scene(root, 1024, 768);

            // Configure and show the stage
            stage.setTitle("Library Management System");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
