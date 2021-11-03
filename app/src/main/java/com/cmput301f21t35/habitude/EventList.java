package com.cmput301f21t35.habitude;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301f21t35.habitude.Event;

import java.util.ArrayList;

public class EventList extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;

    public EventList(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.events = events;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.event_context, parent, false);
        }
        Event event = events.get(position);
        TextView cityName = view.findViewById(R.id.name_text);
        TextView provinceName = view.findViewById(R.id.reason_text);
        cityName.setText(event.getEventName());
        provinceName.setText(event.getEventComment());
        return view;
    }
}