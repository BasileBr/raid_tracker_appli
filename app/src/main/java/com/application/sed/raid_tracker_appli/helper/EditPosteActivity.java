package com.application.sed.raid_tracker_appli.helper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.application.sed.raid_tracker_appli.LandingActivity;
import com.application.sed.raid_tracker_appli.ManageVolunteersPositionActivity;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.application.sed.raid_tracker_appli.organizer.CreateParcours;

public class EditPosteActivity extends AppCompatActivity {

    public static Context context;
    private Toolbar toolbar;
    public String idRaid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_poste);

        Intent intent = getIntent();

        if (intent != null){

            toolbar = (Toolbar) findViewById(R.id.toolbar);

            idRaid = intent.getStringExtra("idRaid");
            // on définit la toolbar
            setSupportActionBar(toolbar);

            //ajouter un bouton retour dans l'action bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            //si on appuie sur le bouton retour, on arrive sur la page landing
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditPosteActivity.this, ManageVolunteersPositionActivity.class);
                    intent.putExtra("idRaid",idRaid);
                    startActivity(intent);
                }
            });

            //récuperation du context
            context = this;
            String id = intent.getStringExtra("idPoste");
            Utils.debug("EditPosteActicity",id);
        }
    }
}
