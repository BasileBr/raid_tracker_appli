package com.application.sed.raid_tracker_appli;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.application.sed.raid_tracker_appli.organizer.NewraidActivity;


public class LandingActivity extends AppCompatActivity {

    private String TAG = "WelcomeActivity";
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Utils.info(TAG, "OnCreate");

        drawerLayout = findViewById(R.id.drawerLayout);
        final TextView user = findViewById(R.id.username);
        //user.setText("Bienvenue " +Utils.Name);
    }

    public void join(View view) {

    }

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
    }
}
