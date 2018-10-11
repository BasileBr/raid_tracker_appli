package com.application.sed.raid_tracker_appli;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class LandingActivity extends AppCompatActivity {

    private String TAG = "WelcomeActivity";
    private DrawerLayout drawerLayout;

    private Toolbar toolbar;
    View button;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Utils.info(TAG, "OnCreate");

        this.toolbar = findViewById(R.id.toolbar);

        //definir notre toolbar en tant qu'actionBar
        setSupportActionBar(toolbar);

        //afficher le bouton retour
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        this.drawerLayout = findViewById(R.id.drawerLayout);
        this.drawerToggle = new ActionBarDrawerToggle(this,this.drawerLayout,0,0);
        this.drawerLayout.setDrawerListener(this.drawerToggle);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.info("NICK", "button button button..................");

                drawerLayout.openDrawer(Gravity.LEFT);

            }
        });





        /*drawerLayout = findViewById(R.id.drawerLayout);
        final TextView user = findViewById(R.id.username);*/
        //user.setText("Bienvenue " +Utils.Name);
    }


    //ne pas oublier de copier/coller ces 2 méthodes
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


    public void join(View view) {

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_landing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId()) {
            case R.id.Hamburgermenu :
                if (!drawerLayout.isDrawerVisible(R.id.Hamburgermenu))
                    drawerLayout.openDrawer(Gravity.RIGHT);
                else
                    drawerLayout.closeDrawer(Gravity.LEFT); // A corriger -> pas d'erreurs, mais fonctionne pas

                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }*/
}
