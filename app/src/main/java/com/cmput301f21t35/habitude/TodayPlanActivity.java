package com.cmput301f21t35.habitude;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class TodayPlanActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    ListView today_habitList;
    ArrayAdapter<Habit> today_habitAdapter;
    ArrayList<Habit> today_habitDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_plan);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Today");

        NavigationBarView navigationBarView = findViewById(R.id.navigation_today);
        navigationBarView.setOnItemSelectedListener(this);
        navigationBarView.setSelectedItemId(R.id.action_today);

        today_habitList = findViewById(R.id.today_plan_list);
        today_habitDataList = new ArrayList<>();
        today_habitAdapter = new HabitList(this,today_habitDataList);
        today_habitList.setAdapter(today_habitAdapter);

        // connect to firebase and search the desired habits inside the firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("habits");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                today_habitDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    String habitName = doc.getId();
                    String habitDate = (String) doc.getData().get("Date");
                    String habitReason = (String) doc.getData().get("Habit Reason");
                    stringToDate(habitDate); // change the string to a date format
                    Date current_date = Calendar.getInstance().getTime();

                    if (doc.getData().get("Plan") != null) {
                        String[] WeekPlan = doc.getData().get("Plan").toString().split(",", 0);// get the plan of the habits
                        ArrayList<String> habitWeekday = new ArrayList<>();
                        Collections.addAll(habitWeekday, WeekPlan);
                        String weekday_name = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
                        Habit todayHabit = new Habit(habitName,habitReason,habitDate,habitWeekday);

                        if (stringToDate(habitDate).before(current_date)) { // check if the date of habit is before the current date
                            for (int i = 0; i < todayHabit.getPlan().size(); i++) {
                                if (weekday_name.equals(todayHabit.getPlan().get(i))) { // check if the plan of weekday is equal to today's weekday
                                    today_habitDataList.add(todayHabit); // add the habit object into the list
                                    today_habitAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }

                if (today_habitDataList.size() == 0) { // if there is no habit to do today, it will tell the users that they don't have any plan for today
                    TextView today_no_habit = findViewById(R.id.today_no_habits);
                    today_no_habit.setVisibility(View.VISIBLE);
                    today_no_habit.setText("No habits planned for today!");
                }
            }

        });

    }

    /**
     * This will change the format of the string to the Date
     * @param habitDate
     * @return
     *      returnType is Date
     */
    public static Date stringToDate(String habitDate){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        Date date = null;
        try {
            date = format.parse(habitDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // this shows that there are four buttons below the screen, Users can click either one of them to navigate to another activity
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_today):
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