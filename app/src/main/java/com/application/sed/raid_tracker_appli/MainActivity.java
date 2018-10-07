package com.application.sed.raid_tracker_appli;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.info(TAG, "OnCreate");

        /** Ceci est une façon d'écouter un bouton sans passer par une fonction dédiée
        final Button button = findViewById(R.id.loginbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.debug(TAG, "Je suis dans le onclick de login");
            }
        });
        **/

    }

    /**
     * Mettre la méthode en public pour que le bouton y ait accés
     * @param view
     */
    public void login(View view){

        /**TODO
         * Bouton du login -> direction vers page HOME si ok (HomeActivity)
         * Sinon, message d'erreur
         */

        Utils.info(TAG,"Login Button action");

    }




}
