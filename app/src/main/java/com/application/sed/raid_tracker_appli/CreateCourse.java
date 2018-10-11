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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.application.sed.raid_tracker_appli.organizer.NewraidActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CreateCourse extends AppCompatActivity {
    private String TAG="CreateCourse";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public static List myListe = new ArrayList<>();

    String recupere;
    String recupere1;
    Button mButton;
    EditText mEdit;
    EditText mEdit1;
    EditText mEdit2;


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


        mButton = (Button)findViewById(R.id.createAccount);
        mEdit   = (EditText)findViewById(R.id.name_raid);
        mEdit1   = (EditText)findViewById(R.id.lieu);



        mButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        recupere=mEdit.getText().toString();
                        recupere1=mEdit1.getText().toString();


                        myListe.add(recupere);
                        myListe.add(recupere1);

                        Utils.info("EditText",myListe.toString());
                        //Utils.info(TAG,recupere[1]);
                        Bdd.addString(myListe);



                        Utils.info("EditText", Bdd.getElement(1).toString());


                    }
                });


    }



    public void cancel(View view){
        Intent intent = new Intent(CreateCourse.this, LandingActivity.class);
        startActivity(intent);
    }
}

