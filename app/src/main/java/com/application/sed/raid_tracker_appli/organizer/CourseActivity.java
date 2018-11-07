package com.application.sed.raid_tracker_appli.organizer;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.application.sed.raid_tracker_appli.Accueil;
import com.application.sed.raid_tracker_appli.Bdd;
import com.application.sed.raid_tracker_appli.CreateCourse;
import com.application.sed.raid_tracker_appli.CreateParcours;
import com.application.sed.raid_tracker_appli.LandingActivity;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils;
import com.application.sed.raid_tracker_appli.WelcomeActivity;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Utils.info("test", "Creation of the new activity");



        toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CourseActivity.this, LandingActivity.class);
                startActivity(intent);

            }
        });

        LinearLayout ll = findViewById(R.id.ParcoursLayout);
        ArrayList<Button> listebouton = new ArrayList<>();
        listebouton = Bdd.getButton();
        for (int i =0; i<Bdd.getButton().size();i++){

            Button button;
            button = listebouton.get(i);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(button, lp);
        }

        for (int j = 0; j<listebouton.size(); j++) {
            Button newButton = listebouton.get(j);

            newButton.setOnClickListener( new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent =  new Intent(CourseActivity.this, CreateCourse.class);
                    startActivity(intent);

                }
            });
        }

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(CourseActivity.this, LandingActivity.class);
        startActivity(intent);
    }

    public void Course(View view){

        Button mybutton = new Button(this);
        mybutton.setId(Bdd.getButton().size()+1);
        mybutton.setText("Ceci est mon parcours " + Bdd.getButton().size()+1);
        Bdd.addButton(mybutton);
        Intent intent2= new Intent(CourseActivity.this, CreateParcours.class);
        startActivity(intent2);

    }
//    /* Méthodes pour le Drawer */
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        // synchroniser le drawerToggle après la restauration via onRestoreInstanceState
//        drawerToggle.syncState();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        drawerToggle.onConfigurationChanged(newConfig);
//    }
}
