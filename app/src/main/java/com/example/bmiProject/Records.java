package com.example.bmiProject;

public class Records {
    String statusRecords;
    double bmiRecords;
    String weightRecords;
    String dateRecords;
    String timeRecords;
    String lengthRecords;
    String uId;
    public Records() {
    }

    public Records(String dateRecords, String timeRecords, String lengthRecords, String weightRecords, String uId, String statusRecords, double bmiRecords) {

        this.bmiRecords = bmiRecords;
        this.lengthRecords = lengthRecords;
        this.dateRecords = dateRecords;
        this.weightRecords = weightRecords;
        this.uId = uId;
        this.statusRecords = statusRecords;
        this.timeRecords = timeRecords;
    }

    public String getStatusRecords() {
        return statusRecords;
    }

    public double getBmiRecords() {
        return bmiRecords;
    }

    public String getWeightRecords() {
        return weightRecords;
    }

    public void setWeightRecords(String weightRecords) {
        this.weightRecords = weightRecords;
    }

    public void setStatusRecords(String statusRecords) {
        this.statusRecords = statusRecords;
    }

    public void setBmiRecords(double bmiRecords) {
        this.bmiRecords = bmiRecords;
    }

    public String getTimeRecords() {
        return timeRecords;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getDateRecords() {
        return dateRecords;
    }

    public void setDateRecords(String dateRecords) {
        this.dateRecords = dateRecords;
    }

    public void setTimeRecords(String timeRecords) {
        this.timeRecords = timeRecords;
    }

    public String getLengthRecords() {
        return lengthRecords;
    }

    public void setLengthRecords(String lengthRecords) {
        this.lengthRecords = lengthRecords;
    }
}

