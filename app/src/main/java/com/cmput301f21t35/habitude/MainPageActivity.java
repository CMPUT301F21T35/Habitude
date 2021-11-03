package com.cmput301f21t35.habitude;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationBarView;

public class MainPageActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button all_habits = (Button) findViewById(R.id.all_habits);
        Button today_plans = findViewById(R.id.today_plan);

        Intent intent_all_habits = new Intent(this,MainActivity.class);
        all_habits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent_all_habits);
            }
        });

        Intent intent_today_plan = new Intent(this,TodayPlanActivity.class);
        today_plans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent_today_plan);
            }
        });

        NavigationBarView navigationBarView = findViewById(R.id.navigation);
        navigationBarView.setOnItemSelectedListener(this);
        navigationBarView.setSelectedItemId(R.id.action_profile);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case (R.id.action_today):
                Intent intent_today_plan = new Intent(this,TodayPlanActivity.class);
                startActivity(intent_today_plan);
                return true;
            case (R.id.action_habits):
                Intent intent_all_habits = new Intent(this,MainActivity.class);
                startActivity(intent_all_habits);
                return true;
            case (R.id.action_profile):
                return true;
            case (R.id.action_following):
                return true;
        }
        return false;
    }
}