package com.example.authbeta;

public class DataPoint {
    int xValue, yValue;
    String description;

    public DataPoint(int xValue, int yValue) {
        this.xValue = xValue;
        this.yValue = yValue;
    }

    public DataPoint() {
    }

    public int getxValue() {
        return xValue;
    }

    public int getyValue() {
        return yValue;
    }

    public String getDescription(){
        return description;
    }
}
