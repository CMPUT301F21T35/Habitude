package com.cmput301f21t35.habitude;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

//Based on https://developer.android.com/guide/topics/ui/layout/recyclerview

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private ArrayList<Habit> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.textView);

            //https://stackoverflow.com/questions/28296708/get-clicked-item-and-its-position-in-recyclerview#comment121192976_39707729
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                    MainActivity mainActivity = MainActivity.getInstance();
                    mainActivity.editHabitFromIndex(pos);
                }
            });
        }

        public TextView getTextView() {
            return textView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public CustomAdapter(ArrayList<Habit> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet.get(position).getHabitTitleName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    //https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf

    //On the swiping of an item, we kill the index with killIndex in MainActivity, and update our View.
    @Override
    public void onItemDismiss(int position) {
        MainActivity mainActivity = MainActivity.getInstance();
        mainActivity.killIndex(position);
        localDataSet.remove(position);
        notifyItemRemoved(position);
    }

    //On the dragging of an item, we swap the locations as seen below.
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(localDataSet, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(localDataSet, i, i - 1);
            }
        }
        updateIndices(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    private void updateIndices(int fromPosition, int toPosition) {
        Habit fromHabit = localDataSet.get(fromPosition);
        Habit toHabit = localDataSet.get(toPosition);

        //fromHabit.setIndex(toPosition);
        //toHabit.setIndex(fromPosition);

        MainActivity mainActivity = MainActivity.getInstance();
        mainActivity.updateIndices(fromHabit);
        mainActivity.updateIndices(toHabit);
    }
}