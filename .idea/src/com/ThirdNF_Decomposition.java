package com;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static com.Functions.*;


public class ThirdNF_Decomposition {
    // Decomposition
    public static ArrayList<Pair<ArrayList<String>,ArrayList<PFD>>> synthesis(ArrayList<String> r,ArrayList<PFD> FDList, int certainty){
        ArrayList<ArrayList<String>> minimalKeys = getMinimalKeys(FDList,r,certainty);
        ArrayList<String> betaPrimeList = getBetaPrimeList(FDList,r,certainty);
        ArrayList<Pair<ArrayList<String>,ArrayList<PFD>>> decomposedRs = new ArrayList<>();
        ArrayList<String> rClone = new ArrayList<>(r);

        ArrayList<PFD> FDClone = betaCut(FDList,certainty);
        System.out.println("Beta-cut: " + FDClone.toString());

        if(isSatisfied3NF(FDList,r,certainty))
            decomposedRs.add(new Pair(r,FDClone));
        else{
            ArrayList<PFD> newFDList = getCanCover2(FDClone);
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
                else{
                    Iterator<Pair<ArrayList<String>,ArrayList<PFD>>> it2 = decomposedRs.iterator();
                    while (it2.hasNext()){
                        Pair<ArrayList<String>,ArrayList<PFD>> pair = it2.next();
                        if(equal(pair.getKey(),subR))
                            pair.getValue().add(curPfd);
                    }
                }
            }

            if (!isContainedKeyInSigmaPrime(sigmaPrime,minimalKeys)){
                if(minimalKeys.isEmpty()){
                    int size = rClone.size();
                    ArrayList<String> processRList = new ArrayList<>(rClone);
                    while (true) {
                        for (String attr: rClone) {
                            processRList.remove(attr);
                            if(!equal(r,getClosureForAttr(processRList,FDList,certainty)))
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
    public static ArrayList<ArrayList<String>> getMinimalKeys(ArrayList<PFD> FDList, ArrayList<String> r, int certainty){
        ArrayList<ArrayList<String>> minimalKeys = new ArrayList<>();
        for (PFD pdf: FDList) {
            ArrayList<String> closureOfCurrentPfd = getClosureForAttr(pdf.x,FDList,certainty);
            if(equal(closureOfCurrentPfd,r)){
                if(pdf.x.size() == 1)
                    minimalKeys.add(pdf.x);
                else{
                    ArrayList<String> subset = getAllCombo(pdf.x);
                    boolean isMinimal = true;
                    for (String str: subset) {
                        String[] strList = str.split("");
                        if(strList.length < pdf.x.size()){
                            ArrayList<String> closure = getClosureForAttr(new ArrayList(Arrays.asList(strList)),FDList,certainty);
                            if(equal(closure,r))
                                isMinimal = false;
                        }
                    }
                    if(isMinimal)
                        minimalKeys.add(pdf.x);
                }
            }
        }
        return minimalKeys;
    }
    public static boolean isSatisfied3NF(ArrayList<PFD> FDList, ArrayList<String> r, int certainty){
        ArrayList<String> betaPrimeList = getBetaPrimeList(FDList,r,certainty);
        ArrayList<ArrayList<String>> minimalKeys = getMinimalKeys(FDList,r,certainty);
        boolean isSatisfied = true;
        for (PFD pfd: FDList){
            if(!isMinimalKey(pfd.x,minimalKeys))
                isSatisfied = false;
        }
        if(!isSatisfied){
            for (PFD pfd: FDList){
                isSatisfied = true;
                ArrayList<String> yList = pfd.y;
                if(!contain(betaPrimeList,yList))
                    return false;
            }
        }
        return isSatisfied;
    }
//    public static boolean isSatisfied3NF(ArrayList<PFD> FDList, ArrayList<String> r, int certainty){
//        ArrayList<String> betaPrimeList = getBetaPrimeList(FDList,r,certainty);
//        for (PFD pfd: FDList){
//            ArrayList<String> xList = pfd.x;
//            if(!contain(betaPrimeList,xList))
//                return false;
//        }
//        return true;
//    }

    public static ArrayList<String> getBetaPrimeList(ArrayList<PFD> FDList, ArrayList<String> r, int certainty){
        ArrayList<String> betaPrimeList = new ArrayList<>();
        ArrayList<ArrayList<String>> minimalKeys = getMinimalKeys(FDList,r,certainty);
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
            if(contain(xyList,xyPrimeList) && !curPfd.equals(pfd))
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
