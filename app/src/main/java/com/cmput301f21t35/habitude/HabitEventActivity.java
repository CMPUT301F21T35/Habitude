package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HabitEventActivity extends AppCompatActivity {
    Map event;
    String habitSrc;
    String eventTitle;
    String eventComment;
    Date eventDateStart;
    boolean finished;
    // Habit photo
    LocalDate habitDateComplete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_habit_event_activity);

        Intent intent = getIntent();
        habitSrc = intent.getStringExtra("habit_id");
        eventTitle = intent.getStringExtra("event_id");

        System.out.println("Habit: " + habitSrc);
        System.out.println("Event: " + eventTitle);

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
