package com;

import java.util.ArrayList;
import java.io.File;
import java.util.Iterator;
import java.util.ListIterator;
//import static com.MyConstraintReader.fileReader;

public class ArmstrongPCK {
    ArrayList<String> R;
    int certainty; // the k+1 of \beta_{k+1}
    ArrayList<PCK> PCKlist;
    ArrayList<ArrayList<PCK>> antiSigSet;

    public ArmstrongPCK(ArrayList<String> R, int certainty, ArrayList<PCK> PCKlist){
        this.R = R;
        this.certainty = certainty;
        this.PCKlist = PCKlist;
        this.antiSigSet = new ArrayList<>();

    }

    //////// LINK TO JDBC OR NOT??????? ///////



    // STORE in a two-dimensional array


    public ArrayList<ArrayList<String>> visualizePCK() {
        ArrayList<ArrayList<String>> rSig = new ArrayList<>();
        //Add the base

        antiSigSet.add(new ArrayList<>());

        for(int i=1; i< this.certainty; i++){
            System.out.println();
            ArrayList<PCK> Sig_i = new ArrayList<>();


            for (PCK pck: PCKlist){
                if(pck.getBeta()<=i){
                    Sig_i.add(pck);
                }
            }
            //System.out.println("Sig_i"+Sig_i);

            //System.out.println("antiSig"+antiSigSet);

            ArrayList<PCK> antiCK_i = antiCK(Sig_i,i,antiSigSet.get(i-1));
            ListIterator<PCK> antiIt = antiCK_i.listIterator();

            //for (PCK pck:antiCK_i){
            while(antiIt.hasNext()){
                PCK pck =antiIt.next();
                if(pck.getBeta()!=i){
                    antiIt.remove();
                }
            }
            antiSigSet.add(antiCK_i);

            System.out.println("AntiSig(i-"+i +"):   "+ antiSigSet);

        }



        //Assign tuple t_0
        ArrayList<String> t_0 = new ArrayList<>();
        for(String a: R){

            t_0.add("0      ");//"c_{"+a+",0}"

        }
        t_0.add("\u03B1_1");
        //System.out.println(t_0);
        rSig.add(t_0);

        int j = 0;
        //ArrayList<String> r = t_0;
        ArrayList<PCK> Sig_0 = new ArrayList<>();//Sig_0 <- \emptyset
        for (int i = this.certainty-1; i > 0; i--){
            ArrayList<PCK> anti_i = antiSigSet.get(i);
            //
//            for(PCK pck: anti_i){
//                if(pck.getBeta()!=i){
//                    pck.setBeta(i);
//                }
//            }
            //System.out.println("$$$$ anti_"+i+": "+anti_i);

            anti_i.removeAll(Sig_0);
            //System.out.println("anti_i-Sig_0"+anti_i);
            for (PCK pck: anti_i){
                j++;
                ArrayList<String> t_j = new ArrayList<>();

                // New tuple with agree set ((C,K),\beta_i)
                for (String a:R){
                    if(pck.getK().contains(a)){
                        t_j.add("0      ");//"c_{"+a+",0}"
                    }else if(pck.get_CslashK().contains(a)){
                        t_j.add(j+"      ");//"c_{"+a+",0}"
                    }else if (pck.get_RslashC(this.R).contains(a)){
                        t_j.add("\u22A5");
                    }

                }
                int pos = this.certainty-i;//p-degree: \alpha_{k+1-i}
                t_j.add("\u03B1_"+pos);
                rSig.add(t_j);

                System.out.println(antiSigSet+"\n"+"\n");//+rSig
            }
            Sig_0.addAll(anti_i);//Get Maximal Set


            //System.out.println("%%%%%%%%%%%"+antiSigSet);
        }
        return rSig;
    }

