package com.application.sed.raid_tracker_appli;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class LandingActivity extends AppCompatActivity {
    private ArrayList<List> Toto;
    //public static List Tata;
    private String test = "test";

    private ArrayList<Button> listButton;

    private String TAG = "WelcomeActivity";
    private static String TAGs = "WelcomeActivity";
    // Attributs pour le menu Hamburger et l'actionBar
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    View button;



    String Element;
    private ActionBarDrawerToggle drawerToggle;

    // Attributs pour faire passer les élèments -> tests
    private static int items = 2;
    private String basile="Toto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Intent intent=getIntent();
        if (intent != null) {
            Utils.warm(TAG,"je rentre ici");

            test = intent.getStringExtra("name");
            Utils.warm(TAG, test);
            if(test.equals(basile)){
                Utils.warm(TAG, "Je rentre la");
                /* get List from Create Raid */
                Toto = Bdd.getArrayList();

                 listButton = new ArrayList<>();


                //Button myButton = new Button(this);


                for (int i = 0; i < Toto.size(); i ++){


                    Button myButton = new Button(this);
                    Utils.warm("Ajout du bouton", "Je rentre dans le for "+i);

                    List Tata = new ArrayList();
                    Tata = Toto.get(i);

                    myButton.setText(Tata.get(0).toString());
                    myButton.setId(i);

//                    myButton.setX(50);
//                      myButton.setY(100);

                    listButton.add(myButton);
                    Utils.warm("listbutton", listButton.get(i).toString());

                }


                for (int i = 0; i < listButton.size(); i ++){

                    Utils.warm("Rajout des boutons", "Valeurs de i" +i);
                    Button myButton2 = listButton.get(i);

                    LinearLayout ll = (LinearLayout) findViewById(R.id.Myfuckinglayout);

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    ll.addView(myButton2, lp);


                }

                /*récupérer la première Liste*/


                /* */
//                String titre = Tata.get(1).toString();
//                Utils.info("EditTexttttttt", titre);


                /*for (int i = 0; i< Tata.size(); i++) {
                    Utils.warm("i", String.valueOf(i));
                    Utils.warm("Tata", Tata.get(i).toString());

                }*/

            }

        }


        Utils.info(TAG, "OnCreate");


        /*
        Button myButton = new Button(this);
        myButton.setText("boutton");

        LinearLayout ll = (LinearLayout) findViewById(R.id.test2);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(myButton, lp);

        */


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

                drawerLayout.openDrawer(Gravity.START);

            }
        });
//





    }

    public void getDescriptionRAid(View view) {
        //RelativeLayout ll=(RelativeLayout) findViewById(R.id.boutooondemerde);
//        /* get List from Create Raid */
//        Toto = Bdd.getArrayList();
//
//        /*récupérer la première Liste*/
//        Tata = Toto.get(0);
//
//        /* */
//        String titre = Tata.get(0).toString();
//
////
//        Button buttonView = new Button(this);
//        buttonView.setText(titre);
//        ll.addView(buttonView);
//
//        Utils.info("EditText", titre);

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


    public void join(View view) {

    }

    public void CreateCourse(View view){
        Intent intent =  new Intent(LandingActivity.this, CreateCourse.class);
        intent.putExtra("items",items);
        startActivity(intent);
    }

    public static void recupereraid(){
    }

}
