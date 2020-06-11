package com.example.authbeta;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Creates the initial app activity and creates buttons for Doctor's and Patients.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Declare buttons.
        Button mBtnDoctor = (Button) findViewById(R.id.btnDoctor);
        Button mBtnPatient = (Button) findViewById(R.id.btnPatient);

        // This method occurs when the user clicks the Doctor button.
        mBtnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // startActivity will lead to the Doctor registration page.
                startActivity(new Intent(MainActivity.this, RegisterDoctor.class));
            }
        });

        // This method occurs when the user clicks the Patient button.
        mBtnPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // startActivity will lead to the Patient registration page.
                startActivity(new Intent(MainActivity.this, RegisterPatient.class));
            }
        });
    }
}
