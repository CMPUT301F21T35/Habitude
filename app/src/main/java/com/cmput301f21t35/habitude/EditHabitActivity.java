package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    Habit changingHabit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("habits");
    ToggleButton sunBool, monBool, tueBool, wedBool, thuBool, friBool, satBool;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    final String TAG = "EditHabitActivity";
    ArrayList<ToggleButton> weekArray = new ArrayList<ToggleButton>();
    ArrayList<String> weekdays = new ArrayList<String>();
    SwitchCompat habitIsPublic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Habit");

        //We get the relevant habit in this routine by pulling the index from the Main and indexing the list directly
        Bundle extras = getIntent().getExtras();
        int changingHabitIndex = extras.getInt("habit_index");
        MainActivity mainActivity = MainActivity.getInstance();
        changingHabit = mainActivity.habitDataList.get(changingHabitIndex);

        //We get the views we're going to be using, with the weekday buttons being retrieved during the next section.
        habitTitle = findViewById(R.id.habit_title);
        habitDescription = findViewById(R.id.habit_description);
        habitCalendar = findViewById(R.id.habit_calendar);
        habitIsPublic = findViewById(R.id.isPublic);

        //Set an array for the days of the week
        sunBool = findViewById(R.id.sunday_button);
        monBool = findViewById(R.id.monday_button);
        tueBool = findViewById(R.id.tuesday_button);
        wedBool = findViewById(R.id.wednesday_button);
        thuBool = findViewById(R.id.thursday_button);
        friBool = findViewById(R.id.friday_button);
        satBool = findViewById(R.id.saturday_button);

        weekArray.add(monBool); weekArray.add(tueBool); weekArray.add(wedBool); weekArray.add(thuBool); weekArray.add(friBool); weekArray.add(satBool); weekArray.add(sunBool);
        weekdays.add("Monday"); weekdays.add("Tuesday"); weekdays.add("Wednesday"); weekdays.add("Thursday"); weekdays.add("Friday"); weekdays.add("Saturday"); weekdays.add("Sunday");

        //We make sure that the fields contain the information we are supposed to on startup.
        initializeFields();
    }

    public void initializeFields() {
        //The title and description we can set directly.
        habitTitle.setText(changingHabit.getHabitTitleName());
        habitDescription.setText(changingHabit.getHabitReason());
        habitIsPublic.setChecked(changingHabit.isPublic());

        //We initialize the date using a Calendar object.
        try {
            Date dateLiteral = formatter.parse(changingHabit.getHabitStartDate());
            //https://www.baeldung.com/java-year-month-day
            //Author: baeldung, Date: 4 January 2020
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateLiteral);
            habitCalendar.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //We initialize the weekday checkboxes by looping over the arrays we set up.
        ArrayList<String> habitPlanArray = changingHabit.getPlan();
        for (int index = 0; index < 7; index++) {
            boolean contains = habitPlanArray.contains(weekdays.get(index));
            weekArray.get(index).setChecked(contains);
        }
    }

    public void doneButton(View view) {
        //We set up a HashMap to push to firebase.
        HashMap<String, Object> data = new HashMap<>();
        data.put("Index",changingHabit.getIndex());
        manageReason(data);
        manageDate(data);
        managePlan(data);
        managePublicity(data);

        //Check if the title has changed, so we know how to change the firebase.
        String oldTitle = changingHabit.getHabitTitleName();
        String newTitle = habitTitle.getText().toString();

        //If we've updated the title, we need to delete the old title and create a new file.
        //This is because the name of the habit is used as the title of the firestore data.
        if (oldTitle.equals(newTitle)) {
            pushData(data);
        } else {
            renameAndPushData(data, oldTitle, newTitle);
        }

        onBackPressed();
    }

    private void managePublicity(HashMap<String, Object> data) {
        //We get the reason string from the Habit class and put it as the "default" date to push.
        data.put("Is Public",changingHabit.getHabitReason());

        //We then read in the reason string from the appropriate EditText to possibly update the value.
        //I don't know if this can actually go wrong? If it can't, we don't need the first line or the try/catch.
        try {
            changingHabit.setPublic(habitIsPublic.isChecked());
            data.put("Is Public", habitIsPublic.isChecked());
        } catch (Exception ignored) {}
    }

    private void renameAndPushData(HashMap<String, Object> data, String oldTitle, String newTitle) {
        //Much like pushData, but we delete the old file and create the new file.
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

    private void managePlan(HashMap<String, Object> data) {
        //We get the reason string from the Habit class and put it as the "default" date to push.
        ArrayList<String> plan_data = changingHabit.getPlan();
        data.put("Plan", String.valueOf(plan_data));

        //We then read in the reason string from the getPlanValues function to possibly update the value.
        try {
            ArrayList<String> newPlan = new ArrayList<String>();
            getPlanValues(newPlan);
            String newPlanString = String.valueOf(newPlan).substring(1,String.valueOf(newPlan).length() - 1).replace(" ","");;
            data.put("Plan", newPlanString);
        } catch (Exception ignored) {}
    }

    private void getPlanValues (ArrayList<String> newPlan){
        //Feeds the string value of each checked weekday box into the ArrayList.
        for (int i = 0; i < 7; i++) {
            if (weekArray.get(i).isChecked()) {
                newPlan.add(weekdays.get(i));
            }
        }
    };

    private void manageReason(HashMap<String, Object> data){
        //We get the reason string from the Habit class and put it as the "default" date to push.
        data.put("Habit Reason",changingHabit.getHabitReason());

        //We then read in the reason string from the appropriate EditText to possibly update the value.
        //I don't know if this can actually go wrong? If it can't, we don't need the first line or the try/catch.
        try {
            changingHabit.setHabitReason(habitDescription.getText().toString());
            data.put("Habit Reason",habitDescription.getText().toString());
        } catch (Exception ignored) {}
    }

    private void manageDate(HashMap<String, Object> data){
        //We get the date from the Habit class and put it as the "default" date to push.
        data.put("Date",changingHabit.getHabitStartDate());

        //We then read in the date from the calendar to possibly update the value.
        try {
            final String day = Integer.toString(habitCalendar.getDayOfMonth());
            final String month = Integer.toString(habitCalendar.getMonth()+1); //What's up with the month?
            final String year = Integer.toString(habitCalendar.getYear());
            final String habitStartDate = (year + "-" + month + "-" +day);
            data.put("Date", habitStartDate);
        } catch (Exception ignored) {}
    }

    private void pushData(HashMap<String, Object> data) {
        //If we haven't changed the title, we can just push the data directly.
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

    /**
     * Click indicator button, it will show the visual indicator of that habit
     * @param view
     */
    public void indicatorButton(View view){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this,IndicatorActivity.class);
        intent.putExtra("HABITSRC", habitTitle.getText().toString());
        startActivity(intent);
    }

}
