package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;
import static com.cmput301f21t35.habitude.R.id.visual_calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddHabitActivity extends AppCompatActivity {
    private EditText habitName;
    private EditText habitReason;
    private DatePicker dateStart;

    private int day;
    private int month;
    private int year;

    private CheckBox monday;
    private CheckBox tuesday;
    private CheckBox wednesday;
    private CheckBox thursday;
    private CheckBox friday;
    private CheckBox saturday;
    private CheckBox sunday;
    ArrayList<String> habitPlan = new ArrayList<>();

    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_add_habit);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("All Habits");

        habitName = (EditText) findViewById(R.id.habitName);
        habitReason = (EditText) findViewById(R.id.habitReason);
        dateStart = (DatePicker) findViewById(R.id.datePicker);

        monday = (CheckBox) findViewById(R.id.monday);
        tuesday = (CheckBox) findViewById(R.id.tuesday);
        wednesday = (CheckBox) findViewById(R.id.wednesday);
        thursday = (CheckBox) findViewById(R.id.thursday);
        friday = (CheckBox) findViewById(R.id.friday);
        saturday = (CheckBox) findViewById(R.id.saturday);
        sunday = (CheckBox) findViewById(R.id.sunday);

        CalendarView visual_ind = (CalendarView)findViewById(visual_calendar); // highlight the date on the calendar

        Button createButton = (Button) findViewById(R.id.createButton);

        Intent intent = new Intent(this,MainActivity.class);
        createButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                final String habitTitleName = habitName.getText().toString();
                final String Reason = habitReason.getText().toString();
                final String day = Integer.toString(dateStart.getDayOfMonth());
                final String month = Integer.toString(dateStart.getMonth());
                final String year = Integer.toString(dateStart.getYear());
                final String habitStartDate = (year + "-" + month + "-" +day);

                setHabitPlan();
                final String habitPlan_toString = String.valueOf(habitPlan);
                final String habitPlan_final = habitPlan_toString.substring(1,habitPlan_toString.length() - 1).replace(" ","");

                HashMap<String, String> data = new HashMap<>();
                data.put("Habit Reason", Reason);
                data.put("Date", habitStartDate);
                data.put("Plan", habitPlan_final);

                collectionReference.document(habitTitleName)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "Data has been added successfully");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Data has not been added successfully");
                            }
                        });
                finish();
            }
        });

        createButton = (Button) findViewById(R.id.createButton);

        createButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String habitTitleName = habitName.getText().toString();
                Date habitStartDate = new Date(dateStart.getYear() - 1900, dateStart.getMonth(), dateStart.getDayOfMonth());
                setHabitHash();
                // DOUBLE CHECK IF THERE IS IMPLEMENTATION TO GET HABITREASON
                //Habit newHabit = new Habit(habitTitleName, "", habitStartDate, habitHash);
                //HabitList.add(newHabit);
            }
        });
//        String[] splitedDate = existingDate.split("-");
//        dateStart.init(Integer.parseInt(splitedDate[0]), Integer.parseInt(splitedDate[1])-1,
//                Integer.parseInt(splitedDate[2]), null);
//
//        year = dateStart.get(Calendar.YEAR)
//        Date newDate = new Date(dateStart.getYear() - 1900, dateStart.getMonth(), dateStart.getDayOfMonth());
    }

    public void setHabitPlan() {
        if (sunday.isChecked()) {
            habitPlan.add("Sunday");
        }

        if (monday.isChecked()){
            habitPlan.add("Monday");
        }

        if (tuesday.isChecked()){
            habitPlan.add("Tuesday");
        }
        if (wednesday.isChecked()) {
            habitPlan.add("Wednesday");
        }

        if (thursday.isChecked()){
            habitPlan.add("Thursday");
        }

        if (friday.isChecked()){
            habitPlan.add("Friday");
        }

        if (saturday.isChecked()){
            habitPlan.add("Saturday");
        }
    }

    HashMap<Integer, Boolean> habitHash = new HashMap<Integer, Boolean>();

    public void setHabitHash(){
        this.habitHash.put(1, sunday.isChecked());
        this.habitHash.put(2, monday.isChecked());
        this.habitHash.put(3, tuesday.isChecked());
        this.habitHash.put(4, wednesday.isChecked());
        this.habitHash.put(5, thursday.isChecked());
        this.habitHash.put(6, friday.isChecked());
        this.habitHash.put(7, saturday.isChecked());
    }
}