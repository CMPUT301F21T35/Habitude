package com.cmput301f21t35.habitude;

import android.content.Context;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FollowersArrayAdapter extends ArrayAdapter<Users> {
    private Context context;
    private ArrayList<Users> followersList;
    private String TAG = "FollowersArrayAdapter";

    public FollowersArrayAdapter(@NonNull Context context, ArrayList<Users> followersList) {
        // need to call super
        super(context, 0, followersList);
        this.context = context;
        this.followersList = followersList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get the view for a follower in the list
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.followers_list_view_content, parent, false);
        }
        // populate the list view content
        String followerId = followersList.get(position).getUsername();
        TextView followerIdView = convertView.findViewById(R.id.pendingFollowerId);
        followerIdView.setText(followerId);

        // pressing the remove button removes the follower
        String currentName = SharedInfo.getInstance().getCurrentUser().getUsername();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference documentReference = db.collection("Users").document(currentName);

        Button removeButton = convertView.findViewById(R.id.remove);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followersList.remove(position);
                String field = "followers";
                FollowersArrayAdapter.super.notifyDataSetChanged();
                documentReference.collection("followers");
                documentReference.update(field, FieldValue.arrayRemove(followerId));


            }
        });
        return convertView;
    }
}



