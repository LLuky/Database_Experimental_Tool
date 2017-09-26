package com;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static com.Functions.*;
import static com.MyFileReader.fileReader;
import static com.TestAlgorithms.genSigma;
import static com.ThirdNF_Decomposition.getMinimalKeys;
import static com.ThirdNF_Decomposition.synthesis;
import static com.createJavaFXNodeFunctions.initFileChooseWithFilter;
import static com.createJavaFXNodeFunctions.showMessageDialog;


public class MyGridPane extends Pane {
    String selectedAlg = null;
    static final String runInfoStr = "======= Run Information ======= \n";

    TextArea logInfoArea;
    static TextField rTextField;
    static int k = 0;
    static ArrayList<String>    R      =  new ArrayList<>();
    static ArrayList<PFD>       FDList =  new ArrayList<>();

    public MyGridPane(){
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10,10,10,10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);

        Label rLabel = new Label("R = ");
        rTextField = new TextField();
        gridPane.add(rLabel,0,5);
        gridPane.add(rTextField,1,5);

        Label kLabel = new Label("K = ");
        TextField kTextField = new TextField();
        gridPane.add(kLabel,0,4);
        gridPane.add(kTextField,1,4);

        Label xLabel = new Label("X = ");
        TextField xTextField = new TextField();
        gridPane.add(xLabel,0,0);
        gridPane.add(xTextField,1,0);

        Label yLabel = new Label("Y = ");
        TextField yTextField = new TextField();
        gridPane.add(yLabel,0,1);
        gridPane.add(yTextField,1,1);

        Label bLabel = new Label("\u00DF"+" = ");
        TextField bTextField = new TextField();
        gridPane.add(bLabel,0,2);
        gridPane.add(bTextField,1,2);

