package com.cmput301f21t35.habitude;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        NavigationBarView navigationBarView = findViewById(R.id.navigation_profile);
        navigationBarView.setOnItemSelectedListener(this);
        navigationBarView.setSelectedItemId(R.id.action_profile);

        logout = findViewById(R.id.logout_button);
        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            finish();
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_today):
                Intent intent_today_plan = new Intent(this,TodayPlanActivity.class);
                intent_today_plan.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent_today_plan.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent_today_plan);
                this.overridePendingTransition(0, 0);
                return true;
            case (R.id.action_habits):
                Intent intent_all_habits = new Intent(this, MainActivity.class);
                intent_all_habits.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent_all_habits.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent_all_habits);
                this.overridePendingTransition(0, 0);
                return true;
            case (R.id.action_profile):
                return true;
            case (R.id.action_following):
                Intent intent_following = new Intent(this, FollowingActivity.class);
                intent_following.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent_following.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent_following);
                this.overridePendingTransition(0, 0);
                return true;
        }
        return false;
    }
}