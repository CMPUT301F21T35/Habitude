package com.cmput301f21t35.habitude;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
<<<<<<< HEAD

public class MainActivity extends AppCompatActivity {

=======

public class MainActivity extends AppCompatActivity implements AddHabitEvent.OnFragmentInteractionListener {

>>>>>>> parent of 462a25e (Merge branch 'jingsheng' into echiu)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
<<<<<<< HEAD

        Button addHabit = findViewById(R.id.addHabit);
        habitList = findViewById(R.id.habit_list);
        habitDataList = new ArrayList<>();
        habitAdapter = new HabitList(this,habitDataList);
        habitList.setAdapter(habitAdapter);

        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("All Habits");

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
                        habitDataList.add(new Habit(habitName,habitReason,habitDate,habitWeekday));
                    }
                }
                habitAdapter.notifyDataSetChanged();
            }
        });
         */
        Intent intent = new Intent(this,AddHabitActivity.class);
        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
=======

        new AddHabitEvent().show(getSupportFragmentManager(), "ADD EVENT");
=======

        new AddHabitEvent().show(getSupportFragmentManager(), "ADD EVENT");
    }

    @Override
    public void onOkPressed(Event newEvent) {

>>>>>>> parent of 462a25e (Merge branch 'jingsheng' into echiu)
    }

    @Override
    public void onOkPressed(Event newEvent) {

>>>>>>> parent of 462a25e (Merge branch 'jingsheng' into echiu)
    }

}