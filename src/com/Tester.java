package com;

import java.io.File;
import java.util.ArrayList;
import java.util.ListIterator;

public class Tester {


    public static void testantiKey(ArrayList<String> R, ArrayList<PCK> Sig,int beta, ArrayList<ArrayList<PCK>> antiSig){

        ArrayList<PCK> CLsig = new ArrayList<>();

        ArrayList<ArrayList<String>> contexts = new ArrayList<>();
        contexts = getAllComb(R,contexts);


        for(ArrayList<String> c: contexts){
            ArrayList<ArrayList<String>> keys = new ArrayList<>();
            keys = getAllComb(c,keys);
            System.out.println(keys);
            for(ArrayList<String> k: keys){
                for(int i=1; i<beta;i++){
                    PCK pck = new PCK(c,k,i);
                    if (!Sig.contains(pck)){
                        CLsig.add(pck);
                    }
                }
            }
        }
        System.out.println("CL(Sig): "+CLsig);

        ArrayList<PCK> antisss = new ArrayList<>();
        ListIterator<PCK> clIt = CLsig.listIterator();

        while (clIt.hasNext()){
            PCK ck = clIt.next();

            if(!existSub(ck,CLsig)){
                antisss.add(ck);
            }
        }
        System.out.println("\n antisss(Sig): "+antisss);


    }
    private static boolean existSub(PCK ck, ArrayList<PCK> CLsig){
        for(PCK cpkp:CLsig){
            if(!cpkp.equals(ck)){
                if (ck.isSubsetOf(cpkp)){
                    return true;
                }else if(ck.getC().equals(cpkp.getC())&& ck.getK().equals(cpkp.getK())){
                    if(ck.getBeta() > cpkp.getBeta()){
                        return true;
                    }
                }
            }
        }

        return false;
    }



    public static ArrayList<ArrayList<String>> getAllComb(ArrayList<String> R, ArrayList<ArrayList<String>> comb) {
        //ArrayList<ArrayList<String>> comb = new ArrayList<>();

        //System.out.println("R: "+R);
        if (R.size() != 0) {//Recursive
            comb.add(R);
            for (String str : R) {
                ArrayList<String> list = new ArrayList<>();
                list.addAll(R);
                list.remove(str);
                if (!comb.contains(list)) {
                    getAllComb(list, comb);
                }
            }
        }
        //System.out.println("R: "+R);
        //System.out.println("Comb: "+comb);
        return comb;
    }

    public static void main(String[] args){


//        ArrayList<String> t = new ArrayList<>();
//        t.add("A");
//        t.add("B");
//        t.add("C");
//        ArrayList<String> t1 = new ArrayList<>();
//        t1.add("Z");
//        t1.add("R");
//        ArrayList<String> t2 = new ArrayList<>();
//        t2.add("Z");
//        t2.add("O");
//        ArrayList<String> t3 = new ArrayList<>();
//        t3.add("R");
//        t3.add("O");
//
//        PCK pck1 = new PCK(t,t1,1);
//        System.out.println(pck1.toString()+ "valid? "+ pck1.valid());

        //"C:\\Users\\lukyl\\OneDrive\\Documents\\GitHub\\Database_Experimental_Tool 1.0\\PFDs_2.txt"
        File file = new File("C://Users//lukyl//OneDrive//Documents//GitHub//Database_Experimental_Tool 1.0//PCKs.txt");
        MyConstraintReader consReader = new MyConstraintReader(file);
        ArrayList constraints = consReader.importedConstraints;
        System.out.println(constraints);


        ArmstrongPCK armstrong = new ArmstrongPCK(consReader.R,4, constraints);
        //ArrayList<PCK> antiCKlist = new ArrayList<>();
//        antiCKlist.add(new PCK(consReader.R,t1,0));
//        antiCKlist.add(new PCK(consReader.R,t2,0));
//        antiCKlist.add(new PCK(consReader.R,t3,0));


        //System.out.println(antiCKlist);
        //System.out.println("ANTI CK -- "+ armstrong.antiCK(constraints,1,antiCKlist));

        //armstrong.visualizePCK();


        testantiKey(armstrong.R,armstrong.PCKlist,4, armstrong.antiSigSet);
    }

}
