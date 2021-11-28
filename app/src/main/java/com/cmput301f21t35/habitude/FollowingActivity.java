package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ntang, echiu
 * this activity is one of the home tabs that will present the user with 3 options
 */

public class FollowingActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, FollowUserFragment.OnFragmentInteractionListener {

    /*
    private Button followingButton;
    private Button followersButton;
    private Button requestsButton;
     */

    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("People");

        // set top sub navigation
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewPager);
        adapterViewPager = new FollowingsNavPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /*
        followingButton = findViewById(R.id.following_Button);
        followersButton = findViewById(R.id.followers_Button);
        requestsButton = findViewById(R.id.requests_Button);
         */

        NavigationBarView navigationBarView = findViewById(R.id.navigation_following);
        navigationBarView.setOnItemSelectedListener(this);
        navigationBarView.setSelectedItemId(R.id.action_following);

        /*
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
         */


    }

    @Override
    public void onOkPressed(String toFollow) {
        // open database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // ensure inputs are all correct
        if (toFollow.isEmpty()) {
            Toast.makeText(this, "Some fields are blank!", Toast.LENGTH_SHORT).show();
        } else if(toFollow.equals(user.getEmail())) { // ensure email isn't own email
            Toast.makeText(this, "Cannot follow Yourself!", Toast.LENGTH_SHORT).show();
        } else {
            String userEmail = user.getEmail();
            Check1(toFollow, userEmail); // call nested function to run all the tests
        }
    }


    /**
     * This function checks that the person the user is trying to follow exists in the database
     * @param toFollow
     * this is a string of the user to be sent a request
     * @param userEmail
     * this is the email of the current user to be used in queries to the databse
     */

    private void Check1(String toFollow, String userEmail) {
        // check that the email exists in the database
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("Users").whereEqualTo("email", toFollow)
                .limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean isEmpty = task.getResult().isEmpty();
                            if (isEmpty) { // if empty it means user does not exist
                                Toast.makeText(FollowingActivity.this, "User does not exist!", Toast.LENGTH_SHORT).show();
                            } else { // run next test
                                Check2(toFollow, userEmail);
                            }
                        }
                    }
                });
    };

    /**
     * This function checks that the user has not already sent a request to this person
     * @param toFollow
     * this is a string of the user to be sent a request
     * @param userEmail
     * this is the email of the current user to be used in queries to the databse
     */
    private void Check2(String toFollow, String userEmail) {
        // check if there is already outgoing request

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("Users").document(userEmail).collection("followingsReq").whereEqualTo("email", toFollow)
                .limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            boolean isEmpty = task.getResult().isEmpty();
                            if (!isEmpty) { // if not empty it means they have already sent a request
                                Toast.makeText(FollowingActivity.this, "You have already requested!", Toast.LENGTH_SHORT).show();
                            } else { // run next test
                                Check3(toFollow, userEmail);
                            }
                        }
                    }
                });
    }

    /**
     * This function checks that the user is not already following this person
     * @param toFollow
     * this is a string of the user to be sent a request
     * @param userEmail
     * this is the email of the current user to be used in queries to the databse
     */
    private void Check3(String toFollow, String userEmail) {
        // check if they are already following
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("Users").document(userEmail).collection("followings").whereEqualTo("email", toFollow)
                .limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean isEmpty = task.getResult().isEmpty();
                            if (!isEmpty) { // if not empty it means they are already following
                                Toast.makeText(FollowingActivity.this, "You are already following!", Toast.LENGTH_SHORT).show();
                            } else { // if all tests pass add to database
                                addRequest(toFollow, userEmail);
                            }
                        }
                    }
                });
    }

    /**
     * If all the tests pass then the person will be sent a request and the database will be updated to reflect that
     * @param toFollow
     * this is a string of the user to be sent a request
     * @param userEmail
     * this is the email of the current user to be used in queries to the databse
     */
    private void addRequest(String toFollow, String userEmail) {
        // open database of user to send request to
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users").document(toFollow).collection("followersReq");

        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("email", userEmail);

        // push to db
        collectionReference.document(userEmail).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Data has been added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data has not been added successfully");
                    }
                });

        // add to current user that he has sent a request
        final CollectionReference collectionReference1 = db.collection("Users").document(userEmail).collection("followingsReq");

        HashMap<String, Object> data1 = new HashMap<String, Object>();
        data1.put("email", toFollow);

        // push to db
        collectionReference1.document(toFollow).set(data1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Data has been added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data has not been added successfully");
                    }
                });

        Toast.makeText(FollowingActivity.this, "Request sent!", Toast.LENGTH_SHORT).show();
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
