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
 * fragment that allows users to see the people that are currently following them
 */
public class FollowerNavFragment extends Fragment {

    private String pageTitle;
    private int pageNum;
    ListView followList;
    ArrayList<String> followerList;
    ArrayAdapter<String> followerAdapter;

    public FollowerNavFragment() {
        // Required empty public constructor
    }

    public static FollowerNavFragment newInstance(String pageTitle, int pageNum) {
        FollowerNavFragment fragment = new FollowerNavFragment();
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
        View view = inflater.inflate(R.layout.fragment_follower_nav, container, false);

        FirebaseFirestore db =  FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("followers");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                followerList.clear();
                for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    String email = (String) doc.getId();
                    followerList.add(email);
                }
                followerAdapter.notifyDataSetChanged();
            }
        });

        followList = (ListView) view.findViewById(R.id.follower_list);
        followerList = new ArrayList<>();
        followerAdapter = new FollowersArrayAdapter(getActivity(),followerList);
        followList.setAdapter(followerAdapter);

        return view;
    }
}