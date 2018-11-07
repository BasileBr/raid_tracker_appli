package com.application.sed.raid_tracker_appli;

import android.view.ViewDebug;
import android.widget.Button;

import java.awt.font.TextAttribute;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bdd {
    private static ArrayList<List> Liste =new ArrayList<>();
    private static ArrayList<List> ListeAccount =new ArrayList<>();
    private static ArrayList<Button> ListButton = new ArrayList<>();
    private static String TAG = "Bdd";
    private static String nomUtilisateur;
    private static String response;

    public ArrayList<List> getStringList(){
        return Liste;
    }


    public static List getElement(int index){
        int test = Liste.size();
        String bla = Integer.toString(test);
        Utils.info("EditText",bla);
        return Liste.get(index-1);
    }

    public static List getElement (String name){
        Utils.info("EditText","get Element String");
        return getElement(Liste.indexOf(name));
    }

    public static ArrayList getArrayList(){
        return Liste;
    }

    public static void addString (List myList){
        Utils.info("EditText","add String");

        Liste.add(myList);
    }


    /*Ajouter un utilisateur */
    public static void addAccount(List myListAccount){
        Utils.info("Check Value in my List","add Account");

        ListeAccount.add(myListAccount);
    }

    /* Recupérer les informations de l'utilisateur */
    public static ArrayList getAccount(){
        return ListeAccount;
    }

    /* Récupérer la liste qui contient l'utilisateur demandé*/
    public static List getAccountByName(String name){
        int intermediare = ListeAccount.indexOf(name);
        return ListeAccount.get(intermediare);
    }


    /** TODO
     * Non finie. Mais normalement non utile -> Utilisation de l'API + BDD
     * @param add
     */
    public static void updateAccount(ArrayList add){
        int intermediare = ListeAccount.indexOf(add.get(1));
    }



    /*Ajouter un nouveau bouton de parcours */
    public static void addButton(Button mybutton){
        Utils.info("Check Value in my List","add Account");
        ListButton.add(mybutton);
    }

    /* Recupérer la liste des boutons */
    public static ArrayList getButton(){
        return ListButton;
    }


    /*
    Connaitre l'utilisateur courant en fonction des sessions
     */
    public static void setNomUtilisateur(String nom){
        nomUtilisateur = nom;
    }

    public static String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public static void setResponse(String res){
        response = res;
    }

    public static String getResponse(){
        return response;
    }
}
