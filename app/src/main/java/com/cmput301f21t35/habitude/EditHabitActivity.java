package com.cmput301f21t35.habitude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class EditHabitActivity extends AppCompatActivity {
    EditText habitTitle;
    EditText habitDescription;
    EditText habitPlan;
    CalendarView habitCalendar;
    Habit changingHabit; //Talk about this
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("All Habits");
    View sunBool, monBool, tueBool, wedBool, thuBool, friBool, satBool;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    final String TAG = "Sample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);

        Bundle extras = getIntent().getExtras(); //is null???
        int changingHabitIndex = extras.getInt("habit_index");
        MainActivity mainActivity = MainActivity.getInstance();
        changingHabit = mainActivity.habitDataList.get(changingHabitIndex);

        habitTitle = findViewById(R.id.habit_title);
        habitDescription = findViewById(R.id.habit_description);
        //habitPlan = findViewById(R.id.habit_plan);
        habitCalendar = findViewById(R.id.habit_calendar);

        //TODO: Get the weekday info hooked up
        //This is a bad way of doing it
        sunBool = findViewById(R.id.sunday_button);
        monBool = findViewById(R.id.monday_button);
        tueBool = findViewById(R.id.tuesday_button);
        wedBool = findViewById(R.id.wednesday_button);
        thuBool = findViewById(R.id.thursday_button);
        friBool = findViewById(R.id.friday_button);
        satBool = findViewById(R.id.saturday_button);

        initializeFields();
    }

    public void initializeFields() {
        habitTitle.setText(changingHabit.getHabitTitleName());
        habitDescription.setText(changingHabit.getHabitReason());
        try {
            Date dateLiteral = formatter.parse(changingHabit.getHabitStartDate());
            //String dateString = formatter.format(dateLiteral);
            long dateLong = dateLiteral.getTime();
            habitCalendar.setDate(dateLong);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Views
    }

    public void doneButton(View view) {
        //TODO: changing title
        //TODO: changing description
        //TODO: changing plan
        //TODO: changing calendar
        //DATA: Habit Reason, Plan, Date
        HashMap<String, String> data = new HashMap<>();

        try {
            changingHabit.setHabitTitleName(habitTitle.getText().toString());
            //data.put("Title",habitTitle.getText().toString()); //This won't work - tweak
        } catch (Exception ignored) {}

        try {
            ArrayList<String> localHabitText = null;
            localHabitText.add(String.valueOf(habitPlan.getText()));
            changingHabit.setPlan(localHabitText);
            data.put("Plan",habitPlan.getText().toString());
        } catch (Exception ignored) {}

        //changingHabit.setWeekFromList(sunBool, monBool, tueBool, wedBool, thuBool, friBool, satBool);
        //data.put("Week",changingHabit.getWeekString()); //???

        manageReason(data);
        manageDate(data);
        managePlan(data);

        //If title not changed:
        pushData(data);
        //Else: renameAndPushData(data);

        onBackPressed();
    }

    private void managePlan(HashMap<String, String> data) {
        ArrayList<String> plan_data = changingHabit.getPlan(); //Currently not right
        data.put("Plan", String.valueOf(plan_data)); //Currently not right
        try {
            ArrayList<String> newPlan = new ArrayList<String>();
            getPlanValues(newPlan);
            data.put("Plan", String.valueOf(newPlan)); //Currently not right
        } catch (Exception ignored) {}
    }

    private void getPlanValues (ArrayList<String> newPlan){
        ArrayList<View> weekArray = new ArrayList<View>();
        weekArray.add(sunBool); weekArray.add(monBool); weekArray.add(tueBool); weekArray.add(wedBool); weekArray.add(thuBool); weekArray.add(friBool); weekArray.add(satBool);
        ArrayList<String> weekdays = new ArrayList<String>();
        weekdays.add("Sunday"); weekdays.add("Monday"); weekdays.add("Tuesday"); weekdays.add("Wednesday"); weekdays.add("Thursday"); weekdays.add("Friday"); weekdays.add("Saturday");
        for (int i = 0; i < 7; i++) {
            if (weekArray.get(i).isActivated()) {
                newPlan.add(weekdays.get(i));
            }
        }
    };

    private void manageReason(HashMap<String, String> data){
        data.put("Habit Reason",changingHabit.getHabitReason());

        try {
            changingHabit.setHabitReason(habitDescription.getText().toString());
            data.put("Habit Reason",habitDescription.getText().toString());
        } catch (Exception ignored) {}
    }

    private void manageDate(HashMap<String, String> data){
        data.put("Date",changingHabit.getHabitStartDate());

        try {
            long localDate = habitCalendar.getDate();
            changingHabit.setHabitStartDate(formatter.format(localDate));
            data.put("Date", String.valueOf(habitCalendar.getDate())); 
        } catch (Exception ignored) {}
    }

    private void pushData(HashMap<String, String> data) {
        try {
            collectionReference
                    .document(habitTitle.getText().toString())
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

    public void eventsButton(View view) { // clicking button pulls up events page
        Intent intent = new Intent(this, EventListActivity.class);
        startActivity(intent);
    }

}
