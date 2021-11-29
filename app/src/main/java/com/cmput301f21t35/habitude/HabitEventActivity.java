package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HabitEventActivity extends AppCompatActivity implements EditHabitEvent.OnFragmentInteractionListener {
    Map event;
    String habitSrc;
    String eventTitle;
    String eventComment;
    Date eventDateStart;
    String eventTime;
    boolean finished;
    // Habit photo
    LocalDate habitDateComplete;
    Button edit_button;
    String eventGeolocation;

    /**
     * Create the activity
     * @param savedInstanceState Bundle
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit_event);

        edit_button = findViewById(R.id.habit_event_edit_button);
        edit_button.setText("Edit");

        Intent intent = getIntent();
        habitSrc = intent.getStringExtra("habit_id");
        eventTitle = intent.getStringExtra("event_id");
        getData();

    }

    /**
     * Update event when fragment OK pressed
     * @param newEvent new event information
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onOkPressed(Event newEvent) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("habits").document(habitSrc).collection("Events");

        String eventName = newEvent.getEventName();
        String eventDate = newEvent.getEventDate();
        String eventTime = newEvent.getEventTime();
        String eventComment = newEvent.getEventComment();
        Boolean eventFinished = newEvent.getEventFinished();
        String eventGeolocation = newEvent.getEventGeolocation(); //new


        // Make sure all fields are filled
        if (eventName.isEmpty() | eventDate.isEmpty() || eventTime.isEmpty()) {
            Toast.makeText(this, "Some fields are blank!", Toast.LENGTH_SHORT).show();
        } else {
            // Create hashmap with all the attributes of event
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("Event Name", eventName);
            data.put("Date", eventDate);
            data.put("Time", eventTime);
            data.put("Comment", eventComment);
            data.put("Finished", eventFinished);
            data.put("Geolocation",eventGeolocation);


            // Delete old event and update database with new information
            collectionReference.document(eventTitle).delete();
            collectionReference.document(newEvent.getEventName())
                    .set(data)
                    .addOnSuccessListener(unused -> Log.d(TAG, "Data has been added successfully"))
                    .addOnFailureListener(e -> Log.d(TAG, "Data has not been added successfully"));
            eventTitle = eventName;
            getData();
        }
    }


    /**
     * Get updated values from Firebase and insert into layout view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DocumentReference documentReference = db.collection("Users").document(user.getEmail()).collection("habits").document(habitSrc).collection("Events").document(eventTitle);

        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                    // Get document data
                    event = document.getData();
                    eventTitle = event.get("Event Name").toString();
                    eventComment = event.get("Comment").toString();
                    try {
                        eventDateStart = new SimpleDateFormat("yyyy-MM-dd").parse(event.get("Date").toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    finished = event.get("Finished").equals(true);
                    eventTime = event.get("Time").toString();
                    eventGeolocation = event.get("Geolocation").toString();

                    // Get layout views
                    TextView habit_event_title_view = findViewById(R.id.habit_event_title);
                    TextView habit_event_reason_view = findViewById(R.id.habit_event_reason);
                    TextView habit_event_date_view = findViewById(R.id.habit_event_date);
                    TextView habit_event_finished = findViewById(R.id.habit_event_finished);
                    TextView habit_event_geolocation = findViewById(R.id.habit_event_geolocation);
                    // TextView habit_event_time = findViewById(R.id.habit_event_time);

                    // Set values in layout
                    habit_event_title_view.setText(eventTitle);
                    habit_event_reason_view.setText(eventComment);
                    habit_event_date_view.setText(new SimpleDateFormat("yyyy-MM-dd").format(eventDateStart) + " at " + eventTime);
                    habit_event_finished.setText(finished ? "Finished" : "Not finished");
                    habit_event_geolocation.setText(new StringBuilder().append("Location: ").append(eventGeolocation).toString());
                    // habit_event_time.setText(eventTime);

                    // Create edit event fragment when edit button is clicked
                    edit_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Event event_class = new Event(eventTitle, eventComment, event.get("Date").toString(), event.get("Time").toString(), finished);
                            new EditHabitEvent(habitSrc, event_class).show(getSupportFragmentManager(), "EDIT EVENT");
                        }
                    });
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }
}
