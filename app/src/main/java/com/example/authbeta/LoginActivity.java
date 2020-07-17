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

//this is a new comment
public class LoginActivity extends AppCompatActivity {

    private EditText mLoginEmail, mLoginPassword;

    private String email  = "";
    private String password = "";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mAuth = FirebaseAuth.getInstance();

        mLoginEmail = findViewById(R.id.editTextLoginEmail);
        mLoginPassword = findViewById(R.id.editTextLoginPassword);
        Button mLoginUserBtn = findViewById(R.id.buttonLogin);



        mLoginUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = mLoginEmail.getText().toString();
                password = mLoginPassword.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()){

                    loginUser();
                } else {

                    Toast.makeText(LoginActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser(){

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "There is a problem to Login. Check your data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
