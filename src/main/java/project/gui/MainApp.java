package project.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        MainController controller = new MainController();
        Scene scene = new Scene(controller.getRoot(), 900, 700);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Rectangle Packing Visualization");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
