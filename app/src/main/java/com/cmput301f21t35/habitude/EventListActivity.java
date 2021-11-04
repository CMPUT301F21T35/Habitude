package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class EventListActivity extends AppCompatActivity implements AddHabitEvent.OnFragmentInteractionListener {

    ListView eventList;
    ArrayAdapter<Event> eventArrayAdapter;
    ArrayList<Event> eventDataList;
    String habitSrc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            // get the name of the habit to use when querying the events
            habitSrc = extras.getString("HABITSRC");
        }

        // open database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("All Habits").document(habitSrc).collection("Events");

        // loop through database and add all existing events to the eventList to show
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                eventDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    String eventName = (String) doc.getId();
                    String eventDate = (String) doc.getData().get("Date");
                    String eventTime = (String) doc.getData().get("Time");
                    String eventComment = (String) doc.getData().get("Comment");
                    eventDataList.add(new Event(eventName,eventComment,eventDate,eventTime));
                }
                eventArrayAdapter.notifyDataSetChanged();
            }
        });

        eventList = findViewById(R.id.events_list);
        eventDataList = new ArrayList<>();

        eventArrayAdapter = new EventList(this, eventDataList);

        eventList.setAdapter(eventArrayAdapter);

        final FloatingActionButton addEventButton = findViewById(R.id.add_event_button);

        // set event listener to open the fragment
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddHabitEvent().show(getSupportFragmentManager(), "ADD EVENT");
            }
        });
    }

    @Override
    public void onOkPressed(Event newEvent) {
        // open database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("All Habits").document(habitSrc).collection("Events");

        // create hashmap with all the attributes of event
        HashMap<String, String> data = new HashMap<>();
        data.put("Event Name", newEvent.getEventName());
        data.put("Date", newEvent.getEventDate());
        data.put("Time", newEvent.getEventTime());
        data.put("Comment", newEvent.getEventComment());

        // push to db
        collectionReference.document(newEvent.getEventName()).set(data)
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
        eventArrayAdapter.notifyDataSetChanged();
    }
}