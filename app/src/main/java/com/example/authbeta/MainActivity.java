package com.example.authbeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//This activity will have 2 buttons, one fot Doctor and for Patient
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //connect buttons with the UI elements
        // Declare buttons
        Button mBtnDoctor = (Button) findViewById(R.id.btnDoctor);
        Button mBtnPatient = (Button) findViewById(R.id.btnPatient);

        //this method occur when the user clicks the Doctor Button
        mBtnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity will lead to the register page
                startActivity(new Intent(MainActivity.this, RegisterDoctor.class));

            }
        });

        //this method occurs when the user clicks the Patient button
        mBtnPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterPatient.class));

            }
        });
    }
}
