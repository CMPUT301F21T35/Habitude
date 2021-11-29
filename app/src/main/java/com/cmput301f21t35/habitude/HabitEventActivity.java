package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HabitEventActivity extends AppCompatActivity implements EditHabitEvent.OnFragmentInteractionListener {
    Map event;
    String habitSrc;
    String eventTitle;
    String eventComment;
    LocalDate eventDateStart;
    String eventTime;
    boolean finished;
    // Habit photo
    LocalDate habitDateComplete;
    Button edit_button;
    String eventGeolocation = "null";
    String eventPhoto;

    /**
     * Create the activity
     * @param savedInstanceState Bundle
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit_event);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event");

        Intent intent = getIntent();
        habitSrc = intent.getStringExtra("habit_id");
        eventTitle = intent.getStringExtra("event_id");
        getData();

    }

    /**
     * Update event when fragment OK pressed
     * @param newEvent new event information
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onOkPressed(Event newEvent) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("habits").document(habitSrc).collection("Events");

        String eventName = newEvent.getEventName();
        String eventDate = newEvent.getEventDate();
        String eventTime = newEvent.getEventTime();
        String eventComment = newEvent.getEventComment();
        Boolean eventFinished = newEvent.getEventFinished();
        String eventGeolocation = newEvent.getEventGeolocation(); //new
        String eventPhoto = newEvent.getEventPhoto();

        // Make sure all fields are filled
        if (eventName.isEmpty() | eventDate.isEmpty() || eventTime.isEmpty()) {
            Toast.makeText(this, "Some fields are blank!", Toast.LENGTH_SHORT).show();
        } else {
            // Create hashmap with all the attributes of event
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("Event Name", eventName);
            data.put("Date", eventDate);
            data.put("Time", eventTime);
            data.put("Comment", eventComment);
            data.put("Finished", eventFinished);
            data.put("Geolocation",eventGeolocation);
            data.put("Photo", eventPhoto);

            // Delete old event and update database with new information
            collectionReference.document(eventTitle).delete();
            collectionReference.document(newEvent.getEventName())
                    .set(data)
                    .addOnSuccessListener(unused -> Log.d(TAG, "Data has been added successfully"))
                    .addOnFailureListener(e -> Log.d(TAG, "Data has not been added successfully"));
            eventTitle = eventName;
            getData();
        }
    }

    /**
     * Shows the overflow menu on the toolbar
     * @param menu overflow menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_event, menu);
        return true;
    }

    /**
     * When overflow menu options are clicked
     * @param item menu item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit_event) {
            Event event_class = new Event(eventTitle, eventComment, event.get("Date").toString(), event.get("Time").toString(), finished);
            new EditHabitEvent(habitSrc, event_class).show(getSupportFragmentManager(), "EDIT EVENT");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Get updated values from Firebase and insert into layout view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DocumentReference documentReference = db.collection("Users").document(user.getEmail()).collection("habits").document(habitSrc).collection("Events").document(eventTitle);

        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {

                    // Get document data
                    event = document.getData();
                    eventTitle = event.get("Event Name").toString();
                    eventComment = event.get("Comment").toString();
                    DateTimeFormatter getDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    eventDateStart = LocalDate.parse(event.get("Date").toString(), getDateFormat);
                    finished = event.get("Finished").equals(true);
                    eventTime = event.get("Time").toString();
                    eventGeolocation = event.get("Geolocation").toString();
                    eventPhoto = event.get("Photo").toString();

                    // Get layout views
                    TextView habit_event_title_view = findViewById(R.id.habit_event_title);
                    TextView habit_event_reason_view = findViewById(R.id.habit_event_reason);
                    ImageView habit_event_finished = findViewById(R.id.habit_event_finished);
                    ImageView habit_event_not_finished = findViewById(R.id.habit_event_not_finished);
                    TextView habit_event_date_time = findViewById(R.id.habit_event_date_time);
                    TextView habit_event_geolocation = findViewById(R.id.habit_event_geolocation);
                    TextView habit_event_photo = findViewById(R.id.habit_event_Photo);
                    // Set values in layout
                    habit_event_title_view.setText(eventTitle);
                    habit_event_reason_view.setText(eventComment);
                    DateTimeFormatter printDateFormat = DateTimeFormatter.ofPattern("LLLL dd, yyyy");
                    habit_event_date_time.setText(printDateFormat.format(eventDateStart) + " at " + eventTime);
                    if (finished) {
                        habit_event_not_finished.setVisibility(View.GONE);
                        habit_event_finished.setVisibility(View.VISIBLE);
                    } else {
                        habit_event_finished.setVisibility(View.GONE);
                        habit_event_not_finished.setVisibility(View.VISIBLE);
                    }
                    try {
                        habit_event_geolocation.setText(new StringBuilder().append("Location: ").append(eventGeolocation).toString());
                        habit_event_photo.setText(new StringBuilder().append("Photo: ").append(eventPhoto).toString());
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    // habit_event_time.setText(eventTime);

                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }
}