        Label algLabel = new Label("Algorithm: ");
        ObservableList<String> algOptions =
                FXCollections.observableArrayList(
                        "Cononical Cover",
                        "BDNF",
                        "3NF",
                        "Minimal Keys",
                        "Algorithm Test"
                );
        ComboBox algComboBox = new ComboBox(algOptions);
        algComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedAlg = algComboBox.getSelectionModel().getSelectedItem().toString();
                System.out.println(selectedAlg);
            }
        });
        gridPane.add(algLabel,0,3);
        gridPane.add(algComboBox,1,3);

        ListView list = new ListView();
        ObservableList<String> items = FXCollections.observableArrayList(" ");
        list.setItems(items);

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(5);
        buttonBox.setPadding(new javafx.geometry.Insets(10, 20, 10, 20));

        Button addpfdButton = new Button("Add");
        addpfdButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String[] x = xTextField.getText().split("");
                    String[] y = yTextField.getText().split("");
                    int b = Integer.valueOf(bTextField.getText());
                    PFD pfd = new PFD( new ArrayList<String>(Arrays.asList(x)),new ArrayList<String>(Arrays.asList(y)),b);
                    items.remove(" ");
                    items.add(pfd.toString());
                    FDList.add(pfd);
                }catch (Exception e){
                    showMessageDialog("Invalid input!");
                }

            }
        });
        Button removepfdButton = new Button("Remove");
        removepfdButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    String selectedItem = list.getSelectionModel().getSelectedItem().toString();
                    items.remove(selectedItem);
                    FDList.remove(getPfd(selectedItem));
                }catch (NullPointerException e){
                    showMessageDialog("Please select an item on the list!");
                }

            }
        });

        // Button for run the selected algorithm.
        Button runButton = new Button("Run");
        runButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String output = runInfoStr;
                if(selectedAlg == null){
                    showMessageDialog("Please select an algorithm!");
                }
                else if (FDList.isEmpty() && !selectedAlg.equals("Algorithm Test")){
                    showMessageDialog("Please add Possibilistic Functional Dependencies!");
                }
                else {
                    output += selectedAlg + ":\n";
                    if(selectedAlg == "Cononical Cover"){
                        try {
//                            int k = Integer.valueOf(kTextField.getText());
                            FDList = getCanCover2(FDList);
                            output += FDList.toString();
                        } catch (Exception e){
                            showMessageDialog("Invalid k!");
                        }

                    }
                    else if(selectedAlg == "BDNF"){
                        try{
                            int k = Integer.valueOf(kTextField.getText());
                            for (int i = 1; i <= k; i++) {
                                output += "\u00DF" + i +":\n"+turnDeOutputToString(DecomposeWithTheCertainty(getRelationList(),FDList,i)) + "\n";
                            }

                        }
                        catch (Exception e){
                            showMessageDialog("Invalid Input!");
                        }
                    }
                    else if(selectedAlg == "3NF"){
//                        try{
                            int k = Integer.valueOf(kTextField.getText());
                            for (int i = 1; i <= k; i++) {
                                output += "\u00DF" + i +":\n" + turnDeOutputToString(synthesis(getRelationList(),FDList,i)) + "\n";
//                            }

                        }
//                        catch (Exception e){
//
//                            showMessageDialog("Invalid Input in 3NF!");
//
//                        }
                    }
                    else if(selectedAlg == "Minimal Keys"){
                        try{
                            int k = Integer.valueOf(kTextField.getText());
                            for (int i = 1; i <= k; i++) {
                                output += ""+ "\u00DF"+ i + ": " +getMinimalKeys(FDList,getRelationList(),i).toString() + "\n";
                            }
                        }
                        catch (Exception e){
                            showMessageDialog("Invalid Input!");
                        }
                    }
                    else if(selectedAlg == "Algorithm Test"){
                        int k = Integer.valueOf(kTextField.getText());
                        String rString = rTextField.getText();
                        ArrayList<String> r = new ArrayList<>(Arrays.asList(rString.split(";")));

                        items.removeAll(items);
                        FDList = genSigma(r,k,r.size());
                        for (PFD pfd: FDList) {
                            items.add(pfd.toString());
                        }
                    }

                    logInfoArea.setText(output);
                }

            }
        });

        Button importButton = new Button("Import");
        importButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FileChooser chooser = initFileChooseWithFilter();
                    File selectedFile = chooser.showOpenDialog(new Stage());
                    System.out.println(selectedFile.getAbsolutePath());
//                    items.remove(" ");
                    items.removeAll(items);
                    FDList = new ArrayList<>(fileReader(selectedFile));
                    for (PFD pfd: FDList) {
                        items.add(pfd.toString());
                    }
                } catch (Exception e){
                    System.out.println(e.fillInStackTrace());
                }
            }
        });
        buttonBox.getChildren().addAll(addpfdButton,removepfdButton,runButton,importButton);
//        this.add(buttonBox,1,3);

        logInfoArea = new TextArea();
        logInfoArea.setEditable(false);
        VBox logInfoBox = new VBox();
        logInfoBox.getChildren().addAll(new Label("Log Information"), logInfoArea);
        logInfoArea.setPrefSize(420,130);
        logInfoBox.setPadding(new Insets(10,50,20,10));
        list.setPrefSize(50,130);
        HBox hBox = new HBox();

        VBox listBox = new VBox();
        listBox.setPadding(new Insets(10,20,20,10));
        listBox.getChildren().addAll(list,buttonBox);
        hBox.getChildren().addAll(gridPane,listBox);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(hBox);
        borderPane.setBottom(logInfoBox);
        this.getChildren().addAll(borderPane);

    }

    public static PFD getPfd(String str){
        PFD target = null;
        for (PFD pfd: FDList) {
            if(pfd.toString().equals(str))
                target = pfd;
        }
        return target;
    }

    public ArrayList<String> getRelationList(){
        String[] tempList = null;
        try {
            tempList = rTextField.getText().split(";");
        } catch (Exception e){
            System.out.println(e.fillInStackTrace());
            showMessageDialog("Invalid Input!");
        }
        return new ArrayList<>(Arrays.asList(tempList));
    }

}
