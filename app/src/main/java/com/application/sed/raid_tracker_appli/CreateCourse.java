package com.application.sed.raid_tracker_appli;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.application.sed.raid_tracker_appli.organizer.CourseActivity;
import com.application.sed.raid_tracker_appli.organizer.NewraidActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CreateCourse extends AppCompatActivity {
    private String TAG = "CreateCourse";
    private TextView mDisplayDate;
    private String getdate = "";
    private DrawerLayout drawerLayout;

    private Toolbar toolbar2;
    View button;
    private ActionBarDrawerToggle drawerToggle;

    private CheckBox getitem;
    private CheckBox getitem2;
    private  CheckBox getitem3;
    private  CheckBox getitem4;
    private CheckBox getitem5;
    private CheckBox getitem6;



    private DatePickerDialog.OnDateSetListener mDateSetListener;



    public static List myListe;

    String charKayak = "";
    String charNatation= "";
    String charVelo = "";
    String charCourse = "";
    String charWater= "";
    String charTerre = "";


    LinearLayout getLinear;
    String recupere;
    String recupere1;
    String recupere2;
    String recupere3;
    CharSequence recupere4;
    CharSequence recupere5;
    CharSequence recupere6;
    CharSequence recupere7;
    CharSequence recupere8;
    CharSequence recupere9;

    Button mButton;
    EditText name_raid;
    EditText lieu;
    EditText organizer_team;
    EditText edition;

    TextView getsports;
    TextView selectdate;
    TextView choosesports;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createcourse);
        Utils.info(TAG, "OnCreate");

//        this.toolbar2 = findViewById(R.id.toolbar3);
        toolbar2 = findViewById(R.id.toolbar3);


        //definir notre toolbar en tant qu'actionBar
        setSupportActionBar(toolbar2);

        //afficher le bouton retour
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateCourse.this, LandingActivity.class);
                startActivity(intent);
            }
        });




//        this.drawerLayout = findViewById(R.id.drawerLayout3);
//        this.drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, 0, 0);
//        this.drawerLayout.setDrawerListener(this.drawerToggle);
//
//        toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utils.info("NICK", "button button button..................");
//
//                drawerLayout.openDrawer(Gravity.START);
//
//            }
//        });


        /**
         * Selectionner plusieurs Sports
         */
//        Spinner mySpinner=(Spinner)findViewById(R.id.spinner);
//
//        ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(CreateCourse.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.sports));
//        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mySpinner.setAdapter(myAdapter);
//
//
        mDisplayDate = (TextView) findViewById(R.id.selectdate);


        /**
         * Affichage pour sélectionner la date du RAID
         */

        Date today = Calendar.getInstance().getTime();


        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateCourse.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        /**
         * Récupérer la date, l'afficher et la stocker
         */
        final int tmp;
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectdate= (TextView) findViewById(R.id.selectdate);
                Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

                String date = dayOfMonth + "/" + (month+1) + "/" + year;
                mDisplayDate.setText(date);
                getdate = date;
                selectdate.setError(null);
            }
        };


