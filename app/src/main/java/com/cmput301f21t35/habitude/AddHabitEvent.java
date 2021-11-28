package com.cmput301f21t35.habitude;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.content.Intent;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * @author echiu
 * this is a fragment that is used to ask users for the inputs to make a new habit event
 */

public class AddHabitEvent extends DialogFragment {
    private EditText eventName;
    private EditText eventComment;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private CheckBox eventFinished;
    private OnFragmentInteractionListener listener;
    private Button geolocationButton;

    String geolocation = "null"; //We create this here so we don't have to worry about if it gets updated or not.

    public interface OnFragmentInteractionListener {
        void onOkPressed(Event newEvent); // notify EventListActivity that a new event has been made
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_habit_event, null);
        eventName = view.findViewById(R.id.event_name_editText);
        eventComment = view.findViewById(R.id.event_comment_editText);
        datePicker = view.findViewById(R.id.event_date);
        timePicker = view.findViewById(R.id.event_time);
        eventFinished = view.findViewById(R.id.event_finished);

        geolocationButton = view.findViewById(R.id.geolocation_button);
        geolocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.getInstance(), MapsActivity.class);
                startActivityForResult(intent,1); //Modernize?
            }
        });

        // set up the fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = eventName.getText().toString();
                        String comment = eventComment.getText().toString();
                        String year = Integer.toString(datePicker.getYear());
                        String month = Integer.toString(datePicker.getMonth()+1);
                        String day = Integer.toString(datePicker.getDayOfMonth());
                        String eventDate = year + "-" + month + "-" + day;
                        String eventTime = timePicker.getHour() + " " + ":" + " " + timePicker.getMinute();
                        Boolean finished = eventFinished.isChecked();
                        listener.onOkPressed(new Event(name, comment,eventDate,eventTime,finished,geolocation));
                    }
                }).create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            geolocation = data.getStringExtra("keyName");
            //Log.v("Tagalog",geolocation);
        } catch (Exception ignored) {}
    }
}
