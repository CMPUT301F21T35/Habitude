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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("habits").document(habitSrc).collection("Events");

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
                    Boolean eventFinished = (Boolean) doc.getData().get("Finished");
                    eventDataList.add(new Event(eventName,eventComment,eventDate,eventTime,eventFinished));
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

        eventList.setOnItemClickListener(((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, HabitEventActivity.class);
            intent.putExtra("habit_id", habitSrc);
            intent.putExtra("event_id", eventDataList.get(i).getEventName());
            startActivity(intent);
        }));

        eventList.setOnItemLongClickListener(((adapterView, view, i, l) -> {
            new DeleteHabitEventFragment(habitSrc, eventDataList.get(i)).show(EventListActivity.this.getSupportFragmentManager(), "DELETE EVENT");
            return true;
        }));

    }

    @Override
    public void onOkPressed(Event newEvent) {
        // open database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("habits").document(habitSrc).collection("Events");

        String eventName = newEvent.getEventName();;
        String eventDate = newEvent.getEventDate();
        String eventTime = newEvent.getEventTime();
        String eventComment = newEvent.getEventComment();
        Boolean eventFinished = newEvent.getEventFinished();

        // ensure inputs are all correct
        if(eventName.isEmpty() || eventDate.isEmpty() || eventTime.isEmpty() || eventComment.isEmpty()) {
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
}