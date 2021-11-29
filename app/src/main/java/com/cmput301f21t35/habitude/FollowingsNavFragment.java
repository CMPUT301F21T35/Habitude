package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author echiu
 * This is the fragment for the followerList activity, showing the people you are following and allowing you to follow others
 */
public class FollowingsNavFragment extends Fragment {

    private String pageTitle;
    private int pageNum;
    ListView followList;
    ArrayList<String> followingList;
    ArrayAdapter<String> followingAdapter;

    public FollowingsNavFragment() {
        // Required empty public constructor
    }

    public static FollowingsNavFragment newInstance(String pageTitle, int pageNum) {
        FollowingsNavFragment fragment = new FollowingsNavFragment();
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
        View view = inflater.inflate(R.layout.fragment_followings_nav, container, false);

        //open database
        FirebaseFirestore db =  FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final CollectionReference collectionReference = db.collection("Users").document(user.getEmail()).collection("followings");

        // get current following and add to list
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                followingList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    String email = (String) doc.getData().get("email");
                    followingList.add(email);
                }
                followingAdapter.notifyDataSetChanged();
            }
        });

        followList = (ListView) view.findViewById(R.id.following_list);
        followingList = new ArrayList<>();

        followingAdapter = new followingList(getActivity(), followingList);

        followList.setAdapter(followingAdapter);

        final FloatingActionButton newFollowButton = view.findViewById(R.id.follow_New_Button);

        // set listener to open fragment
        newFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FollowUserFragment().show(getFragmentManager(), "FOLLOW NEW USER");
            }
        });

        Intent intentViewFHabit = new Intent(getActivity(), ViewFollowingHabit.class);
        followList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                intentViewFHabit.putExtra("UserName", followingList.get(position));
                startActivity(intentViewFHabit);
            }
        });

        return view;
    }
}