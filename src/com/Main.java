package com;

import com.aquafx_project.AquaFx;
import com.guigarage.flatterfx.FlatterConfiguration;
import com.guigarage.flatterfx.FlatterFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    Stage window;
    Scene scene;
    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        window.setTitle("Window");
        Pane myGridPane = new MyGridPane();
        BorderPane borderPane = new BorderPane(myGridPane);
        scene = new Scene(borderPane,550,500);
//        scene.getStylesheets().add("Theme.css");
        window.setScene(scene);
        window.show();
    }
}
