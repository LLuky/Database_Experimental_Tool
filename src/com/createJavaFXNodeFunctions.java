package com;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class createJavaFXNodeFunctions {
    //Show a message Dialog. Need a message as a parameter.
    public static void showMessageDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
//        alert.showAndWait();
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.showAndWait();
    }

    //Create a file chooser
    public static FileChooser initFileChooseWithFilter() {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.TXT)", "*.TXT");
        chooser.getExtensionFilters().add(extFilter);
        chooser.setTitle("Open File");

        return chooser;
    }
}
