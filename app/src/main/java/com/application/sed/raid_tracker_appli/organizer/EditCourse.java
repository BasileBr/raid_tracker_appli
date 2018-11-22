package com.application.sed.raid_tracker_appli.organizer;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.application.sed.raid_tracker_appli.LandingActivity;
import com.application.sed.raid_tracker_appli.R;

import java.util.Calendar;
import java.util.Date;

public class EditCourse extends AppCompatActivity {

    private TextView mDisplayDate;
    private static Context context;
    private Toolbar toolbar2;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private int hours;
    private int min;
    TextView selectdate;
    private String getdate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        //récupération du contexte
        context = this;
//        this.toolbar2 = findViewById(R.id.toolbar3);
        //récupération de l'id de la toolbar
        toolbar2 = findViewById(R.id.toolbar3);


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
                startActivity(intent);
            }
        });


        /**
         * Affichage pour sélectionner la date du RAID
         */

        mDisplayDate = (TextView) findViewById(R.id.selectdate);
        Date today = Calendar.getInstance().getTime();
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
                        EditCourse.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);

                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
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
                //Log.d(TAG, "onDateSet: yyyy/MM/dd HH:mm: " + year + "/" + month + "/" + dayOfMonth + " " +hours +":"+min);

                String date = year + "/" + (month+1) + "/" + dayOfMonth + " " + hours + ":"+min;
                mDisplayDate.setText(date);
                getdate = date;
                selectdate.setError(null);
            }
        };

    }

    /**
     * Permet de retourner à la vue de courseactivity
     */
    public void cancel(View view) {
            Intent intent = new Intent(EditCourse.this, CourseActivity.class);
        startActivity(intent);
    }
}
