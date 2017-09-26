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
        ArrayList<PFD>  CanCover = new ArrayList<>();
        CanCover.addAll(FDList);
        for(PFD curPfd: FDList){
            ArrayList<String> z = new ArrayList<>();
            z.addAll(curPfd.x);
            //fresh copy of the underlying array for iteration
            ArrayList<String> iter = new ArrayList<>();
            iter.addAll(z);
            for(String b: iter){
                z.remove(b);
                ArrayList<String> closureArrayList = getClosureForAttr(z,CanCover,curPfd.certainty);
                System.out.println("x:"+z.toString()+ ", Closure:"+closureArrayList.toString());
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
        System.out.println("CanCover(before merge)--"+CanCover.toString());
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
	
	public static ArrayList<PFD> expand(ArrayList<PFD> FDList){
        ArrayList<PFD> expandedList = new ArrayList<>(FDList);
        ArrayList<PFD> newFDList = new ArrayList<>();
        Iterator<PFD> iterator = expandedList.iterator();
        while (iterator.hasNext()){
            PFD curPfd = iterator.next();
            if(curPfd.y.size() > 1){
                iterator.remove();
                for (String str1 : curPfd.y) {
                    ArrayList<String> listY = new ArrayList<>();
                    listY.add(str1);
                    PFD pfd = new PFD(curPfd.x,listY,curPfd.certainty);
                    newFDList.add(pfd);
                }
            }
        }
        expandedList.addAll(newFDList);
        return expandedList;
    }




    public static boolean isKey(ArrayList<String> key, ArrayList<PFD> Sigma,int certainty){
	    boolean iskey = false;
	    ArrayList<String> keyClosure = getClosureForAttr(key,Sigma,certainty);
//        System.out.println(key.toString()+"_Closure = "+ keyClosure);
	    if(equal(keyClosure,R(Sigma))){
            System.out.println(key + " is key!! ");
	        iskey = true;
        }else {
            System.out.println(key + " is not key ");
        }
	    return iskey;
    }

    private static ArrayList<String> R(ArrayList<PFD> Sigma){

        ArrayList<String> attributes = new ArrayList<>();
        for(PFD curPfd: Sigma){
            for(String x: curPfd.x){
                if(!attributes.contains(x)){
                    attributes.add(x);
                }
            }
            for(String y: curPfd.y){
                if(!attributes.contains(y)){
                    attributes.add(y);
                }
            }
        }
        return attributes;

    }

    public static ArrayList<Pair<ArrayList<String>,ArrayList<PFD>>> DecomposeWithTheCertainty(ArrayList<String> r, ArrayList<PFD> FDList,int certainty){
        ArrayList<Pair<ArrayList<String>,ArrayList<PFD>>> DecomposeRes = new ArrayList<>();
        ArrayList<PFD> FDClone = betaCut(FDList,certainty);

        if(isSatisfiedBDFN(FDList,certainty))
            DecomposeRes.add(new Pair(r,FDClone));
        else {
            Iterator<PFD> it = FDClone.iterator();
            PFD selectPfd = null;
            while (it.hasNext()){
                PFD pfd = it.next();
                if(!isKey(pfd.x, FDList, certainty)){
                    selectPfd = pfd;
                    FDClone.remove(selectPfd);
                    break;
                }
            }
            ArrayList R1 = new ArrayList();
            R1.addAll(selectPfd.x);
            for (String str: selectPfd.y) {
                if(!R1.contains(str))
                    R1.add(str);
            }

            ArrayList<PFD> projectionR1 = projectToR(R1,FDList,certainty);

            ArrayList R2 = new ArrayList();
            R2.addAll(selectPfd.x);
            ArrayList<String> rClone = new ArrayList<>(r);
            rClone.removeAll(R1);
            R2.addAll(rClone);

            ArrayList<PFD> projectionR2 = projectToR(R2,FDList,certainty);

            System.out.println("P2:" + R2.toString());
            System.out.println("Projection:" + projectionR2.toString());


            DecomposeRes.addAll(DecomposeWithTheCertainty(R1,projectionR1,certainty));
            DecomposeRes.addAll(DecomposeWithTheCertainty(R2,projectionR2,certainty));
        }
        return DecomposeRes;
    }

    public static boolean isSatisfiedBDFN(ArrayList<PFD> FDList,int certainty){
        for (PFD pfd: FDList) {
            if(pfd.certainty <= certainty){
                if(!isKey(pfd.x, FDList, certainty))
                    return false;
            }
        }
        return true;
    }
    
    public static ArrayList<PFD> projectToR(ArrayList<String> r1, ArrayList<PFD> FDList,int certainty){
        ArrayList<PFD> projectionOfR = new ArrayList<>();
        ArrayList<String> allCombo = getAllCombo(r1);

        for (String str : allCombo){
            ArrayList<String> closure = getClosureForAttr(new ArrayList<>(Arrays.asList(str.split(""))), FDList, certainty);
            for (String strInClosure : closure){
                if(r1.contains(strInClosure) && !str.contains(strInClosure))
                    projectionOfR.add(new PFD(new ArrayList<>(Arrays.asList(str.split(""))), new ArrayList<>(Arrays.asList(strInClosure.split(""))),certainty));
            }
        }
        return projectionOfR;
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
                System.out.println("remove" + pfd.toString());
            }
        }
        Iterator<PFD> it2 = cutFDList.iterator();
        while (it2.hasNext()){
            PFD pfd = it2.next();
            if(!isNotXRedundant(pfd,cutFDList)){
                it2.remove();
                System.out.println("remove" + pfd.toString());
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
            if(contain(xList,xPrimeList) && equal(pfd.y,curPfd.y) && !curPfd.equals(pfd))
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

        //combo=powerSet2(r1,combo);

        while (it.hasNext()){
            HashSet<String> tempSet = (HashSet<String>) it.next();
            if(!tempSet.isEmpty())
                combo.add(String.join("",tempSet));
        }
//        System.out.println(combo.toString());
        return combo;
    }
}
