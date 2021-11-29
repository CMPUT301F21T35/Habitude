package com.cmput301f21t35.habitude;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ViewFollowingHabitList extends ArrayAdapter<Habit> {
    private ArrayList<Habit> habits;
    private Context context;

    public ViewFollowingHabitList(Context context, ArrayList<Habit> habits){
        super(context,0,habits);
        this.habits = habits;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.view_following_habit_content, parent,false);
        }
        Habit habit = habits.get(position);
        TextView habitName = view.findViewById(R.id.habit_text);
        if (habit != null){
            habitName.setText(habit.getHabitTitleName());
        }
        return view;
    }
}
