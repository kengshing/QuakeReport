package com.example.android.quakereport;



class Earthquake {

    private final double mMagnitude;
    private final String mPlace;
    private final long mDateTime;
    private final String mUrl;

    Earthquake(double magnitude, String place, long dateTime, String url){
        mMagnitude = magnitude;
        mPlace = place;
        mDateTime = dateTime;
        mUrl = url;
    }

    public Double getmMagnitude() {
        return mMagnitude;
    }

    public String getmPlace() {
        return mPlace;
    }

    public long getmDateTime(){
        return mDateTime;
    }

    public String getmUrl() {
        return mUrl;
    }
}
