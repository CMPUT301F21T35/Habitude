package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    TextView name;
    TextView num_followers;
    TextView num_following;

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
        
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        name = findViewById(R.id.profile_name);
        num_followers = findViewById(R.id.profile_num_followers);
        num_following = findViewById(R.id.profile_num_following);

        name.setText(user.getDisplayName());

        CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("followers");
        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int count = 0;
                for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    count++;
                }
                num_followers.setText(String.valueOf(count));
            } else{
                Log.d(TAG, "error getting documents:", task.getException());
            }
        });

        collectionReference = db.collection("Users").document(user.getEmail()).collection("followings");
        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int count = 0;
                for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    count++;
                }
                num_following.setText(String.valueOf(count));
            } else{
                Log.d(TAG, "error getting documents:", task.getException());
            }
        });


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
