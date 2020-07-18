package com.example.authbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * This is the Profile activity for the user.
 * This activity creates a chart for the user to easily digest their submissions.
 * @author Joaquin Solis, Tanner Olson and Travis Stirling.
 * @version 1.0
 */
public class ProfileActivity extends AppCompatActivity {

    // Declare TAG for log.
    private String TAG = "ProfileActivity";

    // Declare Firebase Authorization variable.
    private FirebaseAuth mAuth;

    // Declare Database variable.
    private DatabaseReference mDatabase;

    // Declare TextView variables.
    private TextView mWelcome, dayScale, feelScale;

    // Declare Int variables.
    int dayCounter, feelCounter;

    // Declare Chart variables.
    private LineChart lineChart;
    private LineDataSet lineDataSet = new LineDataSet(null,null);
    private ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();


    /**
     * Create the ProfileActivity and set the content view of that activity.
     * Create an instance of the Firebase authorization and the Firebase database.
     * Set the variables for TextViews and Int.
     * @param savedInstanceState Pass the state of the instance.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initializes the Firebase authorization.
        mAuth = FirebaseAuth.getInstance();

        // Gets the Database instance.
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Set the values of the textViews.
        mWelcome = findViewById(R.id.textViewNameProfile);
        dayScale = findViewById(R.id.textViewDayScale);
        feelScale = findViewById(R.id.textViewFeelScale);

        // Set values for integers.
        dayCounter = 5;
        feelCounter = 5;

        // Connect the line chart with the layout and set  preferences.
        lineChart = findViewById(R.id.lineChart);
        lineChart.getDescription().setEnabled(false);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setLineWidth(4);

        // Set the format of the chart.
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

        // Call the getUser() method.
        getUser();
    }


    /**
     * Pulls user information from the database to display a welcome message.
     * Initiates pulling user data from the database.
     */
    private void getUser() {

        // Get current user.
        FirebaseUser user = mAuth.getCurrentUser();

        // Asserts that user is not null.
        assert user != null;

        // Get the values that were input into the database.
        mDatabase.child("Patients").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Set string value for welcome message.
                String welcome = "Welcome " + dataSnapshot.child("name").getValue(String.class);

                // Sets the textView value to the welcome message.
                mWelcome.setText(welcome);

                // Retrieves previously stored data.
                getData();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                // Create an error log if not able to retrieve data from database.
                Log.w(TAG, "Failed to read value.");
            }
        });
    }


    /**
     * Retrieves the data and adds it to the layout.
     */
    private void getData() {

        // Get current user.
        FirebaseUser user = mAuth.getCurrentUser();

        // Asserts that user is not null.
        assert user != null;

        // Get the values that were input into the database.
        mDatabase.child("Patients").child(user.getUid()).child("ChartValues")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Create a new array to hold the data.
                ArrayList<Entry> array = new ArrayList<>();

                // If the data has children, get the x and y value.
                if (dataSnapshot.hasChildren()) {

                    // Loop through all the children.
                    for (DataSnapshot myDataSnapshot : dataSnapshot.getChildren()) {

                        // Get the values of the DataPoint.
                        DataPoint dataPoint = myDataSnapshot.getValue(DataPoint.class);

                        // Asserts that dataPoint is not null.
                        assert dataPoint != null;

                        // Add the values of the DataPoint to the array.
                        array.add(new Entry(dataPoint.getxValue(), dataPoint.getyValue()));
                    }

                    // Call the showChart() method.
                    showChart(array);
                } else {

                    // Clear and invalidate the line chart.
                    lineChart.clear();
                    lineChart.invalidate();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                // Create an error log if not able to retrieve data from database.
                Log.w(TAG, "Failed to get the values of the array.");
            }
        });
    }


    /**
     * Creates the chart and sets preferences for the information.
     * @param array Passes and array of x/y values.
     */
    private void showChart(ArrayList<Entry> array) {

        // Creates a new line of data.
        LineData lineData = new LineData(iLineDataSets);

        // Sets the values in the new line of data.
        lineDataSet.setValues(array);

        // Labels the line data.
        lineDataSet.setLabel("Health Progress");

        // Clears the array.
        iLineDataSets.clear();

        // Adds line data to the sets.
        iLineDataSets.add(lineDataSet);

        // Sets the data in the chart.
        lineChart.setData(lineData);

        // Clears the chart.
        lineChart.clear();

        // Invalidates the chart.
        lineChart.invalidate();
    }


    /**
     * Adds or Subtracts from the customers scale input.
     * @param view Pass the view.
     */
    @SuppressLint("SetTextI18n")
    public void clickValue(View view) {

        // Run through cases to add or subtract counter.
        if (view.getId() == R.id.buttonMinusScale) {
            dayCounter--;
            dayScale.setText(dayCounter+"");
        } else if (view.getId() == R.id.buttonPlusScale) {
            dayCounter++;
            dayScale.setText(dayCounter+"");
        } else if (view.getId() == R.id.buttonMinusFeel) {
            feelCounter--;
            feelScale.setText(feelCounter+"");
        } else if (view.getId() == R.id.buttonPlusFeel) {
            feelCounter++;
            feelScale.setText(feelCounter+"");
        }
    }


    /**
     * Submits the day and scale values to the database.
     * @param view Pass the view.
     */
    public void clickSubmit(View view) {

        // Get current user.
        FirebaseUser user = mAuth.getCurrentUser();

        // Asserts that user is not null.
        assert user != null;

        // Get the identification for the data.
        String id_data = mDatabase.child("Patients").child(user.getUid()).child("ChartValues")
                .push().getKey();

        // Get the integer from the dayScale and feelScale.
        int x = Integer.parseInt(dayScale.getText().toString());
        int y = Integer.parseInt(feelScale.getText().toString());

        // Creates a new DataPoint with values.
        DataPoint dataPoint = new DataPoint(x, y);

        // Asserts that id_data is not null.
        assert id_data != null;

        // Inserts data into database.
        mDatabase.child("Patients").child(user.getUid()).child("ChartValues").child(id_data)
                .setValue(dataPoint);

        // Thank the user for their submission.
        Toast.makeText(getApplicationContext(),"Thank you for your submission!",
                Toast.LENGTH_SHORT).show();
    }


    /**
     * Logs the user out of the app and returns them to the login page.
     * @param view Pass the view.
     */
    public void clickLogout(View view) {

        // Logs the user out of firebase.
        mAuth.signOut();

        // Start the login activity as a redirect.
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));

        // Toast notifying the user to 'Have a great day'.
        Toast.makeText(getApplicationContext(),"Have a great day!",Toast.LENGTH_SHORT).show();

        // Finishes the current activity.
        finish();
    }
}
