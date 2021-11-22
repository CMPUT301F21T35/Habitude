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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class DeleteHabitEventFragment extends DialogFragment {

    String habitSrc;
    private Event event;

    /**
     * Fragment initializer
     * @param habitSrc Habit name
     * @param event Habit event ID/name
     */
    public DeleteHabitEventFragment(String habitSrc, Event event) {
        this.habitSrc = habitSrc;
        this.event = event;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * Create alert dialog when user activates delete method.  Delete item from Firebase if confirmed.
     * @param savedInstanceState Bundle
     * @return Alert Dialog
     */
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_delete_habit_event,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String errorMessage = String.format("Are you sure you want to delete the habit event \"%s\"? This action cannot be undone.", event.getEventName());

        return builder
                .setView(view)
                .setTitle("Delete event")
                .setMessage(errorMessage)
                .setNegativeButton("NO", null)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,int i) {
                        if (event != null) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            final CollectionReference collectionReference = db.collection("All Habits").document(habitSrc).collection("Events");
                            collectionReference
                                .document(event.getEventName())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Data has been removed successfully!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Data could not be removed!" + e.toString());
                                    }
                                });
                        }
                    }
                }).create();
    }
}
