package com.application.sed.raid_tracker_appli;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.application.sed.raid_tracker_appli.organizer.NewraidActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CreateCourse extends AppCompatActivity {
    private String TAG = "CreateCourse";
    private TextView mDisplayDate;
    private String getdate = "";
    private DrawerLayout drawerLayout;

    private Toolbar toolbar2;
    View button;
    private ActionBarDrawerToggle drawerToggle;

    private CheckBox getitem;
    private CheckBox getitem2;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private int items;


    public static List myListe;

    CharSequence charSequence = "";
    CharSequence charSequence2 = "";
    CharSequence charSequence3 = "";
    CharSequence charSequence4 = "";
    CharSequence charSequence5 = "";


    LinearLayout getLinear;
    String recupere;
    String recupere1;
    String recupere2;
    Button mButton;
    EditText mEdit;
    EditText mEdit1;
    EditText mEdit2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createcourse);
        Utils.info(TAG, "OnCreate");

        this.toolbar2 = findViewById(R.id.toolbar3);

        //definir notre toolbar en tant qu'actionBar
        setSupportActionBar(toolbar2);

        //afficher le bouton retour
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        this.drawerLayout = findViewById(R.id.drawerLayout3);
        this.drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, 0, 0);
        this.drawerLayout.setDrawerListener(this.drawerToggle);

        toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.info("NICK", "button button button..................");

                drawerLayout.openDrawer(Gravity.START);

            }
        });


        Intent intent = getIntent();

        if (intent != null) {
            items = (intent.getIntExtra("items", items));
            Utils.info(TAG, String.valueOf(items));
        }
        /**
         * Selectionner plusieurs Sports
         */
//        Spinner mySpinner=(Spinner)findViewById(R.id.spinner);
//
//        ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(CreateCourse.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.sports));
//        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mySpinner.setAdapter(myAdapter);
//
//
        mDisplayDate = (TextView) findViewById(R.id.tvDate);


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

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateCourse.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, 2010, 00, 01);

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
                Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);
                String date = dayOfMonth + "/" + month + 1 + "/" + year;
                mDisplayDate.setText(date);
                getdate = date;
            }
        };


        mButton = (Button) findViewById(R.id.createAccount);
        mEdit = (EditText) findViewById(R.id.name_raid);
        mEdit1 = (EditText) findViewById(R.id.lieu);
        mEdit2 = (EditText) findViewById(R.id.organizer_teame);

        getitem = (CheckBox) findViewById(R.id.checkbox_meat);
        getitem2 = (CheckBox) findViewById(R.id.checkbox_cheese);
        getLinear = (LinearLayout) findViewById(R.id.checkbox);


        mButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        myListe =  new ArrayList<>();
                        recupere = mEdit.getText().toString();
                        recupere1 = mEdit1.getText().toString();
                        recupere2 = mEdit2.getText().toString();

                        myListe.add(recupere);  // récupère le nom du raid
                        myListe.add(recupere1); // le lieu de l'évènement
                        myListe.add(getdate); // sélectionne la date de l'évènement
                        myListe.add(recupere2); // le nom de l'équipe organisatrice
                        myListe.add(charSequence);
                        myListe.add(charSequence2);


                        Utils.info("EditText", myListe.toString());
                        //Utils.info(TAG,recupere[1]);
                        Bdd.addString(myListe);

                        LandingActivity.recupereraid();
                        Utils.info("EditText", Bdd.getElement(1).toString());
                        Intent intent = new Intent(CreateCourse.this, LandingActivity.class);
                        intent.putExtra("name","Toto");
                        startActivity(intent);



                    }
                });


    }

    public void onCheckboxClicked(View view) {

        getitem = (CheckBox) findViewById(R.id.checkbox_meat);
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.checkbox_meat:
                if (checked)
                    charSequence = getitem.getText();

//                    int toto = getLinear.getId();
//                    Utils.info(TAG,String.valueOf(toto));

                break;
            case R.id.checkbox_cheese:
                if (checked)
                    charSequence2 = getitem2.getText();


                break;
        }
    }

    /**
     * Permet de retourner à la vue "landingActivity
     */

    public void cancel(View view) {
        Intent intent = new Intent(CreateCourse.this, LandingActivity.class);
        startActivity(intent);
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

