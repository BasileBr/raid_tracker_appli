package com.application.sed.raid_tracker_appli.organizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.application.sed.raid_tracker_appli.R;
import com.application.sed.raid_tracker_appli.Utils;

public class CourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Utils.info("test","Creation of the new activity");
    }
}
