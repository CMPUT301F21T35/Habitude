package com.cmput301f21t35.habitude;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * @author echiu
 * this is the array adapter to use for the users that are being followed
 */

public class followingList extends ArrayAdapter<String> {
    private ArrayList<String> following;
    private Context context;

    public followingList(Context context, ArrayList<String> following) {
        super(context, 0, following);
        this.context = context;
        this.following = following;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // inflate the list
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.following_context, parent, false);
        }
        String email = following.get(position); // get the email at position
        TextView emailView = view.findViewById(R.id.email_text); // set on view
        emailView.setText(email);

        return view;
    }
}
