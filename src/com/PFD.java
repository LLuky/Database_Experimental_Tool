package com;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.Functions.*;
import static com.ThirdNF_Decomposition.*;


public class PFD {
    ArrayList<String> x;
    ArrayList<String> y;
    int certainty;
    //boolean isKey;

    public PFD(ArrayList<String> x, ArrayList<String> y, int certainty) {
        this.x = x;
        this.y = y;
        this.certainty = certainty;
        //this.isKey = false;
    }

    @Override
    public String toString() {
        return "("+getStrFromArrayList(x) + "\u2192" + getStrFromArrayList(y) + "," + "\u00DF" + certainty+")";
    }
    public String toStringWithoutCertainty() {
        return getStrFromArrayList(x) + "\u2192" + getStrFromArrayList(y);
    }

    public String getStrFromArrayList(ArrayList<String> arrayList){
        String output = "";
        for (String str: arrayList) {
            output += str;
        }
        return output;
    }

    public static ArrayList<ArrayList<String>> getAllComb(ArrayList<String> R, ArrayList<ArrayList<String>> comb) {
        //ArrayList<ArrayList<String>> comb = new ArrayList<>();

        //System.out.println("R: "+R);
        if(R.size()!=0){//Recursive
            comb.add(R);
            for (String str : R) {
                ArrayList<String> list = new ArrayList<>();
                list.addAll(R);
                list.remove(str);
                if(!comb.contains(list)){
                    getAllComb(list, comb);
                }
            }
        }
        //System.out.println("R: "+R);
        //System.out.println("Comb: "+comb);
        return comb;
    }

