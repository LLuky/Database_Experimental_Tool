package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import static com.MyGridPane.rTextField;

public class MyFileReader {
    // Read PFDs from a text file
    public static ArrayList<PFD> fileReader(File file){
        ArrayList<PFD> importedPfds = new ArrayList<>();
        BufferedReader reader = null;
        String currentLine;
        try {
            reader = new BufferedReader(new FileReader(file));
            int i = 0;
            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.equals("")){
                    if(i == 0){
                        rTextField.setText(currentLine);
                    }
                    else {
                        String[] curStrList  = currentLine.split(";");
                        if(curStrList.length == 3)
                            importedPfds.add(turnStringToPFD(curStrList));
                    }
                    i++;
                }
            }
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
        return importedPfds;
    }

    public static PFD turnStringToPFD(String[] splitedStr){

        String[] xList = splitedStr[0].split("");
        String[] yList = splitedStr[1].split("");
        int beta = Integer.valueOf(splitedStr[2]);

        return new PFD(new ArrayList(Arrays.asList(xList)),new ArrayList(Arrays.asList(yList)),beta);
    }
}
