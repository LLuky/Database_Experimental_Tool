package com;
import javafx.util.Pair;

import java.util.*;


public class Functions {
    static ArrayList<Pair<ArrayList<String>,ArrayList<PFD>>> S = new ArrayList<>();

    public static ArrayList<String> getClosureForAttr(ArrayList<String> x, ArrayList<PFD> FDList, int certainty){
        ArrayList<String> closureArrayList = new ArrayList<>(x);//put x and y into the closure of pfd
        int size = closureArrayList.size();

        while(true){
            for (PFD curPfd: FDList) {
                if(curPfd.x.size() <= closureArrayList.size() && closureArrayList.containsAll(curPfd.x)
                        && curPfd.certainty <= certainty){
                    for (String str: curPfd.y) {
                        if(!closureArrayList.contains(str))
                            closureArrayList.add(str);
                    }
                }
            }
            int curSize = closureArrayList.size();
            if(curSize != size)
                size = curSize;
            else{
                break;
            }
        }
        return closureArrayList;
    }


    //Non-redundant, L-reduced covers where all FDs have the form X->A
    public static ArrayList<PFD> getCanCover1(ArrayList<PFD> FDList){
        ArrayList<PFD>  CanCover = new ArrayList<>(FDList);
        for(PFD curPfd: FDList){
            ArrayList<String> z = new ArrayList<>();
            z.addAll(curPfd.x);
            //fresh copy of the underlying array for iteration
            ArrayList<String> iter = new ArrayList<>();
            iter.addAll(z);
            for(String b: iter){
                z.remove(b);
                ArrayList<String> closureArrayList = getClosureForAttr(z,CanCover,curPfd.certainty);
//                System.out.println("x:"+z.toString()+ ", Closure:"+closureArrayList.toString());
                if(!contain(closureArrayList,curPfd.y))
                    z.add(b);
                }
            for(String y: curPfd.y){
                ArrayList<String> yList = new ArrayList<>();
                yList.add(y);
                PFD pfd = new PFD(z,yList,curPfd.certainty);
                CanCover.remove(curPfd);
                CanCover.add(pfd);
            }
        }
//        System.out.println("CanCover(before merge)--"+CanCover.toString());
        NRcover(CanCover);
        return CanCover;
    }


    //Non-redundant, L-reduced covers where the LHSs of FDs are unique for the same beta
    public static ArrayList<PFD> getCanCover2(ArrayList<PFD> FDList){
        ArrayList<PFD>  CanCover = getCanCover1(FDList);
        ArrayList<PFD> iter = new ArrayList<>();
        iter.addAll(CanCover);
        // Store for a Map with the certainty as key and all the X (left-hand side) as value
        HashMap<Integer,ArrayList<ArrayList<String>>> allLeft = new HashMap<>();


        for(int i = 0; i < iter.size(); i++){
            PFD cur = CanCover.get(0);
            // Store for a Map with the certainty as key and all the X (left-hand side) as value
            ArrayList<ArrayList<String>> curCerArray = new ArrayList<ArrayList<String>>();
            if(allLeft.containsKey(cur.certainty)){
                curCerArray = allLeft.get((Integer)cur.certainty);
            }
            allLeft.put(cur.certainty,curCerArray);


            if(!allLeft.get(cur.certainty).contains(cur.x)) {
                ArrayList<String> holdY = new ArrayList<>();


                for (PFD curPfd : iter) {
                    if (equal(curPfd.x, cur.x)
                            && curPfd.certainty == cur.certainty) {
                        holdY.addAll(curPfd.y);
                        CanCover.remove(curPfd);
                    }

                }
                CanCover.add(new PFD(cur.x, holdY, cur.certainty));
                curCerArray.add(cur.x);
                //System.out.println("Round " + i + ":        " + CanCover);
            }

        }

        System.out.println("CanCover--"+CanCover.toString()+'\n');
        return CanCover;
    }

