package com.cmput301f21t35.habitude;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PendingFollowersActivity extends AppCompatActivity {
    public ListView pendingFollowersListView;
    private ArrayAdapter<Users> pendingFollowersArrayAdapter;
    private ArrayList<Users> pendingFollowersList;
    private String TAG = "ProfilePendingFollowersActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_requests);
        Button back_button = findViewById(R.id.back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // get the View objects
        pendingFollowersListView = findViewById(R.id.pendingFollowersList);

        // instantiate the array adapter
        pendingFollowersList = new ArrayList<>();
        pendingFollowersArrayAdapter = new PendingFollowersArrayAdapter(this, pendingFollowersList);
        pendingFollowersListView.setAdapter(pendingFollowersArrayAdapter);
        populateList();
    }


    public void getPendingFollowers(String userid, SharingListCallBack callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DocumentReference documentReference = db.collection("Users").document(user.getEmail());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> pendingFollowersList = (ArrayList<String>) document.get("pendingFollowerReqs");
                        callback.onCallbackSuccess(pendingFollowersList);
                    } else {
                        callback.onCallbackFailure(String.format("Document for %s does not exist", user.getEmail()));
                    }
                } else {
                    callback.onCallbackFailure(task.getException().toString());
                }
            }
        });
    }
    void populateList() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        getPendingFollowers(user.getEmail(),
                new SharingListCallBack() {
//                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    @Override
                    public void onCallbackSuccess(ArrayList<String> dataList) {
                        for (String userid : dataList) {
                            pendingFollowersList.add(new Users(userid));
                        }
                        pendingFollowersArrayAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCallbackFailure(String reason) {
                        Log.d(TAG, reason);
                    }
                });
    }

    }
////        String currentName = SharedInfo.getInstance().getCurrentUser().getUsername();
////        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
////        final DocumentReference documentReference = db.collection("Users").document(currentName);
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        final DocumentReference documentReference = db.collection("Users").document(user.getEmail());
//////        pendingFollowersList = documentReference.collection("followingsReq").get();
//////
//////        // get the list of the current user's pending follow requests
//////        pendingFollowersList.add(new Users(user.getEmail()));
//////        pendingFollowersArrayAdapter.notifyDataSetChanged();
//////
////        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
////            @Override
////            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////                if (task.isSuccessful()) {
////                    DocumentSnapshot document = task.getResult();
////                    if (document.exists()) {
////                        ArrayList<String> pendingFollowersList = (ArrayList<String>) document.get("pendingFollowerReqs");
////
////                    }
////                }
////            }
////        });
////
////
////
////    }}