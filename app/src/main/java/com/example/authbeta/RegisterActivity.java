package com.example.authbeta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


/**
 * This is the main activity for the user.
 * This activity is a registration page for the user.
 * @author Joaquin Solis, Tanner Olson and Travis Stirling.
 * @version 1.0
 */
public class RegisterActivity extends AppCompatActivity {

    // Declare TAG for log.
    private String TAG = "RegisterActivity";

    // Declare Firebase Authorization variable.
    private FirebaseAuth mAuth;

    // Declare Database variable.
    private DatabaseReference mDatabase;

    // Declare String variables.
    private String name, email, password;

    // Declare EditText variables.
    private EditText mRegisterName, mRegisterEmail, mRegisterPassword;


    /**
     * Create the RegisterActivity and set the content view of that activity.
     * Create an instance of the Firebase authorization and the Firebase database.
     * Set the variables for EditText.
     * @param savedInstanceState Pass the state of the instance.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initializes the Firebase authorization.
        mAuth = FirebaseAuth.getInstance();

        // Gets the Database instance.
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Set the method variables to the user input.
        mRegisterName = findViewById(R.id.editTextRegisterName);
        mRegisterEmail = findViewById(R.id.editTextRegisterEmail);
        mRegisterPassword = findViewById(R.id.editTextRegisterPassword);
    }


    /**
     * Validates the current user against the firebase database. If the user exists, start
     * the Profile activity.
     */
    @Override
    protected void onStart() {
        super.onStart();

        // Conditional statement to validate whether the user exists in database.
        if (mAuth.getCurrentUser() != null) {

            // If the user does exist, start the Profile activity.
            startActivity(new Intent(RegisterActivity.this, ProfileActivity.class));

            // Finishes the current activity.
            finish();
        }
    }


    /**
     * Validates that the user has filled out all fields and that their password
     * is at least six digits.
     * @param view Pass the view.
     */
    public void clickRegister(View view) {

        // Set the values given by the user.
        name = mRegisterName.getText().toString();
        email = mRegisterEmail.getText().toString();
        password = mRegisterPassword.getText().toString();

        // Conditional statements to validate that all fields are not empty.
        if (name.isEmpty()) {
            mRegisterName.setError("Name is required.");
            mRegisterName.requestFocus();
        } else if (email.isEmpty()) {
            mRegisterEmail.setError("Email is required.");
            mRegisterEmail.requestFocus();
        } else if (password.isEmpty()) {
            mRegisterPassword.setError("Password is required.");
            mRegisterPassword.requestFocus();
        } else if (password.length() < 6){

            // If password is less than six digits, notify user of minimum requirement.
            Toast.makeText(RegisterActivity.this,
                    "The password needs at least six characters.",
                    Toast.LENGTH_SHORT).show();
            mRegisterPassword.requestFocus();
        } else {

            // Calls the method to Authenticate the user.
            authenticateUser();
        }
    }


    /**
     * Register the user and add that user to the Firebase Database.
     */
    private void authenticateUser() {

        // Authorization to create the user with email and password.
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this,
                        new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                // Conditional statement to check if the authentication was successful.
                if (task.isSuccessful()){

                    // Information log stating success of user authentication.
                    Log.i(TAG, "User has successfully been authenticated.");

                    // Create a map to upload the new values to the database.
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("email", email);
                    map.put("password", password);

                    // Get current user.
                    FirebaseUser user = mAuth.getCurrentUser();

                    // Assert that user is not null.
                    assert user != null;

                    // Check and set the values that are input into the database.
                    mDatabase.child("Patients").child(user.getUid()).setValue(map)
                            .addOnCompleteListener(RegisterActivity.this,
                                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            // Conditional statement that the data was input successfully.
                            if (task.isSuccessful()) {

                                // Information log stating success of database input.
                                Log.i(TAG, "Name, Email and Password were successfully " +
                                        "added to the database.");

                                // Start the Profile activity.
                                startActivity(new Intent(RegisterActivity.this,
                                        ProfileActivity.class));

                                // Finishes the current activity.
                                finish();
                            }
                        }
                    });
                } else {

                    // If something goes wrong with the database entry process.
                    Toast.makeText(RegisterActivity.this,
                            "There is a problem with the registration process",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * Create the Login activity if the user selects 'I Have An Account'.
     * @param view Pass the view.
     */
    public void clickAccount(View view) {

        // Start the Login activity.
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

        // Finishes the current activity.
        finish();
    }
}
