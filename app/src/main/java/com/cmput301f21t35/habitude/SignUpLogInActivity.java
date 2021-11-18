package com.cmput301f21t35.habitude;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpLogInActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button signUpButton;
    private FirebaseAuth mAuth;
    private boolean signUp = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        signUpButton = findViewById(R.id.sign_up_button);
        email = findViewById(R.id.sign_up_email);
        password = findViewById(R.id.sign_up_password);

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
    }


}
