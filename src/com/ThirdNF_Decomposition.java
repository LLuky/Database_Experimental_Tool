package com;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static com.Functions.*;
import static com.MyGridPane.output;


public class ThirdNF_Decomposition {
    // Decomposition
    public static ArrayList<Pair<ArrayList<String>,ArrayList<PFD>>> synthesis(ArrayList<String> r,ArrayList<PFD> FDList, int certainty){
        long start1 = System.currentTimeMillis();
        ArrayList<ArrayList<String>> minimalKeys = getAllMinimalKeysV2(FDList,r,certainty);
        long stop1 = System.currentTimeMillis();
        System.out.println("time: " + (stop1-start1));
        System.out.println("Minimal Keys: " + minimalKeys.toString());

        ArrayList<Pair<ArrayList<String>,ArrayList<PFD>>> decomposedRs = new ArrayList<>();
        ArrayList<String> rClone = new ArrayList<>(r);

        ArrayList<PFD> FDClone = new ArrayList<>(FDList);

        boolean isSatisfied3NF = isSatisfied3NF(FDClone,r,certainty,minimalKeys);
        System.out.println("is satisfied 3NF" + isSatisfied3NF);
        if(isSatisfied3NF)
            decomposedRs.add(new Pair(r,FDClone));
        else{

            ArrayList<PFD> newFDList = getCanCover2(FDClone);

//            System.out.println("Time: "+ (start-stop));
            ArrayList<PFD> sigmaPrime = new ArrayList<>();
            Iterator<PFD> it = newFDList.iterator();
            while (it.hasNext()){
                PFD curPfd = it.next();
                ArrayList<String> subR = new ArrayList<>();
                subR.addAll(curPfd.x);
                subR.addAll(curPfd.y);
                if(isNotSubset(curPfd,sigmaPrime)){
                    it.remove();
                    decomposedRs.add(new Pair(subR,new ArrayList<PFD>(Arrays.asList(curPfd))));
                    sigmaPrime.add(curPfd);
                }
//                else{
//                    Iterator<Pair<ArrayList<String>,ArrayList<PFD>>> it2 = decomposedRs.iterator();
//                    while (it2.hasNext()){
//                        Pair<ArrayList<String>,ArrayList<PFD>> pair = it2.next();
//                        if(equal(pair.getKey(),subR))
//                            pair.getValue().add(curPfd);
//                    }
//                }
            }

            if (!isContainedKeyInSigmaPrime(sigmaPrime,minimalKeys)){
                if(!minimalKeys.isEmpty()){
                    int size = rClone.size();
                    ArrayList<String> processRList = new ArrayList<>(rClone);
                    while (true) {
                        for (String attr: rClone) {
                            processRList.remove(attr);
                            if(!equal(r,getClosureForAttr(processRList,FDClone,certainty)))
                                processRList.add(attr);
                        }
                        if (size == processRList.size())
                            break;
                        else {
                            size = processRList.size();
                            rClone = new ArrayList<>(processRList);
                        }
                    }
                    PFD pfd = new PFD(processRList,r,certainty);
                    decomposedRs.add(new Pair<>(processRList,new ArrayList<PFD>(Arrays.asList(pfd))));
                }
            }
        }

        return decomposedRs;
    }
    // Get all minimal keys from PFDs
//    public static ArrayList<ArrayList<String>> getMinimalKeys(ArrayList<PFD> FDList, ArrayList<String> r, int certainty){
//        ArrayList<ArrayList<String>> minimalKeys = new ArrayList<>();
//        for (PFD pdf: FDList) {
//            ArrayList<String> closureOfCurrentPfd = getClosureForAttr(pdf.x,FDList,certainty);
//            if(equal(closureOfCurrentPfd,r)){
//                if(pdf.x.size() == 1)
//                    minimalKeys.add(pdf.x);
//                else{
//                    ArrayList<String> subset = getAllCombo(pdf.x);
//                    boolean isMinimal = true;
//                    for (String str: subset) {
//                        String[] strList = str.split("");
//                        if(strList.length < pdf.x.size()){
//                            ArrayList<String> closure = getClosureForAttr(new ArrayList(Arrays.asList(strList)),FDList,certainty);
//                            if(equal(closure,r))
//                                isMinimal = false;
//                        }
//                    }
//                    if(isMinimal)
//                        minimalKeys.add(pdf.x);
//                }
//            }
//        }
//        return minimalKeys;
//    }