    public static void main(String[] args){
//        ArrayList<String> test = new ArrayList<>();
//        test.add("B");
//
//        ArrayList<String> test2 = new ArrayList<>();
//        test2.add("D");
//        test2.add("F");
//
//        PFD pfd1 = new PFD(test,test2,2);
//
//        ArrayList<String> test3 = new ArrayList<>();
//        test3.add("B");
//
//        ArrayList<String> test4 = new ArrayList<>();
//        test4.add("E");
//
//        PFD pfd2 = new PFD(test3,test4,1);
//
//
//        ArrayList<String> test5 = new ArrayList<>();
//        test5.add("C");
//
//        ArrayList<String> test6 = new ArrayList<>();
//        test6.add("B");
//        test6.add("D");
//
//        PFD pfd3 = new PFD(test5,test6,2);
//
//        ArrayList<String> test7 = new ArrayList<>();
//        test7.add("C");
//
//        ArrayList<String> test8 = new ArrayList<>();
//        test8.add("F");
//
//        PFD pfd4 = new PFD(test7,test8,1);
//
//
//
//        ArrayList<String> test9 = new ArrayList<>();
//        test9.add("E");
//
//        ArrayList<String> test10 = new ArrayList<>();
//        test10.add("D");
//
//        PFD pfd5 = new PFD(test9,test10,2);
//
//
//
//        ArrayList<String> test11 = new ArrayList<>();
//        test11.add("E");
//        ArrayList<String> test12 = new ArrayList<>();
//        test12.add("F");
//        PFD pfd6 = new PFD(test11,test12,2);
//
//
//        ArrayList<String> test13 = new ArrayList<>();
//        test13.add("C");
//        test13.add("F");
//        ArrayList<String> test14 = new ArrayList<>();
//        test14.add("B");
//        PFD pfd7 = new PFD(test13,test14,1);
//
//
//        ArrayList<String> test15 = new ArrayList<>();
//        test15.add("C");
//        test15.add("F");
//        ArrayList<String> test16 = new ArrayList<>();
//        test16.add("E");
//        PFD pfd8 = new PFD(test15,test16,1);
//
//
//
//        ArrayList<PFD> pfdArrayList = new ArrayList<>();
//        pfdArrayList.add(pfd1);
//        pfdArrayList.add(pfd2);
//        pfdArrayList.add(pfd3);
//        pfdArrayList.add(pfd4);
//        pfdArrayList.add(pfd5);
//        pfdArrayList.add(pfd6);
//        pfdArrayList.add(pfd7);
//        pfdArrayList.add(pfd8);
//
//        ArrayList<String> target = new ArrayList<>();
//        //target.add("B");
//        target.add("E");
//        target.add("C");
//        System.out.println(pfdArrayList.toString());
//        //getClosureForAttr(target,pfdArrayList,1);
//        //NRcover(pfdArrayList);
//        getCanCover2(pfdArrayList);
//        isKey(target,pfdArrayList,2);
//        System.out.println(isSatisfiedBDFN(pfdArrayList,2));


        //test by tommy
//        ArrayList<String> test = new ArrayList<>();
//        test.add("M");
//        test.add("T");
//
//        ArrayList<String> test2 = new ArrayList<>();
//        test2.add("R");
//
//        PFD pfd1 = new PFD(test,test2,1);
//
//        ArrayList<String> test3 = new ArrayList<>();
//        test3.add("R");
//        test3.add("T");
//
//        ArrayList<String> test4 = new ArrayList<>();
//        test4.add("P");
//
//        PFD pfd2 = new PFD(test3,test4,2);
//
//        ArrayList<String> test5 = new ArrayList<>();
//        test5.add("P");
//        test5.add("T");
//
//        ArrayList<String> test6 = new ArrayList<>();
//        test6.add("M");
//
//        PFD pfd3 = new PFD(test5,test6,3);
//
//        ArrayList<String> test7 = new ArrayList<>();
//        test7.add("P");
//
//        ArrayList<String> test8 = new ArrayList<>();
//        test8.add("M");
//
//        PFD pfd4 = new PFD(test7,test8,4);
//
//        ArrayList<PFD> test2List = new ArrayList<PFD>(Arrays.asList(pfd1, pfd2, pfd3, pfd4));
//        ArrayList<String> r = new ArrayList<String>(Arrays.asList("M", "R", "T", "P"));

        ArrayList<String> test = new ArrayList<>();
        test.add("T");
        test.add("L");

        ArrayList<String> test2 = new ArrayList<>();
        test2.add("P");

        PFD pfd1 = new PFD(test,test2,1);

        ArrayList<String> test3 = new ArrayList<>();
        test3.add("T");
        test3.add("L");

        ArrayList<String> test4 = new ArrayList<>();
        test4.add("M");

        PFD pfd2 = new PFD(test3,test4,1);

        ArrayList<String> test5 = new ArrayList<>();
        test5.add("P");
        ArrayList<String> test6 = new ArrayList<>();
        test6.add("M");

        ArrayList<String> test7 = new ArrayList<>();
        test7.add("T");
        ArrayList<String> test8 = new ArrayList<>();
        test8.add("S");

        PFD pfd3 = new PFD(test5,test6,3);
        PFD pfd4 = new PFD(test7,test8,3);
        ArrayList<PFD> test2List = new ArrayList<PFD>(Arrays.asList(pfd1, pfd2));
        ArrayList<String> r = new ArrayList<String>(Arrays.asList("M", "S", "T", "P"));
//        System.out.println(test2List);
//        System.out.println("Closure: " + getClosureForAttr(new ArrayList<String>(Arrays.asList("T","M")),test2List,1));
//        System.out.println("BDNF? "+ isSatisfiedBDFN(test2List,3));
//        System.out.println(getCanCover1(test2List));
//        S = DecomposeWithTheCertainty(r,test2List,4);
//        System.out.println(turnDeOutputToString());
//        System.out.println(getAllComb(r, new ArrayList<ArrayList<String>>()));

//        System.out.println(getMinimalKeys(test2List,r,4));

//        System.out.println("B-prime: "+getBetaPrimeList(test2List,r,2));
//        System.out.println("Satisfied 3NF? "+isSatisfied3NF(test2List,r,3));
//        System.out.println("Is not subset:"+isNotSubset(pfd4,test2List));
        ArrayList<String> t = new ArrayList<>();
        t.add("A");
        t.add("B");
        t.add("C");
        ArrayList<String> t1 = new ArrayList<>();
        t1.add("B");
        t1.add("A");
//        t1.add("D");
        t.retainAll(t1);
        System.out.println(t.toString());
        System.out.println(String.join("",t1));

        System.out.println(getAllCombo(r).toString());

}




}
