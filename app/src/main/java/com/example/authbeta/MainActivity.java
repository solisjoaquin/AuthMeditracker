package com.example.authbeta;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * The MainActivity class creates buttons for Doctor's and Patients.
 * @author Joaquin Solis, Tanner Olson and Travis Stirling.
 */
public class MainActivity extends AppCompatActivity {

    // Declare String variables.
    private static String TAG = "MainActivity";

    /**
     * Create the MainActivity class and set the view of that activity.
     * @param savedInstanceState Passes the state of the instance.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Log onCreate with MainActivity.
        Log.d(TAG, "onCreate is working.");

        // Declare Button variables.
        Button mButtonDoctor = findViewById(R.id.buttonDoctor);
        Button mButtonPatient = findViewById(R.id.buttonPatient);

        // This method occurs when the user clicks the Doctor button.
        mButtonDoctor.setOnClickListener(new View.OnClickListener() {

            /**
             * Define what happens when the Doctor button is clicked.
             * @param view The view of the activity.
             */
            @Override
            public void onClick(View view) {

                // Log initialization of ButtonDoctor.
                Log.d(TAG, "Doctor onClick working.");

                // startActivity will lead to the Doctor registration page.
                startActivity(new Intent(MainActivity.this, RegisterDoctor.class));
            }
        });

        // This method occurs when the user clicks the Patient button.
        mButtonPatient.setOnClickListener(new View.OnClickListener() {

            /**
             * Define what happens when the Patient button is clicked.
             * @param view The view of the activity.
             */
            @Override
            public void onClick(View view) {

                // Log initialization of ButtonPatient.
                Log.d(TAG, "Patient onClick working.");

                // startActivity will lead to the Patient registration page.
                startActivity(new Intent(MainActivity.this, RegisterPatient.class));
            }
        });
    }
}
