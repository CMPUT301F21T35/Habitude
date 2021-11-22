package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpLogInActivity extends AppCompatActivity {
    EditText name;
    EditText email;
    EditText password;
    Button signUpButton;
    Button logInButton;
    Button switchToSignUp;
    Button switchToLogIn;
    private FirebaseAuth mAuth;

    /**
     * Check if user is logged in when creating activity.
     * If user is logged in, redirect to main activity
     * Else show authentication activity
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            Intent intent = new Intent(SignUpLogInActivity.this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
//            finish();
//            this.overridePendingTransition(0, 0);
        } else {
            signUp();
        }
    }

    /**
     * Check if the user is still logged in when returning to authentication activity.
     * If the user is logged in, redirect to main activity
     * Else show authentication screen
     */
    @Override
    protected void onResume() {
        super.onResume();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            Intent intent = new Intent(SignUpLogInActivity.this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            this.overridePendingTransition(0, 0);
        } else {
            signUp();
        }

    }


    /**
     * Update activity content to sign up view.  Add user to database and log user in on successful sign up
     */
    private void signUp() {
        setContentView(R.layout.activity_sign_up);
        signUpButton = findViewById(R.id.sign_up_button);
        name = findViewById(R.id.sign_up_name);
        email = findViewById(R.id.sign_up_email);
        password = findViewById(R.id.sign_up_password);
        switchToLogIn = findViewById(R.id.switch_to_login);

        signUpButton.setOnClickListener(view -> {
            if (name.getText().length() == 0 || email.getText().length() == 0 || password.getText().length() == 0) {
                Toast.makeText(SignUpLogInActivity.this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {

                                // Update user profile with display name
                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name.getText().toString())
                                        .build();

                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    // Update user with display name
                                    user.updateProfile(profileChangeRequest)
                                            .addOnCompleteListener(this, voidTask -> {
                                                saveUID();

                                                // Add user information to database
                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                final DocumentReference documentReference = db.collection("Users").document(user.getEmail());
                                                HashMap<String, String> userData = new HashMap<>();
                                                userData.put("email", user.getEmail());
                                                userData.put("name", user.getDisplayName());
                                                userData.put("uid", user.getUid());
                                                documentReference
                                                        .set(userData)
                                                        .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                                                        .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));

                                                // Go to main activity
                                                Intent intent = new Intent(SignUpLogInActivity.this, MainActivity.class);
                                                intent.putExtra("user", user);
                                                startActivity(intent);
                                                this.overridePendingTransition(0, 0);
                                            });
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpLogInActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Switch to log in if button pressed
        switchToLogIn.setOnClickListener(view -> {
            logIn();
        });
    }

    /**
     * Update activity content to log in view.  Log user in on successful attempt
     */
    private void logIn() {
        setContentView(R.layout.activity_log_in);
        logInButton = findViewById(R.id.log_in_button);
        email = findViewById(R.id.log_in_email);
        password = findViewById(R.id.log_in_password);
        switchToSignUp = findViewById(R.id.switch_to_signup);

        logInButton.setOnClickListener(view -> {
            if (email.getText().length() == 0 || password.getText().length() == 0) {
                Toast.makeText(SignUpLogInActivity.this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                saveUID();
                                Intent intent = new Intent(SignUpLogInActivity.this, MainActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                                this.overridePendingTransition(0, 0);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(SignUpLogInActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Switch to sign up if button pressed
        switchToSignUp.setOnClickListener(view -> {
            signUp();
        });
    }

    /**
     * Add the user's account information when logging in
     */
    private void saveUID() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        FirebaseUser user = mAuth.getCurrentUser();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", user != null ? user.getUid() : null);
        editor.putString("name", user != null ? user.getDisplayName() : null);
        editor.putString("email", user != null ? user.getEmail() : null);
        editor.apply();
    }

}
