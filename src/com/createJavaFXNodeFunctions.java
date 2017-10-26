package com;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

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

    public static Dialog<ArrayList<String>> getTestAlgDialog(String alg){
        Dialog<ArrayList<String>> dialog = new Dialog<>();
        dialog.setTitle("Input Dialog");

        ButtonType okButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

// Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField sizeTextField = new TextField();
        TextField betaTextField = new TextField();
        TextField rTextField = new TextField();
        TextField roundTextField = new TextField();
        ObservableList<String> algOptions =
                FXCollections.observableArrayList(
                        "BDNF",
                        "3NF"
                );
        ComboBox algComboBox = new ComboBox(algOptions);

        if(alg.equals("v1")){
            grid.add(new Label("Algorithm"),0,0);
            grid.add(algComboBox,1,0);
            grid.add(new Label("Round"),0,1);
            grid.add(roundTextField,1,1);
        }
        else if(alg.equals("PFDs Generator")) {
            grid.add(new Label("Size:"), 0, 0);
            grid.add(sizeTextField, 1, 0);
            grid.add(new Label("Beta:"), 0, 1);
            grid.add(betaTextField, 1, 1);
            grid.add(new Label("Relation:"), 0, 2);
            grid.add(rTextField, 1, 2);
        }
        else {
            grid.add(new Label("Algorithm"),0,0);
            grid.add(algComboBox,1,0);
            grid.add(new Label("Round"),0,1);
            grid.add(roundTextField,1,1);
            grid.add(new Label("Size:"), 0, 2);
            grid.add(sizeTextField, 1, 2);
            grid.add(new Label("Beta:"), 0, 3);
            grid.add(betaTextField, 1, 3);
            grid.add(new Label("Relation:"), 0, 4);
            grid.add(rTextField, 1, 4);
        }




// Enable/Disable login button depending on whether a username was entered.
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

// Do some validation (using the Java 8 lambda syntax).
        if(alg.equals("v1")){
            roundTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(newValue.trim().isEmpty());
            });
        }
        else if(alg.equals("PFDs Generator")){
            rTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(newValue.trim().isEmpty());
            });
        } else{
            rTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(newValue.trim().isEmpty());
            });
        }

        dialog.getDialogPane().setContent(grid);

// Request focus on the username field by default.
        Platform.runLater(() -> sizeTextField.requestFocus());

// Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                ArrayList<String> result = new ArrayList<>();
                if(alg.equals("v1")){
                    result.add(algComboBox.getSelectionModel().getSelectedItem().toString());
                    result.add(roundTextField.getText());
                }
                else if(alg.equals("PFDs Generator")){
                    result.add(sizeTextField.getText());
                    result.add(betaTextField.getText());
                    result.add(rTextField.getText());
                }else {
                    result.add(rTextField.getText());
                    result.add(sizeTextField.getText());
                    result.add(betaTextField.getText());
                    result.add(algComboBox.getSelectionModel().getSelectedItem().toString());
                    result.add(roundTextField.getText());
                }
                return result;
            }
            return null;
        });

        return dialog;
    }
}
