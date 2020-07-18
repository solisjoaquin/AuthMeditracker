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
     */
    public DataPoint() {

        // Return the values of x and y.
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
     * This sets the value of x.
     * @param xValue integer representing the x value.
     */
    public void setxValue(int xValue) {

        // Sets the xValue;
        this.xValue = xValue;
    }


    /**
     * This returns the value of y.
     * @return returns the value of y.
     */
    public int getyValue() {

        // Return the y value.
        return yValue;
    }


    /**
     * This sets the value of y.
     * @param yValue integer representing the x value.
     */
    public void setyValue(int yValue) {

        // Sets the xValue;
        this.yValue = yValue;
    }

}
