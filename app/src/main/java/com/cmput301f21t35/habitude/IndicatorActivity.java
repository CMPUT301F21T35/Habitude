package com.cmput301f21t35.habitude;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class IndicatorActivity extends AppCompatActivity {
    String habitSrc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator);


        Bundle extras = getIntent().getExtras(); //Get the habit name
        if(extras != null) { // check whether it is a null object
            habitSrc = extras.getString("HABITSRC"); // get name of the habit
        }

        // use MCalendarView to highlight the date from calendar
        sun.bob.mcalendarview.MCalendarView mCalendarView = findViewById(R.id.calendar_indicator);


        // find the right habit and its events
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("All Habits").document(habitSrc).collection("Events");


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String eventName = doc.getId();
                    String date = (String) doc.getData().get("Date");
                    String[] dateResult = date.split("-0*"); // collect the year, moth and date from the date string format
                    Boolean finish = (Boolean) doc.getData().get("Finished");
                    if (doc.getData().get("Finished") != null) { // to make sure the boolean is not null
                        if (finish) { // if the event is finished, we will highlight the date on the calendar
                            mCalendarView.markDate(Integer.parseInt(dateResult[0]), Integer.parseInt(dateResult[1]), Integer.parseInt(dateResult[2]));
                        }
                    }
                }
            }
        });
    }
}