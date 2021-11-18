package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
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

    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;


    private static MainActivity importantInstance;
    public static MainActivity getInstance(){
        return importantInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationBarView navigationBarView = findViewById(R.id.navigation_habits);
        navigationBarView.setOnItemSelectedListener(this);
        navigationBarView.setSelectedItemId(R.id.action_habits);

        importantInstance = this;

        Button addHabit = findViewById(R.id.addHabit);
        habitList = findViewById(R.id.habit_list);
        habitDataList = new ArrayList<>();
        habitAdapter = new HabitList(this,habitDataList);
        habitList.setAdapter(habitAdapter);

        // connect to the firebase and get all the habits from the firebase
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("All Habits");

        //
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    String habitName = doc.getId();
                    String habitDate = (String) doc.getData().get("Date");
                    String habitReason = (String) doc.getData().get("Habit Reason");
                    if (doc.getData().get("Plan") != null) {
                        String[] WeekPlan = doc.getData().get("Plan").toString().split(",", 0);
                        ArrayList<String> habitWeekday = new ArrayList<>();
                        Collections.addAll(habitWeekday, WeekPlan);
                        habitDataList.add(new Habit(habitName,habitReason,habitDate,habitWeekday)); // add all the habits into the habitList
                    }
                }
                habitAdapter.notifyDataSetChanged();

                // to check if the firebase has no habits
                if (habitDataList.size() == 0) {
                    TextView no_habits = findViewById(R.id.no_habits);
                    no_habits.setVisibility(View.VISIBLE);
                    no_habits.setText("No habits!  Click the button at the top to add more.");
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


        //We view/edit habits by clicking on them, bundling the index for the next Activity to use.
        Intent intentEdit = new Intent(this,EditHabitActivity.class);
        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //intent.putExtra("habit",habitDataList.get(i));
                Bundle bundle = new Bundle();
                bundle.putInt("habit_index",i);
                intentEdit.putExtras(bundle); //is this redundant?
                startActivity(intentEdit);
            }
        });

        //We use long clicks to delete habits.
        habitList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new DeleteHabitFragment(habitDataList.get(i)).show(MainActivity.this.getSupportFragmentManager(), "DELETE_HABIT"); //deleting the first temporarily
                return true; //Overrides normal click
            }
        });
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
                return true;
            case (R.id.action_habits):
                return true;
            case (R.id.action_profile):
                Intent intent_profile = new Intent(this, ProfileActivity.class);
                intent_profile.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent_profile.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent_profile);
                return true;
            case (R.id.action_following):
                Intent intent_following = new Intent(this, FollowingActivity.class);
                intent_following.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent_following.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent_following);
                return true;
        }
        return false;
    }
}