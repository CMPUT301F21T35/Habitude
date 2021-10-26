package com.cmput301f21t35.habitude;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainPageActivity extends AppCompatActivity {

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
    }
}