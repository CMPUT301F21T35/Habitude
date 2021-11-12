package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class tempActivity extends AppCompatActivity implements FollowUserFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
    }

    @Override
    public void onOkPressed(Request newRequest) {
        // open database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Requests");
        String reqTo = newRequest.getTo();
        String reqFrom = newRequest.getFrom();

        // ensure inputs are all correct
        if(reqTo.isEmpty() || reqFrom.isEmpty()) {
            Toast.makeText(this, "Some fields are blank!", Toast.LENGTH_SHORT).show();
        } else { // otherwise add to db

            // create hashmap with all the attributes of event
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("To", reqTo);
            data.put("From", reqFrom);

            // push to db
            collectionReference.document("Request").set(data)
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
        }
    }
}