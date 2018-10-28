package com.application.sed.raid_tracker_appli.organizer;

import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils;

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
    }

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
