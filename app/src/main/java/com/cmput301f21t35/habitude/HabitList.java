package com.cmput301f21t35.habitude;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HabitList extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habits;
    private Context context;

    public HabitList(Context context, ArrayList<Habit> habits){
        super(context,0,habits);
        this.habits = habits;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.habit_content, parent,false);
        }
        Habit habit = habits.get(position);
        TextView habitName = view.findViewById(R.id.habit_text);
        Button eventsButton = view.findViewById(R.id.events_button);
        // set listener to get position for button whenever it is clicked
        eventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Habit buttonHabit = habits.get(position);
                String title = buttonHabit.getHabitTitleName();
                Bundle bundle = new Bundle();
                Intent intent = new Intent(context, EventListActivity.class);
                // temp solution to get habit name, need less abusable way later
                intent.putExtra("HABITSRC", title);
                context.startActivity(intent);
            }
        });

        if (habit != null){
            habitName.setText(habit.getHabitTitleName());
        }
        return view;
    }
}
