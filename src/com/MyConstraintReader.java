package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import static com.MyGridPane.rTextField;

public class MyConstraintReader {
    //File file;
    ArrayList importedConstraints;
    ArrayList<String> R;

    public MyConstraintReader(File file){

        //this.file = file;
        this.importedConstraints = new ArrayList<>();
        //R = new ArrayList<>();
        fileReader(file);

    }




    // Read PCKs or PFDs from a text file
    public ArrayList fileReader(File file){

        int constraintType=0;
        // 1 -- PCK;
        // 2 -- PFD



        BufferedReader reader = null;
        String currentLine;
        try {
            reader = new BufferedReader(new FileReader(file));
            int i = 0;

            // Determain the type of the Constraints (PCK or PFD currently)
            currentLine = reader.readLine();
            if(currentLine.equals("PCK")){
                //ArrayList<PCK> importedConstraints = new ArrayList<>();
                constraintType = 1;
            }else if(currentLine.equals("PFD")) {
                //ArrayList<PFD> importedConstraints = new ArrayList<>();
                constraintType = 2;
            }

            // get Relation schema: R
            currentLine = reader.readLine();
            R  = new ArrayList(Arrays.asList(currentLine.split(";")));

            do {
                if (!currentLine.equals("")){
                    if(i == 0){
                        //rTextField.setText(currentLine);
                    }
                    else {
                        String[] curStrList  = currentLine.split(";");
                        if(curStrList.length == 3)
                            //PCK
                            if(constraintType == 1){
                                this.importedConstraints.add(turnStringToPCK(curStrList));
                            }else
                                //PFD
                                if(constraintType == 2){
                                this.importedConstraints.add(turnStringToPFD(curStrList));
                            }

                    }
                    i++;
                }
            }while ((currentLine = reader.readLine()) != null);
        } catch (IOException e){
            System.out.println(e.fillInStackTrace());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return importedConstraints;
    }



    private static PFD turnStringToPFD(String[] splitedStr){

        String[] xList = splitedStr[0].split("");
        String[] yList = splitedStr[1].split("");
        int beta = Integer.valueOf(splitedStr[2]);

        return new PFD(new ArrayList(Arrays.asList(xList)),new ArrayList(Arrays.asList(yList)),beta);
    }


    private static PCK turnStringToPCK(String[] splitedStr){

        String[] xList = splitedStr[0].split("");
        String[] yList = splitedStr[1].split("");
        int beta = Integer.valueOf(splitedStr[2]);

        return new PCK(new ArrayList(Arrays.asList(xList)),new ArrayList(Arrays.asList(yList)),beta);
    }
}
