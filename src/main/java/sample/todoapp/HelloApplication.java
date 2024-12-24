package sample.todoapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 400);
        //stage.setResizable(false); <-- disables maximize button
       // stage.initStyle(StageStyle.UNDECORATED); <-- can hide the blue title bar
        stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("/logo.png")));
        stage.setMaxWidth(950);
        stage.setTitle("To-Do Application");
        stage.setScene(scene);
        HelloController.setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
