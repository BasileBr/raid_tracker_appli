package com.application.sed.raid_tracker_appli.organizer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.API.ApiRequestPost;
import com.application.sed.raid_tracker_appli.LandingActivity;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditCourse extends AppCompatActivity {

    private TextView mDisplayDate;
    private TextView mDisplayHour;
    private static Context context;
    private Toolbar toolbar2;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener myTimeListener;
    public static List myListe;
    private int hours;
    private int min;
    //TextView selectdate;
    private String getDate = "";
    private static String token;


    private String getHour ="";
    private String idRaid;

    Button mButton;
    public static EditText name_raid;
    public static EditText lieu;
    public static EditText organizer_team;
    public static EditText edition;
    public static TextView selectdate;
    public static TextView selecthour;
    public Integer checkonclickDate=0;
    public Integer checkonclickHour=0;
    private DatePickerDialog dialog;


    String recuperenom;
    String recuperelieu;
    String recuperedate;
    String recupereedition;
    String recupereequipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        Intent intent = getIntent();

        //récupération du contexte
        context = this;

        token = Bdd.getValue();

//        this.toolbar2 = findViewById(R.id.toolbar3);

        if (intent != null) {
            //récupération de l'id de la toolbar
            toolbar2 = findViewById(R.id.toolbar3);


            idRaid= intent.getStringExtra("idRaid");
            //definir notre toolbar en tant qu'actionBar
            setSupportActionBar(toolbar2);

            //afficher le bouton retour
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

            //si on appuie sur le bouton de retour
            toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditCourse.this, CourseActivity.class);
                    intent.putExtra("idRaid",idRaid);
                    startActivity(intent);
                }
            });

            //nom du raid
            name_raid = (EditText) findViewById(R.id.name_raid);

            //lieu du raid
            lieu = (EditText) findViewById(R.id.lieu);

            //année de l'édition
            edition=(EditText)findViewById(R.id.edition);

            //nom de l'équipe organisatrice
            organizer_team = (EditText) findViewById(R.id.organizer_team);

            selectdate=(TextView) findViewById(R.id.selectdate);

            selecthour=(TextView) findViewById(R.id.selecthour);

            /**
             * Récupérer la date, l'afficher et la stocker
             */
            final int tmp;
            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                    checkonclickDate =1;
                    String date = year + "/" + (month + 1) + "/" + dayOfMonth;
                    selectdate.setText(date);
                    getDate = date;
                    Utils.debug("getdateyvan",getDate);
                    selectdate.setError(null);
                }
            };

            myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    checkonclickHour =1;
                    String hour =hourOfDay+":"+minute;
                    selecthour.setText(hour);
                    getHour=hour;
                    selecthour.setError(null);
                }
            };


            ApiRequestGet.getSpecificRaidforCourseActivity(context,Bdd.getValue(),idRaid,"EditCourse");


        }
    }

    public void UpdateDate(View view){
        //affichage du calendrier

        final long today = System.currentTimeMillis() - 1000;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
//        hours = cal.get(Calendar.HOUR_OF_DAY);
//        min = cal.get(Calendar.MINUTE);

         dialog = new DatePickerDialog(
                EditCourse.this, android.R.style.Theme, mDateSetListener, year, month, day);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        //dialog.getDatePicker().setMaxDate(1577833200000L);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();




    }

    public void UpdateHour(View view){

        checkonclickHour=1;
        Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Selectionnez une heure");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final Integer storeHour=hour;
        timePickerDialog.show();





    }


    public void updateRaid(View view ){
        /**
         * récupération de tous les infos lors de la validation d'un compte
         */
        //button de validation
        mButton = (Button) findViewById(R.id.createAccount);



        //initation de variables par vérifier la complétude des champs
        int checknameraid=1;
        int checklieu=1;
        int checkdate=1;
        int checkedition=1;
        int checkteamname=1;


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

        if(checknameraid==1 && checklieu ==1 && checkdate==1 && checkedition ==1 && checkteamname ==1){
            myListe =  new ArrayList<>();

            recuperenom=name_raid.getText().toString();
            recuperelieu=lieu.getText().toString();

            if (checkonclickDate==0 && checkonclickHour==0 ){
                recuperedate=selectdate.getText().toString()+" "+selecthour.getText().toString();
            }else if(checkonclickDate==1 && checkonclickHour==0){
                recuperedate=getDate+" "+selecthour.getText().toString();
            }else if(checkonclickDate==0 && checkonclickHour==1){
                recuperedate=selectdate.getText().toString()+" "+getHour;
            }else if(checkonclickDate==1 && checkonclickHour==1){
                recuperedate=getDate+" "+getHour;
            }
            //recuperedate=getDate+" "+getHour;
            recupereedition=edition.getText().toString();
            recupereequipe=organizer_team.getText().toString();

            Utils.debug("date",recuperenom);
            Utils.debug("date",recuperelieu);
            Utils.debug("date",recuperedate);
            Utils.debug("date",recupereedition);
            Utils.debug("date",recupereequipe);

            ApiRequestPost.postUpdateRaid(this,Bdd.getValue(),idRaid, recuperenom,recuperelieu,recuperedate,recupereedition,recupereequipe,false);

        }
    }

    /*
     ** Auto compléter les champs
     */

    public static void Autcomplete(String response){



        JsonParser parser = new JsonParser();
        JsonObject raid = (JsonObject) parser.parse(response);

        final String nomraid=raid.get("nom").toString().replace("\"","");
        final String lieuraid=raid.get("lieu").toString().replace("\"","");

       // String date=raid.get("date").toString().substring(1,11);
        String dateY=raid.get("date").toString().substring(1,5);
        String dateM=raid.get("date").toString().substring(6,8);
        String dateD=raid.get("date").toString().substring(9,11);
        final String date=dateY+'/'+dateM+'/'+dateD;


        String hour=raid.get("date").toString().substring(12,14);

        Utils.debug("hour",hour);

        String minute=raid.get("date").toString().substring(14,17);

        Utils.debug("minute",minute);
        final String hourMinute=hour+minute;

        final String editionraid=raid.get("edition").toString();
        final String equiperaid=raid.get("equipe").toString().replace("\"","");
        //Utils.debug("essai",datetest);

        name_raid.setText(nomraid);
        lieu.setText(lieuraid);
        selectdate.setText(date);
        selecthour.setText(hourMinute);
        edition.setText(editionraid);
        organizer_team.setText(equiperaid);

    }


    /**
     * Permet de retourner à la vue de courseactivity
     */
    public void cancel(View view) {
        Intent intent = new Intent(EditCourse.this, CourseActivity.class);
        intent.putExtra("idRaid",idRaid);
        startActivity(intent);
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
}
