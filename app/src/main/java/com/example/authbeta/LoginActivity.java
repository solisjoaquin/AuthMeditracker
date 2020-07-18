package com.example.authbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * This is the Login activity for the user.
 * Using the firebase authentication, this activity validates the user.
 * @author Joaquin Solis, Tanner Olson and Travis Stirling.
 * @version 1.0
 */
public class LoginActivity extends AppCompatActivity {

    // Declare TAG for log.
    private String TAG = "LoginActivity";

    // Declare Firebase Authorization variable.
    private FirebaseAuth mAuth;

    // Declare String variables.
    private String email, password;

    // Declare EditText variables.
    private EditText mLoginEmail, mLoginPassword;


    /**
     * Create the Login activity and set the content view of that activity.
     * Create an instance of the Firebase authorization and the Firebase database.
     * Set the variables for EditText.
     * @param savedInstanceState Pass the state of the instance.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initializes the Firebase authorization.
        mAuth = FirebaseAuth.getInstance();

        // Set the method variables to the user input.
        mLoginEmail = findViewById(R.id.editTextLoginEmail);
        mLoginPassword = findViewById(R.id.editTextLoginPassword);
    }


    /**
     * Validates that the user has filled out all fields.
     * @param view Pass the view.
     */
    public void clickLogin(View view) {

        // Set the values given by the user.
        email = mLoginEmail.getText().toString();
        password = mLoginPassword.getText().toString();

        // Conditional statements to validate that all fields are not empty.
        if (email.isEmpty()) {
            mLoginEmail.setError("Email is required.");
            mLoginEmail.requestFocus();
        } else if (password.isEmpty()) {
            mLoginPassword.setError("Password is required.");
            mLoginPassword.requestFocus();
        } else {

            // Calls the method to Login the user.
            loginUser();
        }
    }


    /**
     * Authenticates the user and logs them in.
     */
    private void loginUser() {

        // Authorization to sign in the user with email and password.
        mAuth.signInWithEmailAndPassword(email, password).
                addOnCompleteListener(LoginActivity.this,
                        new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                // Conditional statement to check if the authentication was successful.
                if (task.isSuccessful()){

                    // Information log stating success of user authentication.
                    Log.i(TAG, "User has successfully been authenticated.");

                    // Start the Profile activity.
                    startActivity(new Intent(LoginActivity.this,
                            ProfileActivity.class));

                    // Finishes the current activity.
                    finish();
                } else {

                    // If something goes wrong with the login process.
                    Toast.makeText(LoginActivity.this,
                            "There is a problem with your login credentials. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
