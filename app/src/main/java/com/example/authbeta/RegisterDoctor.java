package com.example.authbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterDoctor extends AppCompatActivity {

    //These variables are 4 EditText (Name, Hospital, Email, Password) and 2 button (Register, Login)
    private EditText mRegisterName, mRegisterEmail, mRegisterPsssword, mRegisterHospital;
    private Button mRegisterBtn, mToLoginButton;

    // This is the first step for Authentication for Firebase
    private FirebaseAuth mAuth;
    // This is the first step to work with the database
    private DatabaseReference mDatatabe;

    private String name = "";
    private String hospital = "";
    private String email  = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);

        // connect elements (editText, button) with their variables

        mRegisterName = (EditText) findViewById(R.id.editTextRegisterName);
        mRegisterHospital = (EditText) findViewById(R.id.editTextHospital);
        mRegisterEmail = (EditText) findViewById(R.id.editTextRegisterEmail);
        mRegisterPsssword = (EditText) findViewById(R.id.editTextRegisterPassword);

        mRegisterBtn = (Button) findViewById(R.id.registerBtn);
        mToLoginButton = (Button) findViewById(R.id.toLoginBtn);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //get instance of Database
        mDatatabe = FirebaseDatabase.getInstance().getReference();

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = mRegisterName.getText().toString();
                hospital = mRegisterHospital.getText().toString();
                email = mRegisterEmail.getText().toString();
                password = mRegisterPsssword.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !hospital.isEmpty()){

                    if (password.length() >= 6){

                        registerUser();

                    } else {
                        Toast.makeText(RegisterDoctor.this, "The password need at least 6 characters", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(RegisterDoctor.this, "fill all fieds", Toast.LENGTH_SHORT).show();
                }

            }
        });



        mToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterDoctor.this, LoginDoctor.class));
            }
        });
    }

    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            //create a map to upload the new values
                            // map is a data structure with a key and a value
                            Map<String, Object> map = new HashMap<>();
                            map.put("name", name);
                            map.put("hospital",hospital);
                            map.put("email", email);
                            map.put("password", password);

                            String id = mAuth.getCurrentUser().getUid();

                            mDatatabe.child("Doctors").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
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
