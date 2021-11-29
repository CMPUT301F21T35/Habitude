package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    //https://developer.android.com/guide/topics/ui/layout/recyclerview
    RecyclerView habitList;
    CustomAdapter habitAdapter;
    RecyclerView.LayoutManager habitLayoutManager;
    ArrayList<Habit> habitDataList;


    private static MainActivity importantInstance;
    public static MainActivity getInstance(){
        return importantInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        NavigationBarView navigationBarView = findViewById(R.id.navigation_habits);
        navigationBarView.setOnItemSelectedListener(this);
        navigationBarView.setSelectedItemId(R.id.action_habits);

        importantInstance = this;

        Button addHabit = findViewById(R.id.addHabit);
        habitList = findViewById(R.id.habit_list);
        habitDataList = new ArrayList<>();
        habitAdapter = new CustomAdapter(habitDataList);
        habitLayoutManager = new LinearLayoutManager(this);
        setRecyclerViewLayoutManager();
        habitList.setAdapter(habitAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(habitAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(habitList);

        // connect to the firebase and get all the habits from the firebase
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("habits");

        collectionReference.orderBy("Index");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    String habitName = doc.getId();
                    String habitDate = (String) doc.getData().get("Date");
                    String habitReason = (String) doc.getData().get("Habit Reason");

                    //We use habitIndex to remember the arrangement of items in the list
                    Integer habitIndex = -1;
                    try {
                        Long indexLong = (Long) doc.getData().get("Index");
                        habitIndex = Math.toIntExact(indexLong);
                    } catch (Exception ignored) {}

                    if (doc.getData().get("Plan") != null) {
                        String[] WeekPlan = doc.getData().get("Plan").toString().split(",", 0);
                        ArrayList<String> habitWeekday = new ArrayList<>();
                        Collections.addAll(habitWeekday, WeekPlan);
                        Boolean isPublic = doc.getData().get("Is Public").equals("true");
                        habitDataList.add(new Habit(habitName,habitReason,habitDate,habitWeekday,habitIndex,isPublic)); // add all the habits into the habitList
                    }
                }
                habitAdapter.notifyDataSetChanged();

                // to check if the firebase has no habits
                if (habitDataList.size() == 0) {
                    Toast toast = Toast.makeText(MainActivity.this, "No habits!  Click the button at the top to add more.",Toast.LENGTH_LONG);
                    toast.show();
//                    TextView no_habits = findViewById(R.id.no_habits);
//                    no_habits.setVisibility(View.VISIBLE);
//                    no_habits.setText("No habits!  Click the button at the top to add more.");
                }
            }
        });

        // to add an habit
        Intent intent = new Intent(this,AddHabitActivity.class);
        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }

    //https://github.com/android/views-widgets-samples/blob/main/RecyclerView/Application/src/main/java/com/example/android/recyclerview/RecyclerViewFragment.java
    public void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (habitList.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) habitList.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        habitLayoutManager = new LinearLayoutManager(this); //getActivity()
        //mCurrentLayoutManagerType = LinearLayoutManager.LINEAR_LAYOUT_MANAGER;

        habitList.setLayoutManager(habitLayoutManager);
        habitList.scrollToPosition(scrollPosition);
    }

    // this shows that there are four buttons below the screen, Users can click either one of them to navigate to another activity
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
    
    public void editHabitFromIndex(int pos) {
        Intent intentEdit = new Intent(this, EditHabitActivity.class); //Used in CustomAdapter, comment on;
        Bundle bundle = new Bundle();
        bundle.putInt("habit_index", pos);
        intentEdit.putExtras(bundle); //is this redundant?
        startActivity(intentEdit);
    }

    //We're reaching this from onItemDismiss in CustomAdapter, deleting the appropriate habit.
    public void killIndex(int index) {
        Habit receivedHabit = habitDataList.get(index);
        if (receivedHabit != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("habits");

//update with proper location
            collectionReference
                    .document(receivedHabit.getHabitTitleName())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Data has been removed successfully!");
                            //clearHabitEvents(receivedHabit.getHabitTitleName()); //Finish later
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Data could not be removed!" + e.toString());
                        }
                    });
        }
    }

    public void updateIndices(Habit receivedHabit) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("habits");

        collectionReference
                .document(receivedHabit.getHabitTitleName())
                .update("Index",receivedHabit.getIndex());
    }
}