    // Compute the antiCK set for a certain Possibility
    public ArrayList<PCK> antiCK(ArrayList<PCK> CKlist, int beta, ArrayList<PCK> LASTantiCKlist){
        //System.out.println("Sig_i"+CKlist);

        //System.out.println("antiSig"+antiCKlist);

        PCK rr = new PCK(R,R,beta);
        CKlist.add(rr);
        ArrayList<PCK> CKlistTEMP = new ArrayList<>();


        ArrayList<PCK> antiCKlist = new ArrayList<>();
        antiCKlist.addAll(LASTantiCKlist);

        //reset this beta
//        for (PCK pck:antiCKlist){
//            if(pck.getBeta()!=beta-1){
//                pck.setBeta(beta-1);
//            }
//        }


        antiCKlist.add(rr);


        for(PCK ck:CKlist){
            CKlistTEMP.add(ck);

            ListIterator<PCK> uxIT = antiCKlist.listIterator();




            while(uxIT.hasNext()){
            //for(PCK ux: antiCKlist){
                PCK ux = uxIT.next();




                if(ck.isSubsetOf(ux)){
                    //System.out.println("--------- remove "+ux);
                    uxIT.remove();

                    // A \in C\K
                    for(String A_1:ck.get_CslashK()){

                        ArrayList<String> U_1 = new ArrayList<>(ux.getC());
                        U_1.remove(A_1);
                        ArrayList<String> X_1 = new ArrayList<>(ux.getK());
                        X_1.remove(A_1);
                        //System.out.println("add _1" + new PCK(U_1,X_1,beta));

                        uxIT.add(new PCK(U_1,X_1,beta));

                    }
                    ///System.out.println("--------- ux "+ux+ antiCKlist);

                    // A \in K
                    for (String A_2: ck.getK()){
                        //System.out.println("A_2 is "+A_2);
                        ArrayList<String> U_2 = new ArrayList<>(ux.getC());
                        ArrayList<String> X_2 = new ArrayList<>(ux.getK());
                        X_2.remove(A_2);
                        //System.out.println("（RO,RO）here?   "+ ux);
                        PCK nPCK = new PCK(U_2,X_2,beta);
                        if(nPCK.isSubsetOf(ux)){
                            uxIT.add(new PCK(U_2,X_2,beta));
                            //System.out.println("-----add "+nPCK);
                        }

                    }

                }

            }


            while(uxIT.hasPrevious()){
            //for (PCK ux: antiCKlist){
                PCK ux = uxIT.previous();

                if(deleteUX(ux,CKlistTEMP,beta,antiCKlist )){ //|| ux.getK().isEmpty() || ux.getBeta()==0

                    uxIT.remove();
                }



            }

        }
//


        return antiCKlist;
    }

    private boolean deleteUX(PCK ux,ArrayList<PCK> CKlistTEMP , int beta,ArrayList<PCK> antis){

        ArrayList<String> A_context = ux.get_CslashK();
        ArrayList<String> A_noncontext = ux.get_RslashC(this.R);

        for(PCK anti: antis){
            if(!anti.equals(ux)){
                if(ux.isSubsetOf(anti)){

                    return true;
                }
            }
        }
        for(PCK ckTEMP:CKlistTEMP){
            ArrayList<String> newX = new ArrayList<>(ux.getK());
            newX.addAll(A_context);
            PCK uxTEMP_1 = new PCK(ux.getC(), newX,beta);


            ArrayList<String> newU = new ArrayList<>(ux.getK());
            newU.addAll(A_noncontext);
            PCK uxTEMP_2 = new PCK(newU,ux.getK(),beta);

            //System.out.println("            ux:"+ux+ "   A in U\\X"+A_context+", A in R\\U"+A_noncontext);

            //System.out.println("            "+ckTEMP+ "subset of ?  "+uxTEMP_1+" OR "+uxTEMP_2);

            if(ckTEMP.isSubsetOf(uxTEMP_1) || ckTEMP.isSubsetOf(uxTEMP_2)){
                return false;
            }
        }



        return true;
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

        armstrong.visualizePCK();
    }




}
