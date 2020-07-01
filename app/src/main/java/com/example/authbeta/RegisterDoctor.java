package com.example.authbeta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

/**
 * The RegisterDoctor class initializes user inputs, registration and login options.
 * @author Joaquin Solis, Tanner Olson and Travis Stirling.
 */
public class RegisterDoctor extends AppCompatActivity {

    // Declare EditText variables: Name, Hospital, Email and Password.
    private EditText mRegisterName;
    private EditText mRegisterEmail;
    private EditText mRegisterPassword;
    private EditText mRegisterHospital;

    // Declare Firebase authentication variable.
    private FirebaseAuth mAuth;

    // Declare Database variable.
    private DatabaseReference mDatabase;

    // Declare String variables.
    private String mName;
    private String mHospital;
    private String mEmail;
    private String mPassword;
    private static String TAG = "RegisterDoctor";

    /**
     * Create the RegisterDoctor class and set the view of that activity.
     * @param savedInstanceState Passes the state of the instance.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);

        // Log onCreate with RegisterDoctor.
        Log.d(TAG, "onCreate is working.");

        // Set EditText variables: Name, Hospital, Email and Password.
        mRegisterName = findViewById(R.id.editTextRegisterName);
        mRegisterEmail = findViewById(R.id.editTextRegisterEmail);
        mRegisterPassword = findViewById(R.id.editTextRegisterPassword);
        mRegisterHospital = findViewById(R.id.editTextRegisterHospital);

        // Declare Button variables.
        Button mButtonRegister = findViewById(R.id.buttonRegister);
        Button mButtonLogin = findViewById(R.id.buttonLogin);

        // Initialize instance of Firebase authentication.
        mAuth = FirebaseAuth.getInstance();

        // Initialize instance of Database.
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Log Firebase and Database have created instances.
        Log.d(TAG, "Firebase and Database have instances.");

        // This method occurs when the user clicks the Register button.
        mButtonRegister.setOnClickListener(new View.OnClickListener() {

            /**
             * Define what happens when the Register button is clicked.
             * @param view The view of the activity.
             */
            @Override
            public void onClick(View view) {

                // Log initialization of ButtonRegister.
                Log.d(TAG, "Register onClick working.");

                // Set the values of Strings.
                mName = mRegisterName.getText().toString();
                mHospital = mRegisterHospital.getText().toString();
                mEmail = mRegisterEmail.getText().toString();
                mPassword = mRegisterPassword.getText().toString();

                // Validate all fields are not empty.
                if (!mName.isEmpty() && !mEmail.isEmpty() && !mPassword.isEmpty() && !mHospital.isEmpty()) {

                    // Validate password length.
                    if (mPassword.length() >= 6){
                        registerUser();
                    } else {
                        Toast.makeText(RegisterDoctor.this, "The password needs at least 6 characters", Toast.LENGTH_SHORT).show();
                    }

                // If fields are empty.
                } else {
                    Toast.makeText(RegisterDoctor.this, "Fields can not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // This method occurs when the user clicks the Login button.
        mButtonLogin.setOnClickListener(new View.OnClickListener() {

            /**
             * Define what happens when the Login button is clicked.
             * @param view The view of the activity.
             */
            @Override
            public void onClick(View view) {

                // Log initialization of ButtonLogin.
                Log.d(TAG, "Login onClick working.");

                // startActivity will lead to the Login page.
                startActivity(new Intent(RegisterDoctor.this, LoginDoctor.class));
            }
        });
    }

    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            //create a map to upload the new values
                            // map is a data structure with a key and a value
                            Map<String, Object> map = new HashMap<>();
                            map.put("name", mName);
                            map.put("hospital",mHospital);
                            map.put("email", mEmail);
                            map.put("password", mPassword);

                            String id = mAuth.getCurrentUser().getUid();

                            mDatabase.child("Doctors").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if (task2.isSuccessful()) {

                                        startActivity(new Intent(RegisterDoctor.this, ProfileDoctorActivity.class));
                                        finish();
                                    }

                                }
                            });


                        } else {
                            Toast.makeText(RegisterDoctor.this, "There is a problem with register", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
