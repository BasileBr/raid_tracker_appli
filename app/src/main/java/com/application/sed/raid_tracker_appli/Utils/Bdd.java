package com.application.sed.raid_tracker_appli.Utils;

import android.widget.Button;

import com.android.volley.toolbox.StringRequest;
import com.application.sed.raid_tracker_appli.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Bdd {
    private static ArrayList<List> Liste =new ArrayList<>();
    private static ArrayList<List> ListeAccount =new ArrayList<>();
    private static ArrayList<Button> ListButton = new ArrayList<>();
    private static ArrayList<String> listIdRaid = new ArrayList<>();
    private static ArrayList<String> listIdPoste = new ArrayList<>();
    private static String TAG = "Bdd";
    private static String nomUtilisateur;
    private static String response;
    private static String value,id, userid;
    private static String listFromApi;
    private static String idRaid;

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

    public static void setListIdRaid(ArrayList<String> listidraid){
        listIdRaid =listidraid;
    }
    public static ArrayList<String> getlistIdRaid(){
        return listIdRaid;
    }

    public static void setListIdPoste(ArrayList<String> listidposte){
        listIdPoste =listIdPoste;
    }
    public static ArrayList<String> getListIdPoste(){
        return listIdPoste;
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

    public static void setApiNomUtilisateur(String nom){
        nomUtilisateur = nom;
    }




    /**
     * APÏ Methods
     * @param id
     */


    public static void setUserid (String id){
        userid = id;
    }
    public static String getUserid(){
        return userid;
    }


    public static void setValue (String val, String i){
        value = val;
        id = i;
    }

    public static String getValue(){
        return value;
    }
    public static String getId() {
        return id;
    }

    public static void setListFromApi( String string){
        listFromApi = string;
    }
    public static String getListFromApi(){
        return listFromApi;
    }


}
