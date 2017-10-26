package com;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;

public class backup {
//    [6, 7, 9, 5, 9, 12, 5, 9]
//    [591, 735, 722, 1408, 2116, 1453, 2202, 2304, 3656, 4695, 4628, 3153, 5731, 6090, 5730]
//    public static ArrayList<Pair<ArrayList<String>,ArrayList<PFD>>> Decompose(ArrayList<String> r, ArrayList<PFD> FDList, int certainty){
//        ArrayList<Pair<ArrayList<String>,ArrayList<PFD>>> DecomposeRes = new ArrayList<>();
//        ArrayList<PFD> newFDClone = new ArrayList<>(FDList);
//        if(isSatisfiedBDFN(FDList,r,certainty))
//            DecomposeRes.add(new Pair(r,FDList));
//        else {
//            Iterator<PFD> it = FDList.iterator();
//            PFD selectPfd = null;
//            while (it.hasNext()){
//                PFD pfd = it.next();
//                if(!isKey(pfd.x, FDList,r, certainty)){
//                    selectPfd = pfd;
////                    FDList.remove(selectPfd);
//                    break;
//                }
//            }
//            ArrayList R1 = new ArrayList();
//            R1.addAll(selectPfd.x);
//            for (String str: selectPfd.y) {
//                if(!R1.contains(str))
//                    R1.add(str);
//            }
//
//            ArrayList<PFD> projectionR1 = projectToR2(R1,newFDClone,certainty);
//
//            ArrayList R2 = new ArrayList();
//            R2.addAll(selectPfd.x);
//            ArrayList<String> rClone = new ArrayList<>(r);
//            rClone.removeAll(R1);
//            R2.addAll(rClone);
//
//            ArrayList<PFD> projectionR2 = projectToR2(R2,newFDClone,certainty);
//
//            System.out.println("P2:" + R2.toString());
//            System.out.println("Projection:" + projectionR2.toString());
//
//
//            DecomposeRes.addAll(Decompose(R1,projectionR1,certainty));
//            DecomposeRes.addAll(Decompose(R2,projectionR2,certainty));
//        }
//        return DecomposeRes;
//    }

//    public static ArrayList<PFD> expand(ArrayList<PFD> FDList){
//        ArrayList<PFD> expandedList = new ArrayList<>(FDList);
//        ArrayList<PFD> newFDList = new ArrayList<>();
//        Iterator<PFD> iterator = expandedList.iterator();
//        while (iterator.hasNext()){
//            PFD curPfd = iterator.next();
//            if(curPfd.y.size() > 1){
//                iterator.remove();
//                for (String str1 : curPfd.y) {
//                    ArrayList<String> listY = new ArrayList<>();
//                    listY.add(str1);
//                    PFD pfd = new PFD(curPfd.x,listY,curPfd.certainty);
//                    newFDList.add(pfd);
//                }
//            }
//        }
//        expandedList.addAll(newFDList);
//        return expandedList;
//    }
}