    public static ArrayList<ArrayList<String>> getAllMinimalKeys(ArrayList<PFD> FDList, ArrayList<String> r, int certainty){
        ArrayList<ArrayList<String>> allMinimalKeys = new ArrayList<>();
        ArrayList<String> subset = getAllCombo(r);
        ArrayList<String> subsetClone = new ArrayList<>(subset);
        ArrayList<String> acceptedCombo = new ArrayList<>();
        for (String combo: subset) {
             ArrayList<String> strList = new ArrayList<>(Arrays.asList(combo.split("")));
             ArrayList<String> closure = getClosureForAttr(strList, FDList, certainty);
            if(!isSuperset(acceptedCombo,combo)){
                if(equal(closure,r)){
                    allMinimalKeys.add(strList);
                    acceptedCombo.add(combo);
                }
            }

        }
        return allMinimalKeys;
    }
    public static ArrayList<ArrayList<String>> getAllMinimalKeysV2(ArrayList<PFD> FDList, ArrayList<String> r, int certainty){
        ArrayList<ArrayList<String>> allMinimalKeys = new ArrayList<>();
        ArrayList<PFD> canCoverFDList = getCanCover1(FDList);
        ArrayList<String> L = new ArrayList<>();
        ArrayList<String> R = new ArrayList<>();
        ArrayList<String> B = new ArrayList<>();
        ArrayList<String> LTemp = new ArrayList<>();
        ArrayList<String> RTemp = new ArrayList<>();

        for (PFD pfd: canCoverFDList) {
            LTemp.addAll(pfd.x);
            RTemp.addAll(pfd.y);
        }
        for (String attr: r) {
            if(LTemp.contains(attr) && !RTemp.contains(attr))
                L.add(attr);
            else if(!LTemp.contains(attr) && RTemp.contains(attr))
                R.add(attr);
            else if(LTemp.contains(attr) && RTemp.contains(attr))
                B.add(attr);
            else
                L.add(attr);
        }
        System.out.println(L.toString());
        System.out.println(R.toString());
        System.out.println(B.toString());
        if(equal(r,getClosureForAttr(L,FDList,certainty)))
            allMinimalKeys.add(L);
        else{
            ArrayList<String> allComb = getAllCombo(B);
            ArrayList<String> acceptedComb = new ArrayList<>();
            for (String comb: allComb) {
                ArrayList<String> currentList = new ArrayList<>(L);
                currentList.addAll(new ArrayList<>(Arrays.asList(comb.split(""))));
                if(!isSuperset(acceptedComb,comb)){
                    if(equal(r,getClosureForAttr(currentList,FDList,certainty))){
                        allMinimalKeys.add(currentList);
                        acceptedComb.add(comb);
                    }
                }
            }

        }
        return allMinimalKeys;
    }

    //check whether it is a superset
    public static boolean isSuperset(ArrayList<String> acceptedComb, String selectedComb){
        for (String curComb: acceptedComb) {
            if(Arrays.asList(selectedComb.split("")).containsAll(Arrays.asList(curComb.split(""))))
                return true;
        }
        return false;
    }

    public static boolean isSatisfied3NF(ArrayList<PFD> FDList, ArrayList<String> r, int certainty,ArrayList<ArrayList<String>> minimalKeys){
        ArrayList<String> betaPrimeList = getBetaPrimeList(r,minimalKeys);
        boolean isSatisfied = true;
        for (PFD pfd: FDList){
            if(!equal(getClosureForAttr(pfd.x,FDList,certainty),r))
                isSatisfied = false;
        }
        if(!isSatisfied){
            for (PFD pfd: FDList){
                isSatisfied = true;
                ArrayList<String> yList = pfd.y;
                Iterator<String> it = yList.iterator();
                while (it.hasNext()){
                    String x = it.next();
                    if(pfd.x.contains(x))
                        it.remove();
                }
                if(!contain(betaPrimeList,yList))
                    return false;
            }
        }
        return isSatisfied;
    }


    public static ArrayList<String> getBetaPrimeList(ArrayList<String> r,ArrayList<ArrayList<String>> minimalKeys){
        ArrayList<String> betaPrimeList = new ArrayList<>();
        for (String attr: r) {
            if(isContainedAttrInMinkeys(attr,minimalKeys))
                betaPrimeList.add(attr);
        }
        return betaPrimeList;
    }

    private static boolean isContainedAttrInMinkeys(String attr,ArrayList<ArrayList<String>> minimalKeys){
        boolean isContained = false;
        for (ArrayList<String> key: minimalKeys){
            if(key.contains(attr))
                isContained = true;
        }
        return isContained;
    }

    public static boolean isNotSubset(PFD pfd,ArrayList<PFD> FDList){
        ArrayList<String> xyList = new ArrayList<>();
        xyList.addAll(pfd.x);
        xyList.addAll(pfd.y);
        for (PFD curPfd: FDList){
            ArrayList<String> xyPrimeList = new ArrayList<>();
            xyPrimeList.addAll(curPfd.x);
            xyPrimeList.addAll(curPfd.y);
            if(contain(xyPrimeList,xyList) && !curPfd.equals(pfd))
                return false;
        }
        return true;
    }
    // check whether the sigma contains minimal keys?
    public static boolean isContainedKeyInSigmaPrime(ArrayList<PFD> sigmaPrime, ArrayList<ArrayList<String>> minimalKeys){
        for (PFD pfd: sigmaPrime) {
            if(isMinimalKey(pfd.x,minimalKeys))
                return true;
        }
        return false;
    }
    // Check whether X is a minimal key
    public static boolean isMinimalKey(ArrayList<String> x, ArrayList<ArrayList<String>> minimalKeys){
        boolean isKey = false;
        for (ArrayList<String> key: minimalKeys) {
            if(x.containsAll(key))
                isKey = true;
        }
        return isKey;
    }

}
