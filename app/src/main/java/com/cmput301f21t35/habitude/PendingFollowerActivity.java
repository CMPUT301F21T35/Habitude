package com.cmput301f21t35.habitude;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * @deprecated functionality has been moved to RequestNavFragment.java
 */

public class PendingFollowerActivity extends AppCompatActivity {
    ListView pendingFollowList;
    ArrayList<String> pendingFollowerList;
    ArrayAdapter<String> pendingFollowerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_request);

        pendingFollowList = findViewById(R.id.pending_follower_list);
        pendingFollowerList = new ArrayList<>();
        pendingFollowerAdapter = new PendingFollowersArrayAdapter(this,pendingFollowerList);
        pendingFollowList.setAdapter(pendingFollowerAdapter);

        FirebaseFirestore db =  FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("followersReq");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                pendingFollowerList.clear();
                for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    String email = (String) doc.getId();
//                    (String) doc.getData().get("email");
                    pendingFollowerList.add(email);
                }
                pendingFollowerAdapter.notifyDataSetChanged();
            }
        });



        final Button back_button = findViewById(R.id.back);
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

//        pendingFollowList = findViewById(R.id.pending_follower_list);
//        pendingFollowerList = new ArrayList<>();
//        pendingFollowerAdapter = new PendingFollowersArrayAdapter(this,pendingFollowerList);
//        pendingFollowList.setAdapter(pendingFollowerAdapter);
    }
}
