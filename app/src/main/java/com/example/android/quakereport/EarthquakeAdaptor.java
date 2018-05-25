package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.graphics.drawable.GradientDrawable;


class EarthquakeAdaptor extends ArrayAdapter {
    public EarthquakeAdaptor(@NonNull Context context, @NonNull ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        final Earthquake currentEarthQuake = (Earthquake) getItem(position);

        TextView textMagnitudeView = listItemView.findViewById(R.id.txt_magnitude_view);
        textMagnitudeView.setText(formatMagnitude(currentEarthQuake.getmMagnitude()));
        GradientDrawable magnitudeCircle = (GradientDrawable) textMagnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthQuake.getmMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        TextView textSecondaryPlaceView = listItemView.findViewById(R.id.txt_secondary_place_view);
        textSecondaryPlaceView.setText(getSecondaryPlace(currentEarthQuake.getmPlace()));

        TextView textPrimaryPlaceView = listItemView.findViewById(R.id.txt_primary_place_view);
        textPrimaryPlaceView.setText(getPrimaryPlace(currentEarthQuake.getmPlace()));

        TextView textDateView = listItemView.findViewById(R.id.txt_date_view);
        textDateView.setText(getDate(currentEarthQuake.getmDateTime()));

        TextView textTimeView = listItemView.findViewById(R.id.txt_time_view);
        textTimeView.setText(getTime(currentEarthQuake.getmDateTime()));

        return listItemView;
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private String getPrimaryPlace(String place) {
        if (place.contains("of")) {
            int stringLength = place.length();
            int splitIndex = place.indexOf("of") + 3;
            String mPrimaryPlace = place.substring(splitIndex, stringLength);
            return mPrimaryPlace;
        } else {
            return place;
        }
    }

    private String getSecondaryPlace(String place) {
        if (place.contains("of")) {
            int splitIndex = place.indexOf("of") + 3;
            String mSecondaryPlace = place.substring(0, splitIndex);
            return mSecondaryPlace;
        } else {
            return "Near to";
        }
    }

    private String getTime(long dateTime) {
        Date dateObject = new Date(dateTime);
        String mTime = formatTime(dateObject);
        return mTime;
    }

    private String getDate(long dateTime) {
        Date dateObject = new Date(dateTime);
        String mDate = formatDate(dateObject);
        return mDate;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    /**
     * Return the magnitude circle color code based on the magnitude value
     */
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