    private static ArrayList<PFD> NRcover(ArrayList<PFD> FDList){
        ArrayList<PFD>  NRCover = FDList;
        ArrayList<PFD>  checkList = new ArrayList<>();
        checkList.addAll(FDList);

        for(PFD curPfd: checkList) {
            NRCover.remove(curPfd);
            ArrayList<String> closureArrayList = getClosureForAttr(curPfd.x, NRCover, curPfd.certainty);
            if (!contain(closureArrayList,curPfd.y)) { // contain Y or not
                NRCover.add(curPfd);
            }
        }
        System.out.println("NR---" + NRCover.toString());
        return NRCover;
    }


    public static boolean contain(ArrayList<String> a, ArrayList<String> b){
        boolean containY = false;
        if(a.isEmpty() || b.isEmpty()){
            return containY;
        }
        String last = b.get(b.size()-1);
        for(String bEle: b){
            for(String aEle: a){
                if (aEle.equals(bEle)) {
                    containY = true;
                    break;
                }
            }
            if(!containY) //if not contain current y, then break directly
                break;

            if(bEle!=last) // reset boolean for the next round of check
                containY = false;

        }
        return containY;
    }

    public static boolean equal(ArrayList<String> a, ArrayList<String> b){
        boolean flag = false;
        if(contain(a,b) && contain(b,a)){
            flag = true;
        }
        return flag;
    }

    public static boolean isKey(ArrayList<String> key, ArrayList<PFD> Sigma,ArrayList<String> r,int certainty){
	    boolean iskey = false;
	    ArrayList<String> keyClosure = getClosureForAttr(key,Sigma,certainty);
//        System.out.println(key.toString()+"_Closure = "+ keyClosure);
	    if(equal(keyClosure,r)) {
//            System.out.println(key + " is key!! ");
            iskey = true;
        }
//        else {
////            System.out.println(key + " is not key ");
//        }
	    return iskey;
    }

    public static ArrayList<Pair<ArrayList<String>,ArrayList<PFD>>> Decompose(ArrayList<String> r, ArrayList<PFD> FDList,ArrayList<PFD> prjOfFDList,int certainty){
        ArrayList<Pair<ArrayList<String>,ArrayList<PFD>>> DecomposeRes = new ArrayList<>();
        ArrayList<PFD> newFDClone = new ArrayList<>(FDList);
        if(isSatisfiedBDFN(FDList,r,certainty))
            DecomposeRes.add(new Pair(r,FDList));
        else {
            Iterator<PFD> it = FDList.iterator();
            PFD selectPfd = null;
            while (it.hasNext()){
                PFD pfd = it.next();
                if(!isKey(pfd.x, FDList,r, certainty)){
                    selectPfd = pfd;
//                    FDList.remove(selectPfd);
                    break;
                }
            }
            ArrayList R1 = new ArrayList();
            R1.addAll(selectPfd.x);
            for (String str: selectPfd.y) {
                if(!R1.contains(str))
                    R1.add(str);
            }

            ArrayList<PFD> projectionR1 = getRelevantFDs(R1,prjOfFDList);

            ArrayList R2 = new ArrayList();
            R2.addAll(selectPfd.x);
            ArrayList<String> rClone = new ArrayList<>(r);
            rClone.removeAll(R1);
            R2.addAll(rClone);

            ArrayList<PFD> projectionR2 = getRelevantFDs(R2,prjOfFDList);

            System.out.println("P2:" + R2.toString());
            System.out.println("Projection:" + projectionR2.toString());


            DecomposeRes.addAll(Decompose(R1,projectionR1,prjOfFDList,certainty));
            DecomposeRes.addAll(Decompose(R2,projectionR2,prjOfFDList,certainty));
        }
        return DecomposeRes;
    }

