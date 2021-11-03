package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteHabitFragment extends DialogFragment {
 //   private AddHabitEvent.OnFragmentInteractionListener listener;
    private Habit receivedHabit;

    //public interface OnFragmentInteractionListener {
    //    void onOkPressed(Habit newHabit);
    //}

    public DeleteHabitFragment(){}

    public DeleteHabitFragment(Habit habit) {
        this.receivedHabit = habit;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
   //     if (context instanceof AddHabitEvent.OnFragmentInteractionListener) {
   //         listener = (AddHabitEvent.OnFragmentInteractionListener) context;
   //     } else {
   //         throw new RuntimeException(context.toString());
   //     }
    }

    @NonNull//@Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_delete_habit,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Delete habit")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,int i) {
                        if (receivedHabit != null) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            final CollectionReference collectionReference = db.collection("All Habits");

                            collectionReference
                                    .document(receivedHabit.getHabitTitleName())
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