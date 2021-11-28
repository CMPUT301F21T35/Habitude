package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 *
 */
public class FollowingsNavFragment extends Fragment implements FollowUserFragment.OnFragmentInteractionListener {

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

        return view;
    }

    @Override
    public void onOkPressed(String toFollow) {
        // open database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // ensure inputs are all correct
        if (toFollow.isEmpty()) {
            Toast.makeText(getActivity(), "Some fields are blank!", Toast.LENGTH_SHORT).show();
        } else if(toFollow.equals(user.getEmail())) { // ensure email isn't own email
            Toast.makeText(getActivity(), "Cannot follow Yourself!", Toast.LENGTH_SHORT).show();
        } else {
            String userEmail = user.getEmail();
            Check1(toFollow, userEmail); // call nested function to run all the tests
        }
    }

    /**
     * This function checks that the person the user is trying to follow exists in the database
     * @param toFollow
     * this is a string of the user to be sent a request
     * @param userEmail
     * this is the email of the current user to be used in queries to the databse
     */

    private void Check1(String toFollow, String userEmail) {
        // check that the email exists in the database
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("Users").whereEqualTo("email", toFollow)
                .limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean isEmpty = task.getResult().isEmpty();
                            if (isEmpty) { // if empty it means user does not exist
                                Toast.makeText(getActivity(), "User does not exist!", Toast.LENGTH_SHORT).show();
                            } else { // run next test
                                Check2(toFollow, userEmail);
                            }
                        }
                    }
                });
    };

    /**
     * This function checks that the user has not already sent a request to this person
     * @param toFollow
     * this is a string of the user to be sent a request
     * @param userEmail
     * this is the email of the current user to be used in queries to the databse
     */
    private void Check2(String toFollow, String userEmail) {
        // check if there is already outgoing request

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("Users").document(userEmail).collection("followingsReq").whereEqualTo("email", toFollow)
                .limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            boolean isEmpty = task.getResult().isEmpty();
                            if (!isEmpty) { // if not empty it means they have already sent a request
                                Toast.makeText(getActivity(), "You have already requested!", Toast.LENGTH_SHORT).show();
                            } else { // run next test
                                Check3(toFollow, userEmail);
                            }
                        }
                    }
                });
    }

    /**
     * This function checks that the user is not already following this person
     * @param toFollow
     * this is a string of the user to be sent a request
     * @param userEmail
     * this is the email of the current user to be used in queries to the databse
     */
    private void Check3(String toFollow, String userEmail) {
        // check if they are already following
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("Users").document(userEmail).collection("followings").whereEqualTo("email", toFollow)
                .limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean isEmpty = task.getResult().isEmpty();
                            if (!isEmpty) { // if not empty it means they are already following
                                Toast.makeText(getActivity(), "You are already following!", Toast.LENGTH_SHORT).show();
                            } else { // if all tests pass add to database
                                addRequest(toFollow, userEmail);
                            }
                        }
                    }
                });
    }

    /**
     * If all the tests pass then the person will be sent a request and the database will be updated to reflect that
     * @param toFollow
     * this is a string of the user to be sent a request
     * @param userEmail
     * this is the email of the current user to be used in queries to the databse
     */
    private void addRequest(String toFollow, String userEmail) {
        // open database of user to send request to
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users").document(toFollow).collection("followersReq");

        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("email", userEmail);

        // push to db
        collectionReference.document(userEmail).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Data has been added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data has not been added successfully");
                    }
                });

        // add to current user that he has sent a request
        final CollectionReference collectionReference1 = db.collection("Users").document(userEmail).collection("followingsReq");

        HashMap<String, Object> data1 = new HashMap<String, Object>();
        data1.put("email", toFollow);

        // push to db
        collectionReference1.document(toFollow).set(data1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Data has been added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data has not been added successfully");
                    }
                });

        Toast.makeText(getActivity(), "Request sent!", Toast.LENGTH_SHORT).show();
    }
}