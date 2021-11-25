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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

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
                followingAdapter.notifyDataSetChanged();
            }
        });

        followList = findViewById(R.id.following_list);
        followingList = new ArrayList<>();

        followingAdapter = new followingList(this, followingList);

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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // ensure inputs are all correct
        if (toFollow.isEmpty()) {
            Toast.makeText(FollowingListActivity.this, "Some fields are blank!", Toast.LENGTH_SHORT).show();
        } else if(toFollow.equals(user.getEmail())) { // ensure email isn't own email
            Toast.makeText(FollowingListActivity.this, "Cannot follow Yourself!", Toast.LENGTH_SHORT).show();
        } else {
            String userEmail = user.getEmail();
            Check1(toFollow, userEmail); // call nested function to run all the tests
        }
    }

    private void Check1(String toFollow, String userEmail) {
        // check that the email exists in the database
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("Users").whereEqualTo("email", toFollow)
                .limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean isEmpty = task.getResult().isEmpty();
                            if (isEmpty) { // if empty it means user does not exist
                                Toast.makeText(FollowingListActivity.this, "User does not exist!", Toast.LENGTH_SHORT).show();
                            } else { // run next test
                                Check2(toFollow, userEmail);
                            }
                        }
                    }
                });
    };

    private void Check2(String toFollow, String userEmail) {
        // check if there is already outgoing request

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("Users").document(userEmail).collection("followingsReq").whereEqualTo("email", toFollow)
                .limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            boolean isEmpty = task.getResult().isEmpty();
                            if (!isEmpty) { // if not empty it means they have already sent a request
                                Toast.makeText(FollowingListActivity.this, "You have already requested!", Toast.LENGTH_SHORT).show();
                            } else { // run next test
                                Check3(toFollow, userEmail);
                            }
                        }
                    }
                });
    }

    private void Check3(String toFollow, String userEmail) {
        // check if they are already following
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("Users").document(userEmail).collection("followings").whereEqualTo("email", toFollow)
                .limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean isEmpty = task.getResult().isEmpty();
                            if (!isEmpty) { // if not empty it means they are already following
                                Toast.makeText(FollowingListActivity.this, "You are already following!", Toast.LENGTH_SHORT).show();
                            } else { // if all tests pass add to database
                                addRequest(toFollow, userEmail);
                            }
                        }
                    }
                });
    }

    private void addRequest(String toFollow, String userEmail) {
        // open database of user to send request to
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users").document(toFollow).collection("followersReq");

        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("email", userEmail);

        // push to db
        collectionReference.document(userEmail).set(data)
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
        final CollectionReference collectionReference1 = db.collection("Users").document(userEmail).collection("followingsReq");

        HashMap<String, Object> data1 = new HashMap<String, Object>();
        data1.put("email", toFollow);

        // push to db
        collectionReference1.document(toFollow).set(data1)
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

        Toast.makeText(FollowingListActivity.this, "Request sent!", Toast.LENGTH_SHORT).show();
    }
}