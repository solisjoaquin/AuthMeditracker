package com.example.authbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileDoctorActivity extends AppCompatActivity {

    private TextView mUserName, mUserEmail, mHospital;
    private Button mSignOutBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_doctor);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mHospital = (TextView) findViewById(R.id.hospitalName);
        mUserName = (TextView) findViewById(R.id.textViewNameProfile);
        mUserEmail= (TextView) findViewById(R.id.textViewEmailProfile);
        mSignOutBtn = (Button) findViewById(R.id.btnLogOut);

        mSignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                startActivity(new Intent(ProfileDoctorActivity.this, RegisterDoctor.class));
                finish();
            }
        });

        getUserInfo();
    }

    private void getUserInfo(){

        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Doctors").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    String hospital = dataSnapshot.child("hospital").getValue().toString();
                    String name = dataSnapshot.child("name").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();

                    mHospital.setText(hospital);
                    mUserName.setText(name);
                    mUserEmail.setText(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
