package com.application.sed.raid_tracker_appli.organizer;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.sed.raid_tracker_appli.API.ApiRequestGet;
import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils.Bdd;
import com.application.sed.raid_tracker_appli.Utils.Utils;
import com.application.sed.raid_tracker_appli.helper.EditPosteActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class ManageVolunteersPositionActivity extends AppCompatActivity {


    private TextView mDisplayDate;
    private static Context context;
    private Toolbar toolbar2;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private int hours;
    private int min;
    TextView selectdate;
    private String getdate = "";
    private static LinearLayout ll;
    private static String idRaid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_volunteers);
        Intent intent = getIntent();

        //récupération du contexte
        context = this;
//        this.toolbar2 = findViewById(R.id.toolbar3);
        //récupération de l'id de la toolbar
        toolbar2 = findViewById(R.id.toolbar3);
        ll = findViewById(R.id.layoutposte);

        if(intent != null){
            idRaid = intent.getStringExtra("idRaid");
        }

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
                intent.putExtra("idRaid",idRaid);
                startActivity(intent);
            }
        });

        ApiRequestGet.getAllPostesfromRaid(context, Bdd.getValue(),idRaid);
    }


    public static void affichePostes(String response){
        Utils.debug("affichePostes",response);
        JsonArray jsonArray;
        JsonParser parser = new JsonParser();
        ArrayList<Button> listButton = new ArrayList<>();
        final ArrayList<String> listIdPoste = new ArrayList<>();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        jsonArray = (JsonArray) parser.parse(response);

        for (int i = 0; i < jsonArray.size(); i++){
            Button button = new Button(context);
            JsonObject jsonObject;

            jsonObject = (JsonObject) jsonArray.get(i);
            JsonElement nom = jsonObject.get("type");
            JsonElement id = jsonObject.get("id");
            Utils.debug("affichePostes", nom.toString());

            button.setId(i);
            listIdPoste.add(id.toString());
            button.setText(nom.toString().replace("\"", " "));

            listButton.add(button);
            ll.addView(button);

        }

        //Bdd.setListIdRaid(listIdPoste);
        for (int j = 0; j<listButton.size(); j++) {
            final Button newButton = listButton.get(j);
            //final ArrayList<String> listid =Bdd.getListIdPoste();

            newButton.setOnClickListener( new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent =  new Intent(context, EditPosteActivity.class);
                    int id = newButton.getId();

                    //Id du parcours qu'on veut récupérer
                    intent.putExtra("idPoste",listIdPoste.get(id));
                    intent.putExtra("idRaid",idRaid);
                    context.startActivity(intent);

                }
            });
        }
    }
}


