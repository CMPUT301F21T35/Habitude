package com.cmput301f21t35.habitude;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PendingFollowersArrayAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> pendingFollowersList;

    public PendingFollowersArrayAdapter(@NonNull Context context, ArrayList<String> pendingFollowersList) {
        super(context, 0, pendingFollowersList);
        this.context = context;
        this.pendingFollowersList = pendingFollowersList;
    }

    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if (convertView ==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.pending_followers_list_view_content,parent,false);
        }
//        FirebaseFirestore db =  FirebaseFirestore.getInstance();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//        pendingFollowerID = pendingFollowersList.get(position);
        String request = pendingFollowersList.get(position);
        TextView pendingFollowerId = convertView.findViewById(R.id.pendingFollowerId);
        if (request != null){
            pendingFollowerId.setText(request);

        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DocumentReference documentReference = db.collection("Users").document(user.getEmail());
        final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("followersReq");
        final CollectionReference collectionReference1 = db.collection("Users").document(user.getEmail()).collection("followers");
//        final CollectionReference collectionReference2 = db.collection("Users").document(user.getEmail()).collection("followerings");

        Button declineButton = convertView.findViewById(R.id.decline);
        declineButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
//                pendingFollowersList.remove(position);
//                String field = "followersReq";
//                PendingFollowersArrayAdapter.super.notifyDataSetChanged();
//                final CollectionReference collectionReference2 = db.collection("Users").document(getItem(position)).collection("followings");

                //documentReference.collection("followersReq").getId().delete();
                collectionReference.document(getItem(position)).delete();
//                collectionReference1.document().set(getItem(position));
//                collectionReference1.add(getItem(position));
//                collectionReference2.add(user.getEmail());

//                        update(field, FieldValue.arrayRemove(position));

            }
        });
        Map<String,Object> x = new HashMap<>();
        x.put("email", getItem(position));
        Map<String,Object> y = new HashMap<>();
        y.put("email", user.getEmail());
        Button acceptButton = convertView.findViewById(R.id.accept);
        acceptButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                final CollectionReference collectionReference2 = db.collection("Users").document(getItem(position)).collection("followings");
                collectionReference.document(getItem(position)).delete();
//                collectionReference1.document().set(getItem(position));
                collectionReference1.document(getItem(position)).set(x);
                collectionReference2.document(getItem(position)).set(y);
//                collectionReference2.add(user.getEmail());
            }
        });


   
        return convertView;
    }

}
