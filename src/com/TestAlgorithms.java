package com;

import java.util.ArrayList;
import java.util.Random;

public class TestAlgorithms {
    // Randomly generate 8 PFDs
    public static ArrayList<PFD> genSigma(ArrayList<String> r,int certainty,int n){
        Random random = new Random();
        ArrayList<PFD> FDList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int c = random.nextInt(certainty) + 1;
            ArrayList<String> x = new ArrayList<>();
            ArrayList<String> y = new ArrayList<>();
            int sizeOfX = random.nextInt(n) + 1;
            int sizeOfY = random.nextInt(n) + 1;
            for (int j = 0; j < sizeOfX; j++) {
                x.add(r.get(random.nextInt(n)));
            }
            for (int j = 0; j < sizeOfY; j++) {
                y.add(r.get(random.nextInt(n)));
            }
            FDList.add(new PFD(x,y,c));
        }
        return FDList;
    }
}
