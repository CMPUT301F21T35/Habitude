package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    //https://developer.android.com/guide/topics/ui/layout/recyclerview
    RecyclerView habitList;
    CustomAdapter habitAdapter;
    RecyclerView.LayoutManager habitLayoutManager;
    ArrayList<Habit> habitDataList;

    private FusedLocationProviderClient fusedLocationClient;
    private static MainActivity importantInstance;

    public static MainActivity getInstance() {
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

        //Location services
        //https://developer.android.com/training/location/retrieve-current
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Button addHabit = findViewById(R.id.addHabit);
        habitList = findViewById(R.id.habit_list);
        habitLayoutManager = new LinearLayoutManager(this); //? right activity
        habitDataList = new ArrayList<>();
        habitAdapter = new CustomAdapter(habitDataList);

        setRecyclerViewLayoutManager();
        habitList.setAdapter(habitAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(habitAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(habitList);

        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("All Habits");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String habitName = doc.getId();
                    String habitDate = (String) doc.getData().get("Date");
                    String habitReason = (String) doc.getData().get("Habit Reason");
                    Boolean habitPublicity;
                    try {
                        habitPublicity = doc.getData().get("Is Public").equals("true");
                    } catch (Exception ignored) {
                        habitPublicity = false;
                    }
                    if (doc.getData().get("Plan") != null) {
                        String[] WeekPlan = doc.getData().get("Plan").toString().split(",", 0);
                        ArrayList<String> habitWeekday = new ArrayList<>();
                        Collections.addAll(habitWeekday, WeekPlan);
                        habitDataList.add(new Habit(habitName, habitReason, habitDate, habitWeekday, habitPublicity));
                    }
                }
                habitAdapter.notifyDataSetChanged();
            }
        });

        Intent intent = new Intent(this, AddHabitActivity.class);
        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        //We view/edit habits by clicking on them, bundling the index for the next Activity to use.
//        Intent intentEdit = new Intent(this, EditHabitActivity.class);
//        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //intent.putExtra("habit",habitDataList.get(i));
//                Bundle bundle = new Bundle();
//                bundle.putInt("habit_index", i);
//                intentEdit.putExtras(bundle); //is this redundant?
//                startActivity(intentEdit);
//            }
//        });

        //We use long clicks to delete habits.
//        habitList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                new DeleteHabitFragment(habitDataList.get(i)).show(MainActivity.this.getSupportFragmentManager(), "DELETE_HABIT"); //deleting the first temporarily
//                return true; //Overrides normal click
//            }
//        });
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

    public String getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "void";
        }
        final String[] result = {"void"};
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            result[0] = location.toString();
                        }
                    }
                });
        return result[0];
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case (R.id.action_today):
                Intent intent_today_plan = new Intent(this,TodayPlanActivity.class);
                startActivity(intent_today_plan);
                return true;
            case (R.id.action_habits):
                return true;
            case (R.id.action_profile):
                return true;
            case (R.id.action_following):
                return true;
        }
        return false;
    }

    //We're reaching this from onItemDismiss in CustomAdapter, deleting the appropriate habit.
    public void killIndex(int index) {
        Habit receivedHabit = habitDataList.get(index);
        if (receivedHabit != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            final CollectionReference collectionReference = db.collection("All Habits");

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

    public void editHabitFromIndex(int pos) {
        Intent intentEdit = new Intent(this, EditHabitActivity.class); //Used in CustomAdapter, comment on;
        Bundle bundle = new Bundle();
        bundle.putInt("habit_index", pos);
        intentEdit.putExtras(bundle); //is this redundant?
        startActivity(intentEdit);
    }

    public void updateIndices(Habit receivedHabit) {
        HashMap<String, String> data = new HashMap<>();
        data.put("index", String.valueOf(receivedHabit.getIndex())); //?

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("All Habits");

        collectionReference
                .document(receivedHabit.getHabitTitleName())
                .set(data)
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