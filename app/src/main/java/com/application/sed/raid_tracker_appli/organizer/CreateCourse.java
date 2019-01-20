package com.application.sed.raid_tracker_appli.organizer;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.application.sed.raid_tracker_appli.API.ApiRequestPost;
import com.application.sed.raid_tracker_appli.LandingActivity;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;


public class CreateCourse extends AppCompatActivity {
    private String TAG = "CreateCourse";
    private TextView mDisplayDate;
    private String getdate = "";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    public static List myListe;
    public String recupere;
    public String recupere1;
    public String recupere2;
    public String recupere3;
    public Button mButton;
    public EditText name_raid;
    public EditText lieu;
    public EditText organizer_team;
    public EditText edition;
    public TextView selectdate;
    private int hours;
    private int min;

    private static Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createcourse);
        Utils.info(TAG, "OnCreate");

        context = this;
        Toolbar toolbar2 = findViewById(R.id.toolbar3);

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

        mDisplayDate = findViewById(R.id.selectdate);

        /**
         * Affichage pour sélectionner la date du RAID
         */
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                hours = cal.get(Calendar.HOUR_OF_DAY);
                min = cal.get(Calendar.MINUTE);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateCourse.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);

                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        /**
         * Récupérer la date, l'afficher et la stocker
         */

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectdate= (TextView) findViewById(R.id.selectdate);
                Log.d(TAG, "onDateSet: yyyy/MM/dd HH:mm: " + year + "/" + month + "/" + dayOfMonth + " " +hours +":"+min);

                String date = year + "/" + (month+1) + "/" + dayOfMonth + " " + hours + ":"+min;
                mDisplayDate.setText(date);
                getdate = date;
                selectdate.setError(null);
            }
        };
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

    /**
     *
     * @param text
     * @return
     */
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

    /**
     *
     * @param view
     */
    public void createnewraid(View view){

        //button de validation
        mButton = findViewById(R.id.createAccount);

        //nom du raid
        name_raid = findViewById(R.id.name_raid);

        //lieu du raid
        lieu = findViewById(R.id.lieu);

        //année de l'édition
        edition= findViewById(R.id.edition);

        //nom de l'équipe organisatrice
        organizer_team = findViewById(R.id.organizer_team);
        selectdate= findViewById(R.id.selectdate);

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

/* Parce que c'est collector

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
        */


        if(checknameraid==1 && checklieu ==1 && checkdate==1 && checkedition ==1 && checkteamname ==1 ){

            //myListe =  new ArrayList<>();
            recupere = name_raid.getText().toString();
            recupere1 = lieu.getText().toString();
            recupere2= edition.getText().toString();
            recupere3 = organizer_team.getText().toString();

            //myListe.add(recupere);  // récupère le nom du raid
            //myListe.add(recupere1); // le lieu de l'évènement
            //myListe.add(getdate); // sélectionne la date de l'évènement


            //myListe.add(recupere2); // L'édition
            //myListe.add(recupere3); // le nom de l'équipe organisatrice

            Bdd.addString(myListe);
            ApiRequestPost.postRaid(this,Bdd.getValue(),recupere,recupere1,getdate,recupere2,recupere3, false);
        }


    }

    /**
     *
     * @param jsonObject
     * @throws JSONException
     */
    public static void createRaid(JSONObject jsonObject) throws JSONException {

        String idRaid = jsonObject.getString("id");
        Intent intent2= new Intent(context, CourseActivity.class);
        intent2.putExtra("idRaid",idRaid);
        context.startActivity(intent2);
    }
}


