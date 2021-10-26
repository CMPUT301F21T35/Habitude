package com.cmput301f21t35.habitude;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TodayPlanActivity extends AppCompatActivity {
    ListView today_habitList;
    ArrayAdapter<Habit> today_habitAdapter;
    ArrayList<Habit> today_habitDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_plan);

        today_habitList = findViewById(R.id.today_plan_list);
        today_habitDataList = new ArrayList<>();
        today_habitAdapter = new HabitList(this,today_habitDataList);
        today_habitList.setAdapter(today_habitAdapter);

        final Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        String weekday = String.valueOf(c.get(Calendar.DAY_OF_WEEK));

        // deliver the habitDataList from firestore to TodayPlanActivity.
        // Compare the current date with the plan of the habit list.
        // if current date is equal or greater than the plan.
        // compare the weekday
        // if today's weekday is equal to the weekday of the habit list
        // today_habitDataList.add(habit list item).




    }



}