package com.cmput301f21t35.habitude;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddHabitEvent extends DialogFragment {
    private EditText eventName;
    private EditText eventComment;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Event newEvent);
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_habit_event, null);
        eventName = view.findViewById(R.id.event_name_editText);
        eventComment = view.findViewById(R.id.event_comment_editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = eventName.getText().toString();
                        String comment = eventComment.getText().toString();
                        /////////////////////////////////////////////////////////////////////////////////
                        // ON THIS LINE HABIT ID IS JUST 0, WILL HAVE TO IMPLEMENT SOME ID SYSTEM LATER//
                        /////////////////////////////////////////////////////////////////////////////////
                        listener.onOkPressed(new Event(name, comment));
                    }
                }).create();
    }
}