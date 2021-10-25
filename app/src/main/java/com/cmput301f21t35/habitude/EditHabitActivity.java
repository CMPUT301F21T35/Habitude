package com.cmput301f21t35.habitude;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

public class EditHabitActivity extends AppCompatActivity {
    EditText habitTitle;
    EditText habitDescription;
    EditText habitPlan;
    CalendarView habitCalendar;
    Habit changingHabit; //Talk about this

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);

        habitTitle = findViewById(R.id.habit_title);
        habitDescription = findViewById(R.id.habit_description);
        habitPlan = findViewById(R.id.habit_plan);
        habitCalendar = findViewById(R.id.habit_calendar);
    }

    public void doneButton(View view) {
        try {
            changingHabit.setTitle(habitTitle.getText().toString());
        } catch (Exception ignored) {}

        try {
            changingHabit.setDescription(habitDescription.getText().toString());
        } catch (Exception ignored) {}

        try {
            changingHabit.setPlan(habitPlan.getText().toString());
        } catch (Exception ignored) {}

        try {
            changingHabit.setDate(habitCalendar.getDate()); //?
        } catch (Exception ignored) {}
    }

}