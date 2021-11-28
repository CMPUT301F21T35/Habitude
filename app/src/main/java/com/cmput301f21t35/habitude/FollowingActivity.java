package com.cmput301f21t35.habitude;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;

/**
 * the fourth navigation bar view following , to have three button
 * ->following to the followingListActivity
 * ->followers to the followersActivity
 * ->request to the pendingFollowersActivity
 */
public class FollowingActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private Button followingButton;
    private Button followersButton;
    private Button requestsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        followingButton = (Button) findViewById(R.id.following_Button);
        followersButton = (Button) findViewById(R.id.followers_Button);
        requestsButton = (Button) findViewById(R.id.requests_Button);

        NavigationBarView navigationBarView = findViewById(R.id.navigation_following);
        navigationBarView.setOnItemSelectedListener(this);
        navigationBarView.setSelectedItemId(R.id.action_following);

        // create onclick listeners for the buttons to take them to the other activities
        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FollowingListActivity.class);
                startActivity(intent);
            }
        });

        followersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_followers_activity = new Intent(getApplicationContext(),FollowerActivity.class);
                startActivity(intent_followers_activity);
            }
        });

        requestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_pending_followers_activity = new Intent(getApplicationContext(), PendingFollowerActivity.class);
                startActivity(intent_pending_followers_activity);

            }
        });


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_today):
                Intent intent_today_plan = new Intent(this,TodayPlanActivity.class);
                intent_today_plan.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent_today_plan.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent_today_plan);
                finish();
                this.overridePendingTransition(0, 0);
                return true;
            case (R.id.action_habits):
                Intent intent_all_habits = new Intent(this, MainActivity.class);
                intent_all_habits.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent_all_habits.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent_all_habits);
                finish();
                this.overridePendingTransition(0, 0);
                return true;
            case (R.id.action_profile):
                Intent intent_profile = new Intent(this, ProfileActivity.class);
                intent_profile.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent_profile.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent_profile);
                finish();
                this.overridePendingTransition(0, 0);
                return true;
            case (R.id.action_following):
                return true;
        }
        return false;
    }
}
