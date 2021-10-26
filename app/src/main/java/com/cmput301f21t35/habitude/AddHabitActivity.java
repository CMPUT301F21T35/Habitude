package com.cmput301f21t35.habitude;

import static com.cmput301f21t35.habitude.R.id.visual_calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_add_habit);

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

                String habitTitleName = habitName.getText().toString();
                String Reason = habitReason.getText().toString();
                Date habitStartDate = new Date(dateStart.getYear() - 1900, dateStart.getMonth(), dateStart.getDayOfMonth());
                setHabitHash();
                Habit newHabit = new Habit(habitTitleName, Reason, habitStartDate, habitHash);
                intent.putExtra("newHabit",newHabit);
                //startActivity(intent);
                finish();
            }
        });

//        String[] splitedDate = existingDate.split("-");
//        dateStart.init(Integer.parseInt(splitedDate[0]), Integer.parseInt(splitedDate[1])-1,
//                Integer.parseInt(splitedDate[2]), null);
//
//        year = dateStart.get(Calendar.YEAR)
//        Date newDate = new Date(dateStart.getYear() - 1900, dateStart.getMonth(), dateStart.getDayOfMonth());


    }

    HashMap<Integer, Boolean> habitHash = new HashMap<Integer, Boolean>();

    public void setHabitHash() {
        this.habitHash.put(1, sunday.isChecked());
        this.habitHash.put(2, monday.isChecked());
        this.habitHash.put(3, tuesday.isChecked());
        this.habitHash.put(4, wednesday.isChecked());
        this.habitHash.put(5, thursday.isChecked());
        this.habitHash.put(6, friday.isChecked());
        this.habitHash.put(7, saturday.isChecked());
    }
}