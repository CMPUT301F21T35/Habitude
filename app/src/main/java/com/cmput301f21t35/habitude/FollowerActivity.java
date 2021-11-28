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
 * followerAcitivty class is define a actiiity when the user clikck the followers
 * button in the bottom tab following, read the information from the firestore database
 * and show as a listView
 */
public class FollowerActivity extends AppCompatActivity {
    //list view -> used to show the list of the followers in the activity
    //arrayList -> used to store the information in the firestore with the followers
    //arrayadapter-> define in the class followersArrayAdapter, to define the userid and two buttons
    ListView followList;
    ArrayList<String> followerList;
    ArrayAdapter<String> followerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        //initilize the element in the activity find by id
        followList = findViewById(R.id.follower_list);
        followerList = new ArrayList<>();
        followerAdapter = new FollowersArrayAdapter(this,followerList);
        followList.setAdapter(followerAdapter);

        //firestore
        FirebaseFirestore db =  FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("followers");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                followerList.clear();
                for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    //get all the email id, and store them into the followerlist.
                    String email = (String) doc.getId();
//                    (String) doc.getData().get("email");
                    followerList.add(email);
                }
                //notify the change
                followerAdapter.notifyDataSetChanged();
            }
        });


        //back button
        final Button back_button = findViewById(R.id.back);
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

    }
}
