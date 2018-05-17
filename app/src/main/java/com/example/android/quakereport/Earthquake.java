package com.example.android.quakereport;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

class Earthquake {

    private final double mMagnitude;
    private final String mPlace;
    private final long mTime;

    Earthquake(double magnitude, String place, long time){
        mMagnitude = magnitude;
        mPlace = place;
        mTime = time;
    }

    public String getmMagnitude() {
        String mMagnitudeS = String.valueOf(mMagnitude);
        return mMagnitudeS;
    }

    public String getmPlace() {
        return mPlace;
    }

    public String getmTime() {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(mTime);
        String mTimeS = DateFormat.format("MMM dd, yyyy", cal).toString();
        return mTimeS;
    }
}
