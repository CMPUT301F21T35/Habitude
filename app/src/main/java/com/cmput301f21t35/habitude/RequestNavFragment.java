package com.cmput301f21t35.habitude;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * @author cyao, echiu
 * fragement to show the requests that the user has, and allows them to accept/decline
 */
public class RequestNavFragment extends Fragment {

    private String pageTitle;
    private int pageNum;
    ListView pendingFollowList;
    ArrayList<String> pendingFollowerList;
    ArrayAdapter<String> pendingFollowerAdapter;

    public RequestNavFragment() {
        // Required empty public constructor
    }

    public static RequestNavFragment newInstance(String pageTitle, int pageNum) {
        RequestNavFragment fragment = new RequestNavFragment();
        Bundle args = new Bundle();
        args.putString("somePageTitle", pageTitle);
        args.putInt("somePageNum", pageNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageTitle = getArguments().getString("somePageTitle");
            pageNum = getArguments().getInt("somePageNum");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_nav, container, false);
        //inilize the element
        pendingFollowList = view.findViewById(R.id.pending_follower_list);
        pendingFollowerList = new ArrayList<>();
        //creat a new arrayadapter by the class named PendingFOllowersArrayAdapter.
        pendingFollowerAdapter = new PendingFollowersArrayAdapter(getActivity(),pendingFollowerList);
        pendingFollowList.setAdapter(pendingFollowerAdapter);

        //firestore
        FirebaseFirestore db =  FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //collectionreference path to the current user's followersReq collection
        final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("followersReq");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                //make sure the pendingfollowerlist is empty
                pendingFollowerList.clear();
                for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    // go through the firestore database, and get every element's userid as string email
                    // store them into the pendingfollowerList
                    String email = (String) doc.getId();
                    pendingFollowerList.add(email);
                }
                //notify the change
                pendingFollowerAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}