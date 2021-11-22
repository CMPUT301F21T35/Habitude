package com.cmput301f21t35.habitude;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class PendingFollowersArrayAdapter extends ArrayAdapter<Users> {
    private Context context;
    private ArrayList<Users> pendingFollowersList;
    private String TAG = "PendingFollowersArrayAdapter";

    PendingFollowersArrayAdapter(@NonNull Context context, ArrayList<Users> pendingFollowersList) {
        super(context, 0, pendingFollowersList);
        this.context = context;
        this.pendingFollowersList = pendingFollowersList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get the view showing a user in the list
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.pending_followers_list_view_content, parent, false);
        }
        // populate the list view content
        String userid = pendingFollowersList.get(position).getUsername();
        TextView pendingFollower = convertView.findViewById(R.id.pendingFollowerId);
        pendingFollower.setText(userid);

//        String userSrc = "";
//        Bundle extras = getIntent().getExtras(); //Get the habit name
//        if(extras != null) { // check whether it is a null object
//            userSrc = extras.getString("USERSRC"); // get name of the habit
//        }
        // declines a follow request upon pressing the decline button
        String currentName = SharedInfo.getInstance().getCurrentUser().getUsername();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference documentReference = db.collection("Users").document(currentName);
//        String currentName = SharedInfo.getInstance().getCurrentUser().getUsername();
        Button declineButton = convertView.findViewById(R.id.decline);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingFollowersList.remove(position);
                String field = "followersReq";
                PendingFollowersArrayAdapter.super.notifyDataSetChanged();
                documentReference.collection("followersReq");
                documentReference.update(field, FieldValue.arrayRemove(userid));

            }
        });
        // accepts a follow request upon pressing the accept button
        Button acceptButton = convertView.findViewById(R.id.accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               pendingFollowersList.remove(position);
               PendingFollowersArrayAdapter.super.notifyDataSetChanged();
               String field = "followersReq";
               documentReference.collection("followersReq");
               documentReference.update(field, FieldValue.arrayRemove(userid));
               String field2 = "followers";
               documentReference.collection("followers");
               documentReference.update(field2, FieldValue.arrayUnion(userid));

            }
        });

        return convertView;

    }

}