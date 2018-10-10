package com.application.sed.raid_tracker_appli;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.application.sed.raid_tracker_appli.organizer.NewraidActivity;

import java.util.Calendar;

public class CreateCourse extends AppCompatActivity {
    private String TAG="CreateCourse";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createcourse);
        Utils.info(TAG, "OnCreate");


        Spinner mySpinner=(Spinner)findViewById(R.id.spinner);

        ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(CreateCourse.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.sports));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);


        mDisplayDate=(TextView)findViewById(R.id.tvDate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal= Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(
                        CreateCourse.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener,day,month,year);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG,"onDateSet: mm/dd/yyyy: "+month+"/"+ dayOfMonth +"/"+year);
                String date =dayOfMonth + "/" + month +"/" + year;
                mDisplayDate.setText(date);
            }
        };
    }
    public void cancel(View view){
        Intent intent = new Intent(CreateCourse.this, LandingActivity.class);
        startActivity(intent);
    }
}

