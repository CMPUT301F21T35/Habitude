package com.cmput301f21t35.habitude;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements AddHabitEvent.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AddHabitEvent().show(getSupportFragmentManager(), "ADD EVENT");
    }

    @Override
    public void onOkPressed(Event newEvent) {

    }
}