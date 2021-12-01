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

/**
 * this class used to defin a pendingfollowersarrayAdapter to used
 * in the followers activity, so is has the functionality with the userid
 * and accept button and deny button.
 */
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
        //get the view content in the layout to create the adapter
        if (convertView ==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.pending_followers_list_view_content,parent,false);
        }
        // get the id
        String request = pendingFollowersList.get(position);
        TextView pendingFollowerId = convertView.findViewById(R.id.pendingFollowerId);
        if (request != null){
            //set the id
            pendingFollowerId.setText(request);

        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //collectionreference path to the current user's followersReq collection
        final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("followersReq");
        //collectionreference path to the current user's followers collection
        final CollectionReference collectionReference1 = db.collection("Users").document(user.getEmail()).collection("followers");

        Button declineButton = convertView.findViewById(R.id.decline);
        declineButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                //defind the decline button functionality, when the decline button is
                //clicked, delete the followersRequest for the current user in the firestore.
                collectionReference.document(getItem(position)).delete();
            }
        });
        // build two different hash map with different field contained.
        // hashmap x contains the people who is sending the request email as the field email.
        Map<String,Object> x = new HashMap<>();
        x.put("email", getItem(position));
        // hashmap y contains the current users email as the field.
        Map<String,Object> y = new HashMap<>();
        y.put("email", user.getEmail());

        Button acceptButton = convertView.findViewById(R.id.accept);
        acceptButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                // define the accept functionality, when the accept button is clicked
                // first step is delete the followersRequest because it is already decided.
                // second step is storing the people's email who is send the request into the current
                // user's followers collection in the firestore.
                // third step is storing the current user's email into the people who is send the request's
                //followings collection in the firestore.

                // the collectionreference path to the followings collections belongs to the people who is sending the request.
                final CollectionReference collectionReference2 = db.collection("Users").document(getItem(position)).collection("followings");
                collectionReference.document(getItem(position)).delete();
                collectionReference1.document(getItem(position)).set(x);
                collectionReference2.document(user.getEmail()).set(y);
            }
        });


   
        return convertView;
    }

}
