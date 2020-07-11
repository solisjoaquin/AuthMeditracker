package com.example.authbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfilePatientActivity extends AppCompatActivity {

    private TextView mUserName, mUserEmail;
    private Button mSignOutBtn;

    Button insert_btn;
    TextView infoCounterTxt, dayCounterTxt;
    Button minusBtn1, minusBtn2, plusBtn1, plusBtn2;
    int counter1, counter2;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.minus_1_btn:
                    minusCounter1();
                    break;
                case R.id.plus_1_btn:
                    plusCounter1();
                    break;
                case R.id.minus_2_btn:
                    minusCounter2();
                    break;
                case R.id.plus_2_btn:
                    plusCounter2();
                    break;
            }

        }
    };


    // MPAndroidChart
    // 1. install dependencies on build.gradle (Module.app):
    //       implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
    // 2. write on the other build gradle file with the name of the project in the repositories part.

    // allprojects {
    //    repositories {
    //        maven { url 'https://jitpack.io' }  <--- Add this line
    //    }
    //}

    // Chart variables

    LineChart lineChart;
    LineDataSet lineDataSet = new LineDataSet(null,null);
    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
    LineData lineData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_patient);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mUserName = (TextView) findViewById(R.id.textViewNameProfile);
        //mUserEmail= (TextView) findViewById(R.id.textViewEmailProfile);
        mSignOutBtn = (Button) findViewById(R.id.btnLogOut);

        infoCounterTxt = findViewById(R.id.edit_text1);
        dayCounterTxt = findViewById(R.id.edit_text2);

        insert_btn = findViewById(R.id.insertValuesBtn);

        minusBtn1 = findViewById(R.id.minus_1_btn);
        minusBtn1.setOnClickListener(clickListener);

        plusBtn1 = findViewById(R.id.plus_1_btn);
        plusBtn1.setOnClickListener(clickListener);

        minusBtn2 = findViewById(R.id.minus_2_btn);
        minusBtn2.setOnClickListener(clickListener);

        plusBtn2 = findViewById(R.id.plus_2_btn);
        plusBtn2.setOnClickListener(clickListener);

        initCounter();
        initCounter2();

        insertData();


        // connect the linechart with the layout
        lineChart = findViewById(R.id.lineChart);
        /**insert_btn = findViewById(R.id.insert_Btn);*/


        // All this information is to set the format of the chart
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(10f);
        yAxis.setGranularity(1f);
        yAxis.setTextColor(Color.BLUE);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.BLUE);
        xAxis.setDrawLimitLinesBehindData(true);

        YAxis yAxis1 = lineChart.getAxisRight();
        yAxis1.setDrawLabels(false);
        yAxis1.setEnabled(false);

        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineChart.getDescription().setEnabled(false);
        //lineDataSet.setLineWidth(4);
        lineDataSet.setDrawFilled(true);



        mSignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                startActivity(new Intent(ProfilePatientActivity.this, RegisterPatient.class));
                Toast.makeText(getApplicationContext(),"Bye!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // After getting the user info, this method will call the info of the chart
        getUserInfo();

        /**
        insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfilePatientActivity.this, InsertValues.class));
            }
        }); */




    }

    private void getUserInfo(){

        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Patients").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    String name = dataSnapshot.child("name").getValue().toString();
                    //String email = dataSnapshot.child("email").getValue().toString();

                    mUserName.setText("Welcome "+ name);
                    //mUserEmail.setText(email);

                    retrieveData();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // this method is to retrieve the info for the database
    private void retrieveData() {
        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Patients").child(id).child("ChartValues").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Entry> dataVals = new ArrayList<Entry>();

                if(snapshot.hasChildren()){
                    for(DataSnapshot myDataSnapshot : snapshot.getChildren()){
                        DataPoint dataPoint = myDataSnapshot.getValue(DataPoint.class);
                        dataVals.add(new Entry(dataPoint.getxValue(), dataPoint.getyValue()));
                    }


                    showChart(dataVals);
                } else {
                    lineChart.clear();
                    lineChart.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // this method is to show the chart. setting the correct elements with the retrieve data
    private void showChart(ArrayList<Entry> dataVals){
        lineDataSet.setValues(dataVals);
        lineDataSet.setLabel("DataSet 1");
        iLineDataSets.clear();
        iLineDataSets.add(lineDataSet);
        lineData = new LineData(iLineDataSets);
        lineChart.clear();
        lineChart.setData(lineData);
        lineChart.invalidate();

    }

    private void insertData() {

        final String id = mAuth.getCurrentUser().getUid();
        insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_data = mDatabase.child("Patients").child(id).child("ChartValues").push().getKey();
                int x = Integer.parseInt(dayCounterTxt.getText().toString());
                int y = Integer.parseInt(infoCounterTxt.getText().toString());


                DataPoint dataPoint = new DataPoint(x,y);
                mDatabase.child("Patients").child(id).child("ChartValues").child(id_data).setValue(dataPoint);
                Toast.makeText(getApplicationContext(),"Thank you!",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initCounter() {
        counter1 = 5;
        infoCounterTxt.setText(counter1 + "");
    }

    private void plusCounter1(){
        counter1++;
        infoCounterTxt.setText(counter1 + "");
    }
    private void minusCounter1(){
        counter1--;
        infoCounterTxt.setText(counter1 + "");
    }

    private void initCounter2() {
        counter2 = 5;
        dayCounterTxt.setText(counter2 + "");
    }

    private void plusCounter2(){
        counter2++;
        dayCounterTxt.setText(counter2 + "");
    }
    private void minusCounter2(){
        counter2--;
        dayCounterTxt.setText(counter2 + "");
    }


}
