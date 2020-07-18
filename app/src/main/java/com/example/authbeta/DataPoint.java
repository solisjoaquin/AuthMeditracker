package com.example.authbeta;


/**
 * This is the DataPoint class.
 * This sets the values of x and y for the line chart.
 * @author Joaquin Solis, Tanner Olson and Travis Stirling.
 * @version 1.0
 */
public class DataPoint {

    // Declare Int variables.
    int xValue;
    int yValue;


    /**
     * This sets the x and y values for the line chart.
     * @param xValue represents the x value.
     * @param yValue represents the y value.
     */
    public DataPoint(int xValue, int yValue) {

        // Set the values of x and y.
        this.xValue = xValue;
        this.yValue = yValue;
    }


    /**
     * This returns the value of x.
     * @return returns the value of x.
     */
    public int getxValue() {

        // Return the x value.
        return xValue;
    }


    /**
     * This returns the value of y.
     * @return returns the value of y.
     */
    public int getyValue() {

        // Return the y value.
        return yValue;
    }
}
