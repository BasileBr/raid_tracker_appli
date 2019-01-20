package com.application.sed.raid_tracker_appli.organizer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.API.ApiRequestPost;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditCourse extends AppCompatActivity {
    private String TAG = "EditCourse";
    private static Context context;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener myTimeListener;
    public static List myListe;
    private String getDate = "";

    public String token;

    private String getHour ="";
    private String idRaid;

    public Button mButton;
    public static EditText name_raid;
    public static EditText lieu;
    public static EditText organizer_team;
    public static EditText edition;
    public static TextView selectdate;
    public static TextView selecthour;
    public Integer checkonclickDate=0;
    public Integer checkonclickHour=0;

    String recuperenom;
    String recuperelieu;
    String recuperedate;
    String recupereedition;
    String recupereequipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.info(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        Intent intent = getIntent();

        //récupération du contexte
        context = this;
        token = Bdd.getValue();

        if (intent != null) {
            //récupération de l'id de la toolbar
            Toolbar toolbar2 = findViewById(R.id.toolbar3);
            idRaid= intent.getStringExtra("idRaid");

            //definir notre toolbar en tant qu'actionBar
            setSupportActionBar(toolbar2);

            //afficher le bouton retour
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
            name_raid = findViewById(R.id.name_raid);

            //lieu du raid
            lieu = findViewById(R.id.lieu);

            //année de l'édition
            edition= findViewById(R.id.edition);

            //nom de l'équipe organisatrice
            organizer_team = findViewById(R.id.organizer_team);

            selectdate= findViewById(R.id.selectdate);

            selecthour= findViewById(R.id.selecthour);

            /**
             * Récupérer la date, l'afficher et la stocker
             */
            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    checkonclickDate =1;
                    String date = year + "/" + (month + 1) + "/" + dayOfMonth;
                    selectdate.setText(date);
                    getDate = date;
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

            ApiRequestGet.getSpecificRaidforCourseActivity(context,token,idRaid,"EditCourse");
        }
    }

    /**
     *
     * @param view
     */
    public void UpdateDate(View view){

        //affichage du calendrier
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(
                EditCourse.this, android.R.style.Theme, mDateSetListener, year, month, day);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    /**
     *
     * @param view
     */
    public void UpdateHour(View view){
        checkonclickHour=1;
        Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Selectionnez une heure");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    /**
     *
     * @param view
     */
    public void updateRaid(View view ){
        /**
         * récupération de tous les infos lors de la validation d'un compte
         */
        //button de validation
        mButton = findViewById(R.id.createAccount);

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
            recupereedition=edition.getText().toString();
            recupereequipe=organizer_team.getText().toString();

            ApiRequestPost.postUpdateRaid(this,Bdd.getValue(),idRaid, recuperenom,recuperelieu,recuperedate,recupereedition,recupereequipe,false);

        }
    }

    /**
     ** Auto compléter les champs
     */
    public static void Autcomplete(String response){

        JsonParser parser = new JsonParser();
        JsonObject raid = (JsonObject) parser.parse(response);

        final String nomraid=raid.get("nom").toString().replace("\"","");
        final String lieuraid=raid.get("lieu").toString().replace("\"","");

        String dateY=raid.get("date").toString().substring(1,5);
        String dateM=raid.get("date").toString().substring(6,8);
        String dateD=raid.get("date").toString().substring(9,11);
        final String date=dateY+'/'+dateM+'/'+dateD;

        String hour=raid.get("date").toString().substring(12,14);
        String minute=raid.get("date").toString().substring(14,17);

        final String hourMinute=hour+minute;
        final String editionraid=raid.get("edition").toString();
        final String equiperaid=raid.get("equipe").toString().replace("\"","");

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

    /**
     * vérifie qu'un élement editext n'est pas vide
     * @param text
     * @return
     */
    boolean isEmpty (EditText text){
        CharSequence str= text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean isEmpty2 (TextView text){
        CharSequence str= text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}
