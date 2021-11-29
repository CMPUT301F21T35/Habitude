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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * this activity is used to add new habit into the firestore database
 */
public class AddHabitActivity extends AppCompatActivity {
    private EditText habitName;
    private EditText habitReason;
    private DatePicker dateStart;
    private ToggleButton monday;
    private ToggleButton tuesday;
    private ToggleButton wednesday;
    private ToggleButton thursday;
    private ToggleButton friday;
    private ToggleButton saturday;
    private ToggleButton sunday;
    ArrayList<String> habitPlan = new ArrayList<>();
    private SwitchCompat isPublic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Habit");
        //initilize the firestore database used for save datas
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // collectionrefernce path to the current user's habits
        final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("habits");
        // initilize the activity element by find id in layout
        habitName = findViewById(R.id.habitName);
        habitReason = findViewById(R.id.habitReason);
        dateStart = findViewById(R.id.datePicker);
        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);
        sunday = findViewById(R.id.sunday);
        isPublic = findViewById(R.id.isPublic);
        Button createButton = (Button) findViewById(R.id.createButton);
        Intent intent = new Intent(this, MainActivity.class);
        //set on click event for the CREATE button
        createButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //check the input is empty or not 
                if (habitName.getText().length() == 0 || habitReason.getText().length() == 0) {
                    Toast.makeText(AddHabitActivity.this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
                } else {
                    final String habitTitleName = habitName.getText().toString();
                    final String Reason = habitReason.getText().toString();
                    final String day = Integer.toString(dateStart.getDayOfMonth());
                    final String month = Integer.toString(dateStart.getMonth());
                    final String year = Integer.toString(dateStart.getYear());
                    final String habitStartDate = (year + "-" + month + "-" + day);
                    setHabitPlan();
                    final String habitPlan_toString = String.valueOf(habitPlan);
                    final String habitPlan_final = habitPlan_toString.substring(1, habitPlan_toString.length() - 1).replace(" ", "");
                    // save data into a hashmap
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("Habit Reason", Reason);
                    data.put("Date", habitStartDate);
                    data.put("Plan", habitPlan_final);
                    data.put("Is Public", isPublic.isChecked());
                    data.put("Index", -1);
                    // set the firestore database
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
            }
        });
    }

    /**
     * add the plan date into the habitplan if the checkbox is checked
     */
    public void setHabitPlan() {
        if (sunday.isChecked()) {
            habitPlan.add("Sunday");
        }

        if (monday.isChecked()) {
            habitPlan.add("Monday");
        }

        if (tuesday.isChecked()) {
            habitPlan.add("Tuesday");
        }
        if (wednesday.isChecked()) {
            habitPlan.add("Wednesday");
        }

        if (thursday.isChecked()) {
            habitPlan.add("Thursday");
        }

        if (friday.isChecked()) {
            habitPlan.add("Friday");
        }

        if (saturday.isChecked()) {
            habitPlan.add("Saturday");
        }
    }

}
