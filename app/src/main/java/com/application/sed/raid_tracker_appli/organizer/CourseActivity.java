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

import com.application.sed.raid_tracker_appli.Bdd;
import com.application.sed.raid_tracker_appli.CreateCourse;
import com.application.sed.raid_tracker_appli.CreateParcours;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils;

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


        this.toolbar = findViewById(R.id.toolbar);

        //definir notre toolbar en tant qu'actionBar
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.drawerLayout = findViewById(R.id.drawerLayout);
        this.drawerToggle = new ActionBarDrawerToggle(this,this.drawerLayout,0,0);
        this.drawerLayout.setDrawerListener(this.drawerToggle);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.info("NICK", "button button button..................");

                drawerLayout.openDrawer(Gravity.START);

            }
        });
        LinearLayout ll = findViewById(R.id.ParcoursLayout);
        ArrayList<Button> listebouton = new ArrayList<>();
        listebouton = Bdd.getButton();
        for (int i =0; i<Bdd.getButton().size();i++){

            Button button = new Button(this);
            button = listebouton.get(i);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(button, lp);
        }

    }


    public void Course(View view){

        Button mybutton = new Button(this);
        mybutton.setId(Bdd.getButton().size()+1);
        mybutton.setText("Ceci est mon parcours " + Bdd.getButton().size()+1);
        Bdd.addButton(mybutton);
        Intent intent2= new Intent(CourseActivity.this, CreateParcours.class);
        startActivity(intent2);

    }
    /* Méthodes pour le Drawer */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // synchroniser le drawerToggle après la restauration via onRestoreInstanceState
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
