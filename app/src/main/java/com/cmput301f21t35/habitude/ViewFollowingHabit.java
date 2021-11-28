package com.cmput301f21t35.habitude;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class ViewFollowingHabit extends AppCompatActivity {

    private Button backButton;
    private ListView followingHabitList;
    private ArrayAdapter<Habit> followingHabitAdapter;
    private ArrayList<Habit> followingHabitDataList;
    private String followingUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_following_habit);

        backButton = findViewById(R.id.fl_back);
        followingHabitList = (ListView) findViewById(R.id.fl_habit);

        followingHabitDataList = new ArrayList<>();
        followingHabitAdapter = new HabitList(this,followingHabitDataList);
        followingHabitList.setAdapter(followingHabitAdapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            followingUser = extras.getString("UserName");
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users").document(followingUser).collection("habits");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                followingHabitDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    String habitName = doc.getId();
                    String habitDate = (String) doc.getData().get("Date");
                    String habitReason = (String) doc.getData().get("Habit Reason");
                    if (doc.getData().get("Plan") != null) {
                        String[] WeekPlan = doc.getData().get("Plan").toString().split(",", 0);
                        ArrayList<String> habitWeekday = new ArrayList<>();
                        Collections.addAll(habitWeekday, WeekPlan);
                        int index = 1;
                        Boolean isPublic = (Boolean) doc.getData().get("Is Public");
                        followingHabitDataList.add(new Habit(habitName,habitReason,habitDate,habitWeekday,index,isPublic)); // add all the habits into the habitList
                    }
                }
                followingHabitAdapter.notifyDataSetChanged();
            }
        });

        Intent flIndicator = new Intent(this,IndicatorActivity.class);
        followingHabitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                flIndicator.putExtra("HABITSRC",followingHabitDataList.get(position).getHabitTitleName());
                flIndicator.putExtra("followingUser",followingUser);
                startActivity(flIndicator);
            }
        });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}