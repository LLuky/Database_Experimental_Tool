package com;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import static com.Functions.*;
import static com.MyGridPane.runInfoStr;
import static com.ThirdNF_Decomposition.synthesis;
import static com.MyGridPane.output;
public class TestAlgorithms {
    // Randomly generate 8 PFDs
    public static ArrayList<PFD> genSigma(ArrayList<String> r,int certainty,int n){
        Random random = new Random();
        ArrayList<PFD> FDList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int c = random.nextInt(certainty) + 1;
            ArrayList<String> x = new ArrayList<>();
            ArrayList<String> y = new ArrayList<>();
            int sizeOfX = random.nextInt(r.size()) + 1;
//            int sizeOfX = random.nextInt(2) + 1;
            int sizeOfY = random.nextInt(r.size()) + 1;
            for (int j = 0; j < sizeOfX; j++) {
                String attr = r.get(random.nextInt(r.size()));
                if(!x.contains(attr))
                    x.add(attr);
            }
            for (int j = 0; j < sizeOfY; j++) {
                String attr = r.get(random.nextInt(r.size()));
                if(!y.contains(attr))
                    y.add(attr);
            }
            FDList.add(new PFD(x,y,c));
        }
        return FDList;
    }

    public static void testAlgorithms(String algType, int testTime, int beta, ArrayList<PFD> FDList,ArrayList<String> r){
        int j = 0;
        int numOfRelation;
        long timeTaken;
        while (j < testTime){
            numOfRelation = 0;
            Collections.shuffle(FDList);
            long startTime = System.nanoTime();
            for (int i = 1; i <= beta; i++) {
                if (algType.equals("BDNF"))
                    numOfRelation += synthesis(r, FDList, i).size();
//                else
//                    numOfRelation += Decompose(r,FDList,i).size();
            }
            long stopTime = System.nanoTime();
            timeTaken = stopTime - startTime;
            output += "num of Rs: " + numOfRelation + ", " +"Time: " + timeTaken + "\n";
            System.out.println("num of Rs: " + numOfRelation + ", " +"Time: " + timeTaken);
            j++;
        }
    }

    public static void testAlgorithmsVersion2(String algType, int testTime, int beta, ArrayList<String> r,int size){
        int j = 0;
        int numOfRelation;
        long timeTaken;
        ArrayList<PFD> FDList;
        ArrayList timeList = new ArrayList();
        ArrayList relationsList = new ArrayList();
        while (j < testTime){
            numOfRelation = 0;
            FDList = genSigma(r,beta,size);
            System.out.println("FDList: " + FDList.toString());
            long startTime;
            long stopTime;
            if (algType.equals("BDNF")){
                System.out.println("Im in BDNF");
                startTime = System.currentTimeMillis();
                for (int i = 1; i <= beta; i++) {
//                    numOfRelation += Decompose(r,FDList,i).size();
                }
                stopTime = System.currentTimeMillis();
            }else{
                startTime = System.currentTimeMillis();
                for (int i = 1; i <= beta; i++) {
                    numOfRelation += synthesis(r, FDList, i).size();
                }
                stopTime = System.currentTimeMillis();
            }
            timeTaken = (stopTime - startTime);
            timeList.add(timeTaken);
            relationsList.add(numOfRelation);
            output += "num of Rs: " + numOfRelation + ", " +"Time: " + timeTaken + "\n";
            System.out.println("num of Rs: " + numOfRelation + ", " +"Time: " + timeTaken);
            j++;
        }
        System.out.println(timeList.toString());
        System.out.println(relationsList.toString());
    }

    public static void testAlgorithmsVersion3(String algType, int testTime, int beta, ArrayList<String> r,int size){
        int j = 0;
        int numOfRelation;
        long timeTaken;
        ArrayList<PFD> FDList;
        ArrayList<Long> timeList = new ArrayList();
        ArrayList relationsList = new ArrayList();
        while (j < testTime){
            numOfRelation = 0;
            FDList = genSigma(r,beta,size);
//            System.out.println("FDList: " + FDList.toString());
            long startTime;
            long stopTime;
            if (algType.equals("BDNF")){
                System.out.println("Im in BDNF");
                for (int i = 1; i <= beta; i++) {
                    ArrayList<PFD> cutFDList = betaCut(genSigma(r,beta,size),i);
                    System.out.println("beta-cut: " + cutFDList.toString());
                    startTime = System.currentTimeMillis();
                    ArrayList<PFD> projection = projectToR(r,cutFDList,i);
//                    System.out.println("projection: " + projection.toString());

                    numOfRelation = Decompose(r,cutFDList,projection,i).size();
                    stopTime = System.currentTimeMillis();
                    timeTaken = (stopTime - startTime);
                    timeList.add(timeTaken);
                    relationsList.add(numOfRelation);
                }

            }else{
                for (int i = 1; i <= beta; i++) {
                    ArrayList<PFD> cutFDList = betaCut(genSigma(r,beta,size),i);
                    startTime = System.currentTimeMillis();
                    numOfRelation = synthesis(r, cutFDList, i).size();
                    stopTime = System.currentTimeMillis();
                    timeTaken = (stopTime - startTime);
                    System.out.println("Time: " + timeTaken );
                    timeList.add(timeTaken);
                    relationsList.add(numOfRelation);
                }
            }
            j++;
        }
        ArrayList<Integer> averageTime = getAverage(timeList,testTime,beta);
        ArrayList<Integer> averageRelation = getAverageR(relationsList,testTime,beta);
        output += "Time:" + averageTime.toString() + "\n";
        output += "Size:" + averageRelation.toString() + "\n";
        System.out.println(averageTime.toString());
        System.out.println(averageRelation.toString());
    }

    public static ArrayList<Integer> getAverage(ArrayList<Long> timeList, int round, int beta){
        ArrayList<Integer> averageTime = new ArrayList<>();
        for (int i = 0; i < beta ; i++) {
            double sum = 0;
            for (int j = 0; j < round ; j++) {
                sum += timeList.get(i + (j * beta));
            }
            averageTime.add((int) (sum / round));
        }
        return averageTime;
    }

    public static ArrayList<Integer> getAverageR(ArrayList<Integer> rList, int round, int beta){
        ArrayList<Integer> averageR = new ArrayList<>();
        for (int i = 0; i < beta ; i++) {
            double sum = 0;
            for (int j = 0; j < round ; j++) {
                sum += rList.get(i + (j * beta));
            }
            averageR.add((int) (sum / round));
        }
        return averageR;
    }

}
