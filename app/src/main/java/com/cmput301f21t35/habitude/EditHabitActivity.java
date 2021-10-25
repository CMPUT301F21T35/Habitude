package com.cmput301f21t35.habitude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import java.util.HashMap;

public class EditHabitActivity extends AppCompatActivity {
    EditText habitTitle;
    EditText habitDescription;
    EditText habitPlan;
    CalendarView habitCalendar;
    Habit changingHabit; //Talk about this
    final CollectionReference collectionReference = db.collection("Cities"); //And this

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
        HashMap<String, String> data = new HashMap<>(); //I don't think that's quite right
        data.put("Title",changingHabit.getTitle());
        data.put("Description",changingHabit.getDescription());
        data.put("Plan",changingHabit.getPlan());
        data.put("Calendar",changingHabit.getCalendar());

        try {
            changingHabit.setTitle(habitTitle.getText().toString());
            data.put("Title",habitTitle.getText().toString());
        } catch (Exception ignored) {}

        try {
            changingHabit.setDescription(habitDescription.getText().toString());
            data.put("Description",habitDescription.getText().toString());
        } catch (Exception ignored) {}

        try {
            changingHabit.setPlan(habitPlan.getText().toString());
            data.put("Plan",habitPlan.getText().toString());
        } catch (Exception ignored) {}

        try {
            changingHabit.setDate(habitCalendar.getDate()); //?
            data.put("Calendar", String.valueOf(habitCalendar.getDate())); //?  
        } catch (Exception ignored) {}

        try {
            collectionReference
                    .document(habitTitle.getText().toString()) //???
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Data has been added successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Data could not be added!" + e.toString());
                        }
                    });
        } catch (Exception ignored) {}
    }

}