package com;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PCK {
    private ArrayList<String> C; //Context
    private ArrayList<String> K; //Key
    private int beta; //Possibility

    public PCK(ArrayList<String> C, ArrayList<String> K, int beta) {
        this.C = removeDuplicates(C);
        this.K = removeDuplicates(K);
        this.beta = beta;
    }

    // CK - with no possiblility shown
//    public PCK(ArrayList<String> C, ArrayList<String> K) {
//        this.C = C;
//        this.K = K;
//        this.beta = 0;
//    }


    @Override
    public String toString() {
        if (this.beta == 0) {
            //return "(" + getStrFromArrayList(C) + "," + getStrFromArrayList(K) + ")";
        }
        return "((" + getStrFromArrayList(C) + "," + getStrFromArrayList(K) + ")," + "\u00DF" + beta + ")";
    }

    public String toStringWithoutCertainty() {
        return getStrFromArrayList(C) + "," + getStrFromArrayList(K);
    }

    public String getStrFromArrayList(ArrayList<String> arrayList) {
        String output = "";
        for (String str : arrayList) {
            output += str;
        }
        return output;
    }

    public boolean valid() {
        if(C.isEmpty()){
            return false;
        }


        for (String k : K) {
            if (!C.contains(k)) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<String> removeDuplicates(ArrayList<String> Dup){
        Set<String> hs = new HashSet<>();
        hs.addAll(Dup);
        Dup.clear();
        Dup.addAll(hs);
        return Dup;
    }

    public boolean isSubsetOf(PCK ux) {

        if (!ux.valid() || !this.valid() || this.beta!=ux.getBeta()){
            return false;
        }
        // C < U
        for (String c : C) {
            if (!ux.C.contains(c)) {
                return false;
            }
        }

        // K < X
        for (String k : K) {
            if (!ux.K.contains(k)) {
                return false;
            }
        }

        //System.out.println("(C,K) < (U,X)  "+this+"<" + ux);
        return true;

    }

    public ArrayList<String> getC() {
        return C;
    }

    public ArrayList<String> getK() {
        return K;
    }

    public int getBeta() {
        return beta;
    }

    public void setBeta(int beta) {
        this.beta = beta;
    }

    public ArrayList<String> get_CslashK() {
        ArrayList<String> CslashK = new ArrayList<String>();
        for (String c : C) {
            if (!K.contains(c)) {
                CslashK.add(c);
            }
        }

        return CslashK;
    }


    public ArrayList<String> get_RslashC(ArrayList<String> R) {
        ArrayList<String> RslashC = new ArrayList<String>();
        for (String r : R) {
            if (!C.contains(r)) {
                RslashC.add(r);
            }
        }

        return RslashC;
    }

    public void addC(String c) {
        C.add(c);
    }

    public void addK(String k) {
        K.add(k);
    }



}
