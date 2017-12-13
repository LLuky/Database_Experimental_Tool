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
import java.util.Optional;

import static com.Functions.*;
import static com.TestAlgorithms.testAlgorithmsVersion3;
import static com.createJavaFXNodeFunctions.*;



public class MyGridPane_PCK extends Pane {
    String selectedAlg = null;
    static final String runInfoStr = "======= Run Information ======= \n";
    static String output = "";
    TextArea logInfoArea;
    static TextField rTextField;
    static int beta = 0;
    static ArrayList<String>    R      =  new ArrayList<>();
    static ArrayList<PCK>       PCKList =  new ArrayList<>();

    public MyGridPane_PCK(){
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10,10,10,10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);

        Label rLabel = new Label("R = ");
        rTextField = new TextField();
        gridPane.add(rLabel,0,5);
        gridPane.add(rTextField,1,5);

        Label tLabel = new Label("test in r  = ");
        TextField tTextField = new TextField();
        gridPane.add(tLabel,0,4);
        gridPane.add(tTextField,1,4);

        Label cLabel = new Label("C = ");
        TextField cTextField = new TextField();
        gridPane.add(cLabel,0,0);
        gridPane.add(cTextField,1,0);

        Label kLabel = new Label("K = ");
        TextField kTextField = new TextField();
        gridPane.add(kLabel,0,1);
        gridPane.add(kTextField,1,1);

        Label bLabel = new Label("\u00DF"+" = ");
        TextField bTextField = new TextField();
        gridPane.add(bLabel,0,2);
        gridPane.add(bTextField,1,2);

