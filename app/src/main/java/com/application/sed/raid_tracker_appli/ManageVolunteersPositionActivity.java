package com.application.sed.raid_tracker_appli;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.application.sed.raid_tracker_appli.organizer.CourseActivity;

public class ManageVolunteersPositionActivity extends AppCompatActivity {


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
        setContentView(R.layout.activity_manage_volunteers);

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
                Intent intent = new Intent(ManageVolunteersPositionActivity.this, CourseActivity.class);
                startActivity(intent);
            }
        });
    }
}