//
//        //button de validation
//        mButton = (Button) findViewById(R.id.createAccount);
//
//        //nom du raid
//        name_raid = (EditText) findViewById(R.id.name_raid);
//
//        //lieu du raid
//        lieu = (EditText) findViewById(R.id.lieu);
//
//        //année de l'édition
//        edition=(EditText)findViewById(R.id.edition);
//
//        //nom de l'équipe organisatrice
//        organizer_team = (EditText) findViewById(R.id.organizer_team);
//
//
//
//        getitem = (CheckBox) findViewById(R.id.checkbox_kayak);
//        getitem2 = (CheckBox) findViewById(R.id.checkbox_natation);
//        getitem3 = (CheckBox) findViewById(R.id.checkbox_coursepied);
//        getitem4 = (CheckBox) findViewById(R.id.checkbox_velo);
//
//        getitem5=(CheckBox) findViewById(R.id.checkbox_water);
//        getitem6 = (CheckBox) findViewById(R.id.checkbox_terre);
//        getLinear = (LinearLayout) findViewById(R.id.checkbox);
//
//
//
//
//
//        mButton.setOnClickListener(
//                new View.OnClickListener() {
//                    public void onClick(View view) {
//
//
//                        int checknameraid=1;
//                        int checklieu=1;
//                        int checkdate=1;
//                        int checkedition=1;
//                        int checkteamname=1;
//                        int checksports=1;
//                        int checksurface;
//
//                        if (isEmpty(name_raid)) {
//                            checknameraid=0;
//                            name_raid.setError("le prénom n'est pas renseigné");
//                        }
//
//
//
//
//                        myListe =  new ArrayList<>();
//                        recupere = name_raid.getText().toString();
//                        recupere1 = lieu.getText().toString();
//                        recupere2 = organizer_team.getText().toString();
//
//                        myListe.add(recupere);  // récupère le nom du raid
//                        myListe.add(recupere1); // le lieu de l'évènement
//                        myListe.add(getdate); // sélectionne la date de l'évènement
//                        myListe.add(recupere2); // le nom de l'équipe organisatrice
//                        myListe.add(charSequence); //
//                        myListe.add(charSequence2);
//
//
//                        Utils.info("EditText", myListe.toString());
//                        //Utils.info(TAG,recupere[1]);
//                        Bdd.addString(myListe);
//
//                        LandingActivity.recupereraid();
//                        Utils.info("EditText", Bdd.getElement(1).toString());
//
//                        //déclencher la création d'un parcours sur la page de landing
////                        Intent intent = new Intent(CreateCourse.this, LandingActivity.class);
////                        intent.putExtra("name","Toto");
////                        startActivity(intent);
//
//                        //redirection vers la page de création d'un parcours
////                        Intent intent2= new Intent(CreateCourse.this, CreateParcours.class);
////                        startActivity(intent2);
//
//
//
//                    }
//                });


    }

    public void onCheckboxClicked(View view) {

        getitem = (CheckBox) findViewById(R.id.checkbox_kayak);

        //kayak selectionné ?
        getitem = (CheckBox) findViewById(R.id.checkbox_kayak);

        //natation selectionné ? ?
        getitem2 = (CheckBox) findViewById(R.id.checkbox_natation);

        //courseapied selectionné ?
        getitem3 = (CheckBox) findViewById(R.id.checkbox_coursepied);

        //velo selectionné ?
        getitem4 = (CheckBox) findViewById(R.id.checkbox_velo);

        //surface eau selectionné ?
        getitem5=(CheckBox) findViewById(R.id.checkbox_water);

        //surface terre selectionné ?

        getitem6 = (CheckBox) findViewById(R.id.checkbox_terre);
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        choosesports = (TextView) findViewById(R.id.choosesport);

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.checkbox_kayak:
                if (checked)
                    charKayak = getitem.getText().toString();
                choosesports.setError(null);
                Utils.info("charkayal", charKayak);
//                    int toto = getLinear.getId();
//                    Utils.info(TAG,String.valueOf(toto));

                break;
            case R.id.checkbox_natation:
                if (checked)
                    choosesports.setError(null);
                charNatation = getitem2.getText().toString();


                break;
            case R.id.checkbox_velo:
                if (checked)
                    choosesports.setError(null);
                charVelo = getitem3.getText().toString();


                break;
            case R.id.checkbox_coursepied:
                if (checked)
                    choosesports.setError(null);
                charCourse = getitem4.getText().toString();


                break;

            case R.id.checkbox_water:
                if (checked)
                    charWater = getitem5.getText().toString();


                break;
            case R.id.checkbox_terre:
                if (checked)
                    charTerre = getitem6.getText().toString();


                break;

            //vérifie si la surface correspond au sport
        }



        if ((getitem.isChecked() || getitem2.isChecked()) && (getitem5.isChecked() && !getitem6.isChecked())) {
            choosesports.setError(null);
        }

        if ((getitem3.isChecked() || getitem4.isChecked()) && (getitem6.isChecked() && !getitem5.isChecked())) {
            choosesports.setError(null);
        }
        if ((getitem.isChecked() || getitem2.isChecked() || getitem3.isChecked() || getitem4.isChecked()) && (getitem5.isChecked() && getitem6.isChecked()) ) {
            choosesports.setError(null);
        }

 }


    //vérifie qu'un élement editext n'est pas vide
    boolean isEmpty (EditText text){
        CharSequence str= text.getText().toString();
        return TextUtils.isEmpty(str);
    }


    boolean isEmpty2 (TextView text){
        CharSequence str= text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    /**
     * Permet de retourner à la vue "landingActivity
     */

    public void cancel(View view) {
        Intent intent = new Intent(CreateCourse.this, LandingActivity.class);
        startActivity(intent);
    }




    public void createnewraid(View view){


        /**
         * récupération de tous les infos lors de la validation d'un compte
         */
        //button de validation
        mButton = (Button) findViewById(R.id.createAccount);

        //nom du raid
        name_raid = (EditText) findViewById(R.id.name_raid);

        //lieu du raid
        lieu = (EditText) findViewById(R.id.lieu);

        //année de l'édition
        edition=(EditText)findViewById(R.id.edition);

        //nom de l'équipe organisatrice
        organizer_team = (EditText) findViewById(R.id.organizer_team);

        getitem = (CheckBox) findViewById(R.id.checkbox_kayak);

        //kayak selectionné ?
        getitem = (CheckBox) findViewById(R.id.checkbox_kayak);

        //natation selectionné ? ?
        getitem2 = (CheckBox) findViewById(R.id.checkbox_natation);

        //courseapied selectionné ?
        getitem3 = (CheckBox) findViewById(R.id.checkbox_coursepied);

        //velo selectionné ?
        getitem4 = (CheckBox) findViewById(R.id.checkbox_velo);

        //surface eau selectionné ?
        getitem5=(CheckBox) findViewById(R.id.checkbox_water);

        //surface terre selectionné ?

        getitem6 = (CheckBox) findViewById(R.id.checkbox_terre);


        //nécessaire ?
        getLinear = (LinearLayout) findViewById(R.id.checkbox);

        //afficher l'erreur sur la ligne selectionner les sports

        choosesports = (TextView) findViewById(R.id.choosesport);

        //afficher l'erreur sur la ligne selectdate

        selectdate= (TextView) findViewById(R.id.selectdate);



        //initation de variables par vérifier la complétude des champs
        int checknameraid=1;
        int checklieu=1;
        int checkdate=1;
        int checkedition=1;
        int checkteamname=1;
        int checkkayak=1;
        int checknatation=1;
        int checkvelo=1;
        int checkcourse=1;
        int checkwater=1;
        int checkterre=1;


        int checkcoherence=0;


        if (isEmpty(name_raid)) {
            checknameraid=0;
            name_raid.setError("le nom du raid n'est pas renseigné");
            }
        if (isEmpty(lieu)) {
            checklieu=0;
            lieu.setError("le nom du lieu n'est pas renseigné");
        }

        if (isEmpty2(selectdate)) {
            checkdate=0;
            selectdate.setError("la date n'est pas sélectionnée");
        }
        if (isEmpty(edition)) {
            checkedition=0;
            edition.setError("l'édition du raid n'est pas renseigné");
        }
        if (isEmpty(organizer_team)) {
            checkteamname=0;
            organizer_team.setError("le nom de l'équipe n'est pas renseigné");
        }

        if (isEmpty(name_raid)) {
            checknameraid=0;
            name_raid.setError("le nom du raid n'est pas renseigné");
        }

//        if(!getitem.isChecked()){
//            checkkayak=0;
//        }
//
//        if(!getitem2.isChecked()){
//            checknatation=0;
//        }
//        if(!getitem3.isChecked()){
//            checkvelo=0;
//        }
//        if(!getitem4.isChecked()){
//            checkcourse=0;
//        }
//        if(!getitem5.isChecked()){
//            checkwater=0;
//        }
//
//        if(!getitem6.isChecked()){
//            checkterre=0;
//        }


        //vérifie si un sport est selectionné
        if(!getitem.isChecked()&& !getitem2.isChecked()&& !getitem3.isChecked() && !getitem4.isChecked()){
            choosesports.setError("aucun sport selectionné");

        }

        //vérifie si la surface correspond au sport
//        if((getitem.isChecked() && !getitem5.isChecked() || getitem2.isChecked() && !getitem5.isChecked() || getitem.isChecked() && getitem2.isChecked() && !getitem5.isChecked())){
//            choosesports.setError("type de surface incohérent");
//        }

        if ((getitem.isChecked() || getitem2.isChecked()) && (!getitem5.isChecked() || getitem6.isChecked()) ) {
            choosesports.setError("type de surface incohérent");
        }

        //pareil vérif de la surface
//        if((getitem3.isChecked() && !getitem6.isChecked() || getitem4.isChecked() && !getitem6.isChecked() || getitem3.isChecked() && getitem4.isChecked() && !getitem6.isChecked())){
//            choosesports.setError("type de surface incohérent");
//        }

        if ((getitem3.isChecked() || getitem4.isChecked()) && (!getitem6.isChecked() || getitem5.isChecked())) {
            choosesports.setError("type de surface incohérent");
        }
        if ((getitem.isChecked() || getitem2.isChecked() || getitem3.isChecked() || getitem4.isChecked()) && (getitem5.isChecked() && getitem6.isChecked()) ) {
            choosesports.setError(null);
        }

        //vérification complète de la cohérence du/ des sports selectionnées en fonction de la surface
        if(getitem.isChecked() && getitem5.isChecked() || getitem2.isChecked() && getitem5.isChecked() || getitem.isChecked() && getitem2.isChecked() && getitem5.isChecked() ||
                getitem3.isChecked() && getitem6.isChecked() || getitem4.isChecked() && getitem6.isChecked() || getitem3.isChecked() && getitem4.isChecked() && getitem6.isChecked() ||
                getitem.isChecked() && getitem5.isChecked() && getitem3.isChecked() && getitem6.isChecked() || getitem.isChecked() &&getitem5.isChecked() && getitem4.isChecked() && getitem6.isChecked()
                || getitem2.isChecked() && getitem5.isChecked() && getitem3.isChecked() &&getitem6.isChecked() || getitem2.isChecked() && getitem5.isChecked() && getitem4.isChecked()&& getitem6.isChecked()
                || getitem.isChecked() && getitem2.isChecked() && getitem3.isChecked() && getitem5.isChecked() && getitem6.isChecked() || getitem2.isChecked() && getitem3.isChecked() && getitem4.isChecked()
                && getitem5.isChecked() && getitem6.isChecked() || getitem.isChecked() && getitem3.isChecked() && getitem4.isChecked() && getitem5.isChecked() && getitem6.isChecked()
                || getitem.isChecked() &&getitem2.isChecked() && getitem3.isChecked() && getitem4.isChecked() && getitem5.isChecked() && getitem6.isChecked()){

            Utils.info("coherent","oui");
            checkcoherence=1;
        }


        if(checknameraid==1 && checklieu ==1 && checkdate==1 && checkedition ==1 && checkteamname ==1 && checkcoherence==1){

            myListe =  new ArrayList<>();
            recupere = name_raid.getText().toString();
            recupere1 = lieu.getText().toString();
            recupere2= edition.getText().toString();
            recupere3 = organizer_team.getText().toString();
            recupere4= getitem.getText().toString();
            recupere5=getitem2.getText().toString();
            recupere6=getitem3.getText().toString();
            recupere7=getitem4.getText().toString();
            recupere8=getitem5.getText().toString();
            recupere9=getitem6.getText().toString();


            myListe.add(recupere);  // récupère le nom du raid
            myListe.add(recupere1); // le lieu de l'évènement
            myListe.add(getdate); // sélectionne la date de l'évènement


            myListe.add(recupere2); // le nom de l'équipe organisatrice
            myListe.add(recupere3);
            myListe.add(recupere4);
            myListe.add(recupere4);
            myListe.add(recupere5);
            myListe.add(recupere6);
            myListe.add(recupere7);
            myListe.add(recupere8);
            myListe.add(recupere9);

//trop long
//            if(getitem.isChecked() && getitem5.isChecked()){
//                myListe.add(recupere4);
//                myListe.add (recupere7);
//            }else if(getitem.isChecked() && getitem2.isChecked() && getitem5.isChecked()){
//                myListe.add(recupere4);
//                myListe.add(recupere5);
//                myListe.add(recupere7);
//            }else if(getitem2.isChecked() && getitem5.isChecked()){
//                myListe.add(recupere5);
//                myListe.add(recupere7);
//            }else if( getitem3.isChecked() && getitem6.isChecked()){
//                myListe.add(recupere6);
//                myListe.add(recupere9);
//
//            }else if (getitem4.isChecked() && getitem6.isChecked()){
//                myListe.add(recupere7);
//                myListe.add(recupere9);
//            }else if(getitem3.isChecked() && getitem6.isChecked()){
//                myListe.add(recupere6);
//                myListe.add(recupere9);
//            }
//            else if(getitem4.isChecked() && getitem6.isChecked()){
//                myListe.add(recupere7);
//                myListe.add(recupere9);
//            }
//            else if(getitem3.isChecked() && getitem4.isChecked() && getitem6.isChecked()){
//                myListe.add(recupere6);
//                myListe.add(recupere7);
//                myListe.add(recupere9);
//            }else if (getitem.isChecked() && getitem5.isChecked() && getitem3.isChecked() && getitem6.isChecked()){
//                myListe.add(recupere4);
//                myListe.add(recupere5);
//                myListe.add(recupere6);
//                myListe.add(recupere9);
//            }else if(getitem.isChecked() &&getitem5.isChecked() && getitem4.isChecked() && getitem6.isChecked()){
//                myListe.add(recupere4);
//                myListe.add(recupere8);
//                myListe.add(recupere7);
//                myListe.add(recupere9);
//            }else if (getitem2.isChecked() && getitem5.isChecked() && getitem3.isChecked() &&getitem6.isChecked(){
//                myListe.add(recupere5);
//                myListe.add(recupere8);
//                myListe.add(recupere6);
//                myListe.add(recupere9);
//            }else if (getitem2.isChecked() && getitem5.isChecked() && getitem4.isChecked()&& getitem6.isChecked()){
//
//            }
//
//            else (getitem2.isChecked() && getitem5.isChecked() && getitem4.isChecked()&& getitem6.isChecked()){
//
//            }



            Utils.info("EditText", myListe.toString());
            //Utils.info(TAG,recupere[1]);
            Bdd.addString(myListe);

            LandingActivity.recupereraid();
            Utils.info("EditText", Bdd.getElement(1).toString());

            //déclencher la création d'un parcours sur la page de landing
                        Intent intent = new Intent(CreateCourse.this, LandingActivity.class);
                        intent.putExtra("name","Toto");
                        startActivity(intent);

            //redirection vers la page de création d'un parcours
                        Intent intent2= new Intent(CreateCourse.this, CourseActivity.class);
                        startActivity(intent2);
        }





        }
    }


