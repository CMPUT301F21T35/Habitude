package com.cmput301f21t35.habitude;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * define a arrayadapter to start with ont users id and accept button and deny button
 * and define the set on listner.
 */
public class FollowersArrayAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> followersList;

    public FollowersArrayAdapter(@NonNull Context context, ArrayList<String> followersList) {
        super(context, 0, followersList);
        this.context = context;
        this.followersList = followersList;
    }
    // get the view
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.followers_list_view_content, parent, false);
        }
        //get the first element id
        String followers = followersList.get(position);
        TextView FollowerId = convertView.findViewById(R.id.FollowerId);
        if (followers != null){
            //set the first element as the id
            FollowerId.setText(followers);
        }
        // firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //collectionreference path to current user's followers
        final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("followers");
        // set the onclick listener when clicking the button remove, to delete it from the database firestore
        Button removeButton = convertView.findViewById(R.id.remove);
        removeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                // define the remove button functionality, when the remove button is clicked,
                //the followers is gonna remove from the firestore database.
                collectionReference.document(getItem(position)).delete();
            }
        });
        return convertView;
    }
}