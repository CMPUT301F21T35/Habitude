package com.cmput301f21t35.habitude;


import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class FollowUserFragment extends DialogFragment {
    private EditText followIDView;
    private TextView myIDView;
    private OnFragmentInteractionListener listener;
    private String myID;


    public interface OnFragmentInteractionListener {
        void onOkPressed(Request newRequest);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_follow_user, null);

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null) {
            // get the name of the habit to use when querying the events
            myID = extras.getString("MY_ID");
        }

        followIDView = view.findViewById(R.id.user_id_editText);
        myIDView = view.findViewById(R.id.my_id_textView);

        // set up the fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add User")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String toFollow = followIDView.getText().toString();
                        listener.onOkPressed(new Request(toFollow, myID));
                    }
                }).create();
    }
}