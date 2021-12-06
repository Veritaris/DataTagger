package me.veritaris.datatagger.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class ViewController {

    public static Stage switchStage(Stage stage, SceneType sceneType) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ViewController.class.getResource(String.format("/views/%s.fxml", sceneType.fileName)));
        Parent root = fxmlLoader.load();
        stage.setTitle(sceneType.title);
        stage.setScene(new Scene(root));
        return stage;
    }
}
