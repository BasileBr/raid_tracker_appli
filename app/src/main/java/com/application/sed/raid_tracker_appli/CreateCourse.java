package com.application.sed.raid_tracker_appli;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.application.sed.raid_tracker_appli.organizer.NewraidActivity;

public class CreateCourse extends AppCompatActivity {
    private String TAG="CreateCourse";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creationaccount);
        Utils.info(TAG, "OnCreate");
    }


}

