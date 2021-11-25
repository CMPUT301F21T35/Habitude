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

public class FollowersArrayAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> followersList;

    public FollowersArrayAdapter(@NonNull Context context, ArrayList<String> followersList) {
        super(context, 0, followersList);
        this.context = context;
        this.followersList = followersList;
    }

    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.followers_list_view_content, parent, false);
        }
        String followers = followersList.get(position);

        TextView FollowerId = convertView.findViewById(R.id.FollowerId);
        if (followers != null){
            FollowerId.setText(followers);
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("followers");

        Button removeButton = convertView.findViewById(R.id.remove);
        removeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                collectionReference.document(getItem(position)).delete();
            }
        });


        return convertView;
    }
}