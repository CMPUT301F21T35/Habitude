package com.cmput301f21t35.habitude;


import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;

public class AddHabitEvent extends DialogFragment {
    private EditText eventName;
    private EditText eventComment;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private ToggleButton geoPicker;
    private OnFragmentInteractionListener listener;


    public interface OnFragmentInteractionListener {
        void onOkPressed(Event newEvent);
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

        geoPicker = view.findViewById(R.id.geolocation_button);

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
                        String month = Integer.toString(datePicker.getMonth());
                        String day = Integer.toString(datePicker.getDayOfMonth());
                        String eventDate = year + "-" + month + "-" + day;
                        String eventTime = timePicker.getHour() + " " + ":" + " " + timePicker.getMinute();
                        boolean geoPrompt = geoPicker.isChecked();
                        listener.onOkPressed(onOkHandle(name, comment, eventDate, eventTime, geoPrompt));
                    }
                }).create();
    }

    public Event onOkHandle(String name, String comment, String eventDate, String eventTime, boolean geoPrompt) {
        String eventLocation = "void";

        if (geoPrompt) {
            MainActivity mainActivity = MainActivity.getInstance();
            eventLocation = mainActivity.getLocation();
            Log.v("LOCATION",mainActivity.getLocation());
        }

        return new Event(name, comment, eventDate, eventTime);
    }
}