    public static boolean isSatisfiedBDFN(ArrayList<PFD> FDList,ArrayList<String> r,int certainty){
        boolean isKey = true;
        Iterator<PFD> it = FDList.iterator();
        PFD removedPFD = null;
        while (it.hasNext()){
            PFD pfd = it.next();
            if(!isKey(pfd.x, FDList, r,certainty)){
                it.remove();
                isKey = false;
                removedPFD = pfd;
                break;
            }
        }
        if(!isKey){
            FDList.add(0,removedPFD);
        }
        return isKey;
    }
    // get relevant FDs from projection of FDList
    public static ArrayList<PFD> getRelevantFDs(ArrayList<String> r, ArrayList<PFD> prj){
        ArrayList<PFD> subProjection = new ArrayList<>();
        for (PFD pfd: prj) {
            if(r.containsAll(pfd.x) && r.containsAll(pfd.y))
                subProjection.add(pfd);
        }

        return subProjection;
    }
    public static ArrayList<PFD> projectToR(ArrayList<String> r1, ArrayList<PFD> FDList,int certainty){
        ArrayList<PFD> projectionOfR = new ArrayList<>();
        ArrayList<String> allCombo = getAllCombo(r1);
        ArrayList<String> keyList = new ArrayList<>();
        for (String str : allCombo){
            ArrayList<String> strList = new ArrayList<>(Arrays.asList(str.split("")));
            ArrayList<String> closure = getClosureForAttr(strList, FDList, certainty);
            if(equal(r1,closure)){
                if(!isRedundantKey(keyList,strList)){
                    keyList.add(str);
                }
                else {
                    continue;
                }
            }
            for (String strInClosure : closure){
                PFD curPFD = new PFD(strList, new ArrayList<>(Arrays.asList(strInClosure.split(""))),certainty);
                if(r1.contains(strInClosure) && !str.contains(strInClosure) && isNotXRedundant(curPFD,projectionOfR))
                    projectionOfR.add(curPFD);
            }
        }
        return projectionOfR;
    }

//    public static ArrayList<PFD> projectToR2(ArrayList<String> r1, ArrayList<PFD> FDList,int certainty){
//        ArrayList<PFD> projectionOfR = new ArrayList<>();
//        ArrayList<String> allCombo = getAllCombo(r1);
//        ArrayList<String> comboClone = new ArrayList<>(allCombo);
//        for (String str : allCombo){
//            if(allCombo.contains(str)){
//                ArrayList<String> strList = new ArrayList<>(Arrays.asList(str.split("")));
//                ArrayList<String> closure = getClosureForAttr(strList, FDList, certainty);
//                if(equal(closure,r1)){
//                    removeSuperkey(comboClone,strList);
//                }
//                for (String strInClosure : closure){
//                    PFD curPFD = new PFD(strList, new ArrayList<>(Arrays.asList(strInClosure.split(""))),certainty);
//                    if(r1.contains(strInClosure) && !str.contains(strInClosure) && isNotXRedundant(curPFD,projectionOfR))
//                        projectionOfR.add(curPFD);
//                }
//            }
//        }
//        return projectionOfR;
//    }

    public static ArrayList<PFD> projectToR3(ArrayList<String> r1, ArrayList<PFD> FDList,int certainty){
        ArrayList<PFD> projectionOfR = new ArrayList<>();
        ArrayList<String> newR = new ArrayList<>();
        ArrayList<String> allCombo = new ArrayList<>();
        for (PFD pfd: FDList) {
            ArrayList<String> cutX = pfd.x;
            cutX.retainAll(r1);
            newR.add(String.join("",cutX));
        }
        allCombo = getAllCombo(newR);
        ArrayList<String> keyList = new ArrayList();
        for (String str : allCombo){
            ArrayList<String> strList = new ArrayList<>(Arrays.asList(str.split("")));
            ArrayList<String> closure = getClosureForAttr(strList, FDList, certainty);
//            if(equal(r1,closure)){
//                if(!isRedundantKey(keyList,strList)){
//                    keyList.add(str);
//                }
//                else {
//                    continue;
//                }
//            }
            for (String strInClosure : closure){
                PFD curPFD = new PFD(strList, new ArrayList<>(Arrays.asList(strInClosure.split(""))),certainty);
                if(r1.contains(strInClosure) && !str.contains(strInClosure) && isNotXRedundant(curPFD,projectionOfR))
                    projectionOfR.add(curPFD);
            }
        }
        return projectionOfR;
    }


