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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FollowersActivity extends AppCompatActivity {
    public ListView followersListView;
    private ArrayAdapter<Users> followersArrayAdapter;
    private ArrayList<Users> followersList;
    private String TAG = "ProfileFollowersActivity";
    private Users currentUser = SharedInfo.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        // finish the activity upon pressing the back button
        Button back_button = findViewById(R.id.back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // get the View objects
        followersListView = findViewById(R.id.followersList);

        // initialize array adapter
        followersList = new ArrayList<>();
        followersArrayAdapter = new FollowersArrayAdapter(this, followersList);
        followersListView.setAdapter(followersArrayAdapter);
        populateList();
    }


    private void populateList() {
        getUserListItems(SharedInfo.getInstance().getCurrentUser().getUsername(),
                "followers", new SharingListCallBack() {
                    @Override
                    public void onCallbackSuccess(ArrayList<String> dataList) {
                        for (String userid : dataList) {
                            followersList.add(new Users(userid));
                        }
                        followersArrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCallbackFailure(String reason) {
                        Log.d(TAG, reason);
                    }
                });
    }
    public void getUserListItems(String userid, String field, SharingListCallBack callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DocumentReference documentReference = db.collection("Users").document(user.getEmail());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> followersList = (ArrayList<String>) document.get(field);
                        callback.onCallbackSuccess(followersList);
                    } else {
                        callback.onCallbackFailure(String.format("Document for %s does not exist", user.getEmail()));
                    }
                } else {
                    callback.onCallbackFailure(task.getException().toString());
                }
            }
        });
    }

}
