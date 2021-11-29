package com.cmput301f21t35.habitude;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.vo.DateData;
import sun.bob.mcalendarview.vo.MarkedDates;

public class IndicatorActivity extends AppCompatActivity {
    String habitSrc = "";
    String followingUser = "";
    static sun.bob.mcalendarview.MCalendarView mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator);
        Bundle extras = getIntent().getExtras(); //Get the habit name
        if(extras != null) { // check whether it is a null object
            habitSrc = extras.getString("HABITSRC"); // get name of the habit
            followingUser = extras.getString("followingUser");
        }

        // use MCalendarView to highlight the date from calendar
        mCalendarView = findViewById(R.id.calendar_indicator);

        //clear all the date once this activity restarts
        mCalendarView.getMarkedDates().getAll().clear();

        // find the right habit and its events
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (followingUser != null){ // see the following users' indicator
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
        }else{
            // show the current user's indicator
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
}}