        Label algLabel = new Label("Algorithm: ");
        ObservableList<String> algOptions =
                FXCollections.observableArrayList(
                        "Armstrong Relation"
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
        buttonBox.setPadding(new Insets(10, 20, 10, 20));

        Button addpckButton = new Button("Add");
        addpckButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String[] x = cTextField.getText().split("");
                    String[] y = kTextField.getText().split("");
                    int b = Integer.valueOf(bTextField.getText());
                    PCK pck = new PCK( new ArrayList<String>(Arrays.asList(x)),new ArrayList<String>(Arrays.asList(y)),b);
                    items.remove(" ");
                    items.add(pck.toString());
                    PCKList.add(pck);
                }catch (Exception e){
                    showMessageDialog("Invalid input!");
                }

            }
        });
        Button removepckButton = new Button("Remove");
        removepckButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    String selectedItem = list.getSelectionModel().getSelectedItem().toString();
                    items.remove(selectedItem);
                    PCKList.remove(getPCK(selectedItem));
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
                output = runInfoStr;
                if(selectedAlg == null){
                    showMessageDialog("Please select an algorithm!");
                }
                else if (PCKList.isEmpty() && !selectedAlg.equals("PCKs Generator") && !selectedAlg.equals("Algorithm Test")){
                    showMessageDialog("Please add Possibilistic Contextual Key!");
                }
                else {
                    output += selectedAlg + ":\n";
                    if(selectedAlg == "Armstrong Relation"){
                        try {

                            int t = Integer.valueOf(tTextField.getText());

                            long time = System.currentTimeMillis();
                            System.out.println("What's in it?"+PCKList);

                            ArmstrongPCK armstrongPCK = new ArmstrongPCK(R, t,PCKList);//Integer.valueOf(bTextField.getText())
                            ArrayList<ArrayList<String>> antiPCK= armstrongPCK.visualizePCK();

                            long time2 = System.currentTimeMillis();
                            System.out.println("Time: "+ (time2- time));

                            output += "\n Anti PCK: \n";
                            for(int i = 1; i< armstrongPCK.antiSigSet.size();i++) {
                                output += armstrongPCK.antiSigSet.get(i) + "\n";
                            }


                            output += "\n Armstrong Relation: \n";
                            for(String attr:R){
                                output += attr + "      ";
                            }
                            output += "p-degree \n";
                            for(int i = 0; i< antiPCK.size();i++) {
                                output += antiPCK.get(i) +"\n";
                            }
                        } catch (Exception e){
                            showMessageDialog("Invalid certainty \u03B1!");
                        }

                    }/*
                    else if(selectedAlg == "BDNF"){
                        try{
                            int k = Integer.valueOf(kTextField.getText());
                            ArrayList<String> r = getRelationList(rTextField.getText());
                            for (int i = 1; i <= k; i++) {
                                ArrayList<PFD> cutFDList = betaCut(FDList,i);
                                ArrayList<PFD> projection = projectToR3(r,cutFDList,i);
                                output += "\u00DF" + i +":\n"+turnDeOutputToString(Decompose(r,cutFDList,projection,i)) + "\n";
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
                                ArrayList<PFD> cutFDList = betaCut(FDList,i);
                                output += "\u00DF" + i +":\n" + turnDeOutputToString(synthesis(getRelationList(rTextField.getText()),cutFDList,i)) + "\n";
//                            }

                        }
//                        catch (Exception e){
//
//                            showMessageDialog("Invalid Input in 3NF!");
//
//                        }
                    }
                    else if(selectedAlg == "Minimal Keys old version"){
                        try{
                            int k = Integer.valueOf(kTextField.getText());
                            for (int i = 1; i <= k; i++) {
                                output += ""+ "\u00DF"+ i + ": " +getAllMinimalKeys(FDList,getRelationList(rTextField.getText()),i).toString() + "\n";
                            }
                        }
                        catch (Exception e){
                            showMessageDialog("Invalid Input!");
                        }
                    }
                    else if(selectedAlg == "Minimal Keys"){
                        try{
                            int k = Integer.valueOf(kTextField.getText());
                            for (int i = 1; i <= k; i++) {
                                output += ""+ "\u00DF"+ i + ": " +getAllMinimalKeysV2(FDList,getRelationList(rTextField.getText()),i).toString() + "\n";
                            }
                        }
                        catch (Exception e){
                            showMessageDialog("Invalid Input!");
                        }
                    }
                    else if(selectedAlg == "Projection v1"){
                        try{
                            int k = Integer.valueOf(kTextField.getText());
                            for (int i = 1; i <= k; i++) {
                                output += ""+ "\u00DF"+ i + ": " +projectToR(getRelationList(rTextField.getText()),FDList,i).toString() + "\n";
                            }
                        }
                        catch (Exception e){
                            showMessageDialog("Invalid Input!");
                        }
                    }
                    else if(selectedAlg == "Projection v3"){
                        try{
                            int k = Integer.valueOf(kTextField.getText());
                            for (int i = 1; i <= k; i++) {
                                output += ""+ "\u00DF"+ i + ": " +projectToR3(getRelationList(rTextField.getText()),FDList,i).toString() + "\n";
                            }
                        }
                        catch (Exception e){
                            showMessageDialog("Invalid Input!");
                        }
                    }*/
                    else if(selectedAlg == "PCKs Generator"){
                        Dialog dialog = getTestAlgDialog("PCKs Generator");
                        Optional<ArrayList<String>> result = dialog.showAndWait();
                        ArrayList<String> resultList = result.get();
                        int size = Integer.valueOf(resultList.get(0));
                        beta = Integer.valueOf(resultList.get(1));
                        R = getRelationList(resultList.get(2));

                        items.removeAll(items);
//                        PCKList = genSigma(R,beta,size);
//                        for (PFD pfd: PCKList) {
//                            items.add(pfd.toString());
//                        }
                    }
                    else if(selectedAlg.equals("Algorithm Test")){
                        Dialog dialog = getTestAlgDialog("v2");
                        Optional<ArrayList<String>> result = dialog.showAndWait();
                        ArrayList<String> resultList = result.get();
                        R = getRelationList(resultList.get(0));
                        int size = Integer.valueOf(resultList.get(1));
                        beta = Integer.valueOf(resultList.get(2));
                        String alg = resultList.get(3);
                        int round = Integer.valueOf(resultList.get(4));
                        testAlgorithmsVersion3(alg,round,beta,R,size);
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

                    MyConstraintReader consReader = new MyConstraintReader(selectedFile);
                    R = consReader.R;
                    PCKList = new ArrayList<>(consReader.importedConstraints);
                    for (PCK pck: PCKList) {
                        items.add(pck.toString());
                    }
                } catch (Exception e){
                    System.out.println(e.fillInStackTrace());
                }
            }
        });
        buttonBox.getChildren().addAll(addpckButton,removepckButton,runButton,importButton);
//        this.add(buttonBox,1,3);

        logInfoArea = new TextArea();
        logInfoArea.setEditable(false);
        VBox logInfoBox = new VBox();
        logInfoBox.getChildren().addAll(new Label("Log Information"), logInfoArea);
        logInfoArea.setPrefSize(500,230);
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

    public static PCK getPCK(String str){
        PCK target = null;
        for (PCK pck: PCKList) {
            if(pck.toString().equals(str))
                target = pck;
        }
        return target;
    }

    public ArrayList<String> getRelationList(String input){
        String[] tempList = null;
        try {
            tempList = input.split(";");
        } catch (Exception e){
            System.out.println(e.fillInStackTrace());
            showMessageDialog("Invalid Input!");
        }
        return new ArrayList<>(Arrays.asList(tempList));
    }

}
