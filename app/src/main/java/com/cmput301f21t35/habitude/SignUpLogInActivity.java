package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpLogInActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button signUpButton;
    Button logInButton;
    Button switchToSignUp;
    Button switchToLogIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        signUp();
    }

    private void signUp() {
        setContentView(R.layout.activity_sign_up);
        signUpButton = findViewById(R.id.sign_up_button);
        email = findViewById(R.id.sign_up_email);
        password = findViewById(R.id.sign_up_password);
        switchToLogIn = findViewById(R.id.switch_to_login);

        signUpButton.setOnClickListener(view -> {
            if (email.getText().length() == 0 || password.getText().length() == 0) {
                Toast.makeText(SignUpLogInActivity.this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(SignUpLogInActivity.this, MainActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpLogInActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        switchToLogIn.setOnClickListener(view -> {
            logIn();
        });
    }

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
                                Intent intent = new Intent(SignUpLogInActivity.this, MainActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(SignUpLogInActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        switchToSignUp.setOnClickListener(view -> {
            signUp();
        });
    }


}
