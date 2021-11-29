package com.cmput301f21t35.habitude;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewHabitActivity extends AppCompatActivity {

    Habit habit;
    int habitIndex;
    String habitSrc = "";
    String followingUser = "";
    TextView title;
    TextView description;
    TextView habitProgress;
    sun.bob.mcalendarview.MCalendarView mCalendarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_habit);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Habit");

        Bundle extras = getIntent().getExtras();
        habitIndex = extras.getInt("habit_index");
        followingUser = extras.getString("followingUser");
        MainActivity mainActivity = MainActivity.getInstance();
        habit = mainActivity.habitDataList.get(habitIndex);

        title = findViewById(R.id.view_habit_title);
        description = findViewById(R.id.view_habit_description);
        habitProgress = findViewById(R.id.habit_progress);

        title.setText(habit.getHabitTitleName());
        description.setText(habit.getHabitReason());
        habitProgress.setText(followingUser != null ? "Their progress:" : "Your progress:");

        habitSrc = habit.getHabitTitleName();

        // use MCalendarView to highlight the date from calendar
        mCalendarView = findViewById(R.id.progress_calendar);

        //clear all the date once this activity restarts
        mCalendarView.getMarkedDates().getAll().clear();

        // find the right habit and its events
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (followingUser != null) {
            final CollectionReference collectionReference = db.collection("Users").document(followingUser).collection("habits").document(habitSrc).collection("Events");
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String eventName = doc.getId();
                        String date = (String) doc.getData().get("Date");
                        String[] dateResult = date.split("-0*"); // collect the year, moth and date from the date string format
                        Boolean finish = (Boolean) doc.getData().get("Finished");
                        if (doc.getData().get("Finished") != null) { // to make sure the boolean is not null
                            if (finish) { // if the event is finished, we will highlight the date on the calendar
                                mCalendarView.markDate(Integer.parseInt(dateResult[0]), Integer.parseInt(dateResult[1]), Integer.parseInt(dateResult[2]));
                            }
                        }
                    }
                }
            });
        } else {
            final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("habits").document(habitSrc).collection("Events");
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String eventName = doc.getId();
                        String date = (String) doc.getData().get("Date");
                        String[] dateResult = date.split("-0*"); // collect the year, moth and date from the date string format
                        Boolean finish = (Boolean) doc.getData().get("Finished");
                        if (doc.getData().get("Finished") != null) { // to make sure the boolean is not null
                            if (finish) { // if the event is finished, we will highlight the date on the calendar
                                mCalendarView.markDate(Integer.parseInt(dateResult[0]), Integer.parseInt(dateResult[1]), Integer.parseInt(dateResult[2]));
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * Shows the overflow menu on the toolbar
     *
     * @param menu overflow menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (followingUser == null) {
            getMenuInflater().inflate(R.menu.toolbar_view_habit, menu);
        }
        return true;
    }

    /**
     * When overflow menu options are clicked
     *
     * @param item menu item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit_habit) {
            Intent intentEdit = new Intent(this, EditHabitActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("habit_index", habitIndex);
            intentEdit.putExtras(bundle); //is this redundant?
            startActivity(intentEdit);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
