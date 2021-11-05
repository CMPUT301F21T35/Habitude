package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    boolean finished;
    // Habit photo
    LocalDate habitDateComplete;
    Button edit_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_habit_event_activity);

        edit_button = findViewById(R.id.habit_event_edit_button);
        edit_button.setText("Edit");

        Intent intent = getIntent();
        habitSrc = intent.getStringExtra("habit_id");
        eventTitle = intent.getStringExtra("event_id");
        getData();

    }

    @Override
    public void onOkPressed(Event newEvent) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("All Habits").document(habitSrc).collection("Events");

        String eventName = newEvent.getEventName();
        String eventDate = newEvent.getEventDate();
        String eventTime = newEvent.getEventTime();
        String eventComment = newEvent.getEventComment();
        Boolean eventFinished = newEvent.getEventFinished();

        // ensure inputs are all correct
        if (eventName.isEmpty() | eventDate.isEmpty() || eventTime.isEmpty() || eventComment.isEmpty()) {
            Toast.makeText(this, "Some fields are blank!", Toast.LENGTH_SHORT).show();
        } else { // otherwise add to db

            // create hashmap with all the attributes of event
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("Event Name", eventName);
            data.put("Date", eventDate);
            data.put("Time", eventTime);
            data.put("Comment", eventComment);
            data.put("Finished", eventFinished);

            // push to db

            collectionReference.document(eventTitle).delete();

            collectionReference.document(newEvent.getEventName())
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
            eventTitle = eventName;
            getData();
        }
    }


    /**
     * Get updated values from Firebase
     * TODO: Add time to values
     */
    private void getData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = db.collection("All Habits").document(habitSrc).collection("Events").document(eventTitle);

        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    event = document.getData();
                    System.out.println("Event found");

                    eventTitle = event.get("Event Name").toString();
                    eventComment = event.get("Comment").toString();
                    try {
                        eventDateStart = new SimpleDateFormat("yyyy-MM-dd").parse(event.get("Date").toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    finished = event.get("Finished").equals(true);

                    TextView habit_event_title_view = findViewById(R.id.habit_event_title);
                    TextView habit_event_reason_view = findViewById(R.id.habit_event_reason);
                    TextView habit_event_date_start_view = findViewById(R.id.habit_event_date_start);
                    TextView habit_event_finished = findViewById(R.id.habit_event_finished);
//        TextView habit_date_complete_view = findViewById(R.id.habit_date_complete);

                    habit_event_title_view.setText(eventTitle);
                    habit_event_reason_view.setText(eventComment);
                    habit_event_date_start_view.setText(eventDateStart.toString());
                    habit_event_finished.setText(finished ? "Finished" : "Not finished");
//        habit_date_complete_view.setText((habitDateComplete.toString()));


                    edit_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Event event_class = new Event(eventTitle, eventComment, event.get("Date").toString(), event.get("Time").toString(), finished);
                            new EditHabitEvent(habitSrc, event_class).show(getSupportFragmentManager(), "EDIT EVENT");
                        }
                    });


                } else {
                    Log.d(TAG, "No such document");
                    System.out.println("Event not found");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
                System.out.println("Failed to retrieve data");
            }
        });
    }
}
