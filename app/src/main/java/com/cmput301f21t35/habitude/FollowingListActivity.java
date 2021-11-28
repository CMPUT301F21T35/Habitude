package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class FollowingListActivity extends AppCompatActivity implements FollowUserFragment.OnFragmentInteractionListener {

    ListView followList;
    ArrayList<String> followingList;
    ArrayAdapter<String> followingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_list);

        //open database
        FirebaseFirestore db =  FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("followings");

        // get current following and add to list
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                followingList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    String email = (String) doc.getData().get("email");
                    followingList.add(email);
                }
                //followingAdapter.notifyDataSetChanged();
            }
        });

        followList = findViewById(R.id.following_list);
        followingList = new ArrayList<>();

        followList.setAdapter(followingAdapter);

        final FloatingActionButton newFollowButton = findViewById(R.id.follow_New_Button);

        // set listener to open fragment
        newFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FollowUserFragment().show(getSupportFragmentManager(), "FOLLOW NEW USER");
            }
        });
    }

    @Override
    public void onOkPressed(String toFollow) {

        // open database
        FirebaseFirestore db =  FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // check that the email exists in the database
        String emailExists;
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Users");
        rootRef.orderByChild("email").equalTo(toFollow).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // ensure inputs are all correct
                    if(toFollow.isEmpty()) {
                        Toast.makeText(FollowingListActivity.this, "Some fields are blank!", Toast.LENGTH_SHORT).show();
                    } else{ // otherwise add to db
                        // open database of user to send request to
                        final CollectionReference collectionReference = db.collection("Users").document(toFollow).collection("followersReq");

                        // push to db
                        collectionReference.document("email").set(user.getEmail())
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

                        // add to current user that he has sent a request
                        final CollectionReference collectionReference1 = db.collection("Users").document(user.getEmail()).collection("followingsReq");

                        // push to db
                        collectionReference.document("email").set(toFollow)
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}