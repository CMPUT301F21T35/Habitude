package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class EditHabitActivity extends AppCompatActivity {
    EditText habitTitle;
    EditText habitDescription;
    DatePicker habitCalendar;
    Habit changingHabit; //Talk about this
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("All Habits");
    ToggleButton sunBool, monBool, tueBool, wedBool, thuBool, friBool, satBool;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    final String TAG = "EditHabitActivity";
    ArrayList<ToggleButton> weekArray = new ArrayList<ToggleButton>();
    ArrayList<String> weekdays = new ArrayList<String>();

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

        weekArray.add(monBool); weekArray.add(tueBool); weekArray.add(wedBool); weekArray.add(thuBool); weekArray.add(friBool); weekArray.add(satBool); weekArray.add(sunBool);
        weekdays.add("Monday"); weekdays.add("Tuesday"); weekdays.add("Wednesday"); weekdays.add("Thursday"); weekdays.add("Friday"); weekdays.add("Saturday"); weekdays.add("Sunday");

        initializeFields();
    }

    public void initializeFields() {
        habitTitle.setText(changingHabit.getHabitTitleName());
        habitDescription.setText(changingHabit.getHabitReason());

        try {
            Date dateLiteral = formatter.parse(changingHabit.getHabitStartDate());
            //long dateLong = dateLiteral.getTime(); //???
            //https://www.baeldung.com/java-year-month-day
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateLiteral);
            habitCalendar.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<String> habitPlanArray = changingHabit.getPlan();
        for (int index = 0; index < 7; index++) {
            boolean contains = habitPlanArray.contains(weekdays.get(index));
            weekArray.get(index).setChecked(contains);
        }
    }

    public void doneButton(View view) {
        HashMap<String, String> data = new HashMap<>();
        manageReason(data);
        manageDate(data);
        managePlan(data);

        String oldTitle = changingHabit.getHabitTitleName();
        String newTitle = habitTitle.getText().toString();

        if (oldTitle.equals(newTitle)) {
            pushData(data);
        } else {
            renameAndPushData(data, oldTitle, newTitle);
        }

        onBackPressed();
    }

    private void renameAndPushData(HashMap<String, String> data, String oldTitle, String newTitle) {
        try {
            changingHabit.setHabitTitleName(habitTitle.getText().toString());
        } catch (Exception ignored) {}

        try {
            collectionReference
                    .document(newTitle)
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

            collectionReference
                    .document(oldTitle)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Data has been removed successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Data could not be removed!" + e.toString());
                        }
                    });
        } catch (Exception ignored) {}
    }

    private void managePlan(HashMap<String, String> data) {
        ArrayList<String> plan_data = changingHabit.getPlan(); //Currently not right
        data.put("Plan", String.valueOf(plan_data)); //Currently not right
        try {
            ArrayList<String> newPlan = new ArrayList<String>();
            getPlanValues(newPlan);
            String newPlanString = String.valueOf(newPlan).substring(1,String.valueOf(newPlan).length() - 1).replace(" ","");;
            data.put("Plan", newPlanString);
        } catch (Exception ignored) {}
    }

    private void getPlanValues (ArrayList<String> newPlan){
        for (int i = 0; i < 7; i++) {
            if (weekArray.get(i).isChecked()) {
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
            final String day = Integer.toString(habitCalendar.getDayOfMonth());
            final String month = Integer.toString(habitCalendar.getMonth()+1); //What's up with the month?
            final String year = Integer.toString(habitCalendar.getYear());
            final String habitStartDate = (year + "-" + month + "-" +day);
            data.put("Date", habitStartDate);
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
