package com.cmput301f21t35.habitude;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EventListActivity extends AppCompatActivity implements AddHabitEvent.OnFragmentInteractionListener {

    ListView eventList;
    ArrayAdapter<Event> eventArrayAdapter;
    ArrayList<Event> eventDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        eventList = findViewById(R.id.events_list);
        eventDataList = new ArrayList<>();

        eventArrayAdapter = new EventList(this, eventDataList);

        eventList.setAdapter(eventArrayAdapter);

        final FloatingActionButton addEventButton = findViewById(R.id.add_event_button);

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddHabitEvent().show(getSupportFragmentManager(), "ADD EVENT");
            }
        });
    }

    // TODO: INTEGRATE WITH FIREBASE

    @Override
    public void onOkPressed(Event newEvent) {
        eventArrayAdapter.add(newEvent);
        eventArrayAdapter.notifyDataSetChanged();
    }
}