    // remove superkey
    public static void removeSuperkey(ArrayList<String> comList,ArrayList<String> key){
        Iterator<String> it = comList.iterator();
        while (it.hasNext()){
            ArrayList<String> curStr = new ArrayList<>(Arrays.asList(it.next().split("")));
            if(curStr.containsAll(key))
                it.remove();
        }
    }
    // is superset of key
    public static boolean isRedundantKey(ArrayList<String> keyList, ArrayList<String> curX){
        Iterator<String> it = keyList.iterator();
        while (it.hasNext()){
            String[] curKeyArray = it.next().split("");
            ArrayList<String> curKeyList = new ArrayList<>(Arrays.asList(curKeyArray));
            if(curX.containsAll(curKeyList))
                return true;
        }

        return false;
    }

    public static String turnDeOutputToString(ArrayList<Pair<ArrayList<String>,ArrayList<PFD>>> s){
//        System.out.println("Size: " + S.size());
        String output = "";
        int index = 1;
        for (Pair<ArrayList<String>,ArrayList<PFD>> pair : s) {
            ArrayList<String> subR = pair.getKey();
            ArrayList<PFD> project = pair.getValue();
            String strOfProject = "";
            for (int i = 0; i < project.size() ; i++) {
                strOfProject += project.get(i).toStringWithoutCertainty();
                if (i != project.size() - 1)
                    strOfProject += ", ";
            }
            output += "R" + index + " = " + subR.toString() + "\n";
            output += "Project of R" + index + " = [" + strOfProject + "]\n";
            index++;
        }
        return output;
    }

    public static ArrayList<PFD> betaCut(ArrayList<PFD> originalFDList, int certainty){
        ArrayList<PFD> cutFDList = new ArrayList<>(originalFDList);
        Iterator<PFD> it = cutFDList.iterator();
        while (it.hasNext()){
            PFD pfd = it.next();
            if(pfd.certainty > certainty){
                it.remove();
//                System.out.println("remove" + pfd.toString());
            }
        }
        Iterator<PFD> it2 = cutFDList.iterator();
        while (it2.hasNext()){
            PFD pfd = it2.next();
            if(!isNotXRedundant(pfd,cutFDList)){
                it2.remove();
//                System.out.println("remove" + pfd.toString());
            }
        }
        return cutFDList;
    }

    public static boolean isNotXRedundant(PFD pfd, ArrayList<PFD> FDList){
        ArrayList<String> xList = new ArrayList<>();
        xList.addAll(pfd.x);
        for (PFD curPfd: FDList){
            ArrayList<String> xPrimeList = new ArrayList<>();
            xPrimeList.addAll(curPfd.x);
            if((contain(xList,xPrimeList) || (contain(xPrimeList,xList))) && equal(pfd.y,curPfd.y) && !curPfd.equals(pfd))
                return false;
        }
        return true;
    }

    public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
        Set<Set<T>> sets = new HashSet<Set<T>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<T>());
            return sets;
        }
        List<T> list = new ArrayList<T>(originalSet);
        T head = list.get(0);
        Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
        for (Set<T> set : powerSet(rest)) {
            Set<T> newSet = new HashSet<T>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }

    public static ArrayList<ArrayList<String>> powerSet2(ArrayList<String> R, ArrayList<ArrayList<String>> comb) {
        //ArrayList<ArrayList<String>> comb = new ArrayList<>();

        //System.out.println("R: "+R);
        if(R.size()!=0){//Recursive
            comb.add(R);
            for (String str : R) {
                ArrayList<String> list = new ArrayList<>();
                list.addAll(R);
                list.remove(str);
                if(!comb.contains(list)){
                    powerSet2(list, comb);
                }
            }
        }
        //System.out.println("R: "+R);
        //System.out.println("Comb: "+comb);
        return comb;
    }
    public static ArrayList<String> getAllCombo(ArrayList<String> r1){
        ArrayList<String> combo = new ArrayList<>();
        HashSet set = new HashSet(r1);

        Set<Set<String>> powerSet = powerSet(set);
        Iterator<Set<String>> it = powerSet.iterator();

        while (it.hasNext()){
            HashSet<String> tempSet = (HashSet<String>) it.next();
            if(!tempSet.isEmpty())
                combo.add(String.join("",tempSet));
        }
        combo.sort(Comparator.comparing(String::length));
        return combo;
    }
}
