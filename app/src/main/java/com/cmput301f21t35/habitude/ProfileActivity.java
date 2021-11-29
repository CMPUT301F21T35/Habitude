package com.cmput301f21t35.habitude;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("You");


        NavigationBarView navigationBarView = findViewById(R.id.navigation_profile);
        navigationBarView.setOnItemSelectedListener(this);
        navigationBarView.setSelectedItemId(R.id.action_profile);

        name = findViewById(R.id.profile_name);

        name.setText(user.getDisplayName());
    }


    /**
     * Delete the user information from shared preferences on logout
     */
    private void deleteUID() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * Shows the overflow menu on the toolbar
     * @param menu overflow menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_profile, menu);
        return true;
    }

    /**
     * When overflow menu options are clicked
     * @param item menu item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            deleteUID();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_today):
                Intent intent_today_plan = new Intent(this, TodayPlanActivity.class);
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
                return true;
            case (R.id.action_following):
                Intent intent_following = new Intent(this, FollowingActivity.class);
                intent_following.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent_following.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent_following);
                finish();
                this.overridePendingTransition(0, 0);
                return true;
        }
        return false;
    }
}