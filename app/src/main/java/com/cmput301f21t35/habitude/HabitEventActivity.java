package com.cmput301f21t35.habitude;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class HabitEventActivity extends AppCompatActivity {
    ArrayList<Event> eventList;
    int position;
    Event event;
    String habitTitle;
    String habitComment;
    Date habitDateStart;
    // Habit photo
    LocalDate habitDateComplete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_habit_event_activity);

        Intent intent = getIntent();
        eventList = (ArrayList<Event>) intent.getSerializableExtra("DATA_LIST");
        position = intent.getIntExtra("POSITION", 0);
        event = eventList.get(position);

        TextView habit_title_view = findViewById(R.id.habit_title);
        TextView habit_reason_view = findViewById(R.id.habit_reason);
        TextView habit_date_start_view = findViewById(R.id.habit_date_start);
//        TextView habit_date_complete_view = findViewById(R.id.habit_date_complete);

        habitTitle = event.getEventName();
        habitComment = event.getEventComment();
        //habitDateStart = event.getEventDate();

        habit_title_view.setText(habitTitle);
        habit_reason_view.setText(habitComment);
        habit_date_start_view.setText(habitDateStart.toString());
//        habit_date_complete_view.setText((habitDateComplete.toString()));

    }
}
