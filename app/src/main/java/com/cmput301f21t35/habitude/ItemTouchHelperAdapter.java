package com.cmput301f21t35.habitude;

//https://gist.githubusercontent.com/iPaulPro/5d43325ac7ae579760a9/raw/44694e320b77085df717be8fae20f7343db07e06/ItemTouchHelperAdapter.java

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}