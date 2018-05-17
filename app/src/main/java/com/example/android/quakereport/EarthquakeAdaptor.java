package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class EarthquakeAdaptor extends ArrayAdapter {
    public EarthquakeAdaptor(@NonNull Context context, int resource, @NonNull ArrayList<Earthquake> earthquakes) {
        super(context, resource, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if(listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        final Earthquake currentEarthQuake = (Earthquake) getItem(position);

        TextView textMagnitudeView = listView.findViewById(R.id.txt_magnitude_view);
        textMagnitudeView.setText(currentEarthQuake.getmMagnitude());

        TextView textPlaceView = listView.findViewById(R.id.txt_place_view);
        textPlaceView.setText(currentEarthQuake.getmPlace());

        TextView textTimeView = listView.findViewById(R.id.txt_time_view);
        textTimeView.setText(currentEarthQuake.getmTime());

        return listView;
    }
}
