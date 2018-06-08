/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.net.ConnectivityManager;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    Toolbar mainToolbar;
    Spinner mainSpinner;
    String usgsRequestUrl;
    TextView listLoadingMessageView;
    ProgressBar listProgressBar;
    TextView listEmptyView;
    Spinner qASpinner;
    /**
     * URL for earthquake data from the USGS dataset
     */
    // private static final String USGS_REQUEST_URL =
    // "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";

    private EarthquakeAdaptor mAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mian, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        qASpinner = (Spinner) item.getActionView();
        ArrayAdapter<CharSequence> qAAdaptor = ArrayAdapter.createFromResource(this, R.array.quake_type, R.layout.spinner_text_layout);
        qAAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qASpinner.setAdapter(qAAdaptor);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
               if (id ==R.id.action_settings){
            Log.e(LOG_TAG, "action_settings selected");
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
            }
           return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.earthquake_activity);

        listLoadingMessageView = findViewById(R.id.list_loading_message_view);
        listProgressBar = findViewById(R.id.list_progress_bar);
        listEmptyView = findViewById(R.id.list_empty_view);
        mainSpinner = findViewById(R.id.spinner_view);

        ArrayAdapter<String> spinnerAdaptor = new ArrayAdapter<String>(EarthquakeActivity.this, R.layout.spinner_text_layout, getResources().getStringArray(R.array.quake_type));
        spinnerAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainSpinner.setAdapter(spinnerAdaptor);

        mainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listEmptyView.setText(null);
                listLoadingMessageView.setText(R.string.loading_message);
                listLoadingMessageView.setVisibility(View.VISIBLE);
                listProgressBar.setVisibility(View.VISIBLE);
                getLoaderManager().destroyLoader(0);

                switch (position) {
                    case 0:
                        usgsRequestUrl = setCustomUrl();
                        //Log.e(LOG_TAG, "Custom usgsRequestUrl: " + usgsRequestUrl);
                        refreshScreen();
                        break;

                    case 1:
                        usgsRequestUrl = getString(R.string.usgs_request_url_recent);
                        refreshScreen();
                        break;
                    case 2:
                        usgsRequestUrl = getString(R.string.usgs_request_url_since2000);
                        refreshScreen();
                        break;
                    case 3:
                        usgsRequestUrl = getString(R.string.usgs_request_url_test);
                        refreshScreen();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                usgsRequestUrl = getString(R.string.usgs_request_url_test);
                refreshScreen();
            }
        });
    }

    public String setCustomUrl(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(EarthquakeActivity.this);
        String minMagnitude = sharedPreferences.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        //Log.e(LOG_TAG, "minMagnitude:" + minMagnitude + "  orderBy: " + orderBy);
        String usgsRequestUrlBase = getString(R.string.usgs_request_url_template);
        StringBuilder usgsRequestUrlBuilder = new StringBuilder();
        usgsRequestUrlBuilder.append(usgsRequestUrlBase);
        usgsRequestUrlBuilder.append("format=" + "geojson");
        usgsRequestUrlBuilder.append("&limit=" + "10");
        usgsRequestUrlBuilder.append("&minmag=" + minMagnitude);
        usgsRequestUrlBuilder.append("&orderby=" + orderBy);
        return usgsRequestUrlBuilder.toString();
       };

    @Override
    public android.content.Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        return new EarthquakeLoader(this, usgsRequestUrl);
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        mAdapter.clear();
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Earthquake>> loader, List<Earthquake> data) {

        listEmptyView.setText(null);
        listLoadingMessageView.setVisibility(View.GONE);
        listProgressBar.setVisibility(View.GONE);

        //Clear the adapter of previous earthquake data
        mAdapter.clear();

        //If there is a valid list of Earthquakes, then add them to the adapter's
        //dataset. This will trigger the ListView update
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        } else {
            listEmptyView.setText(R.string.no_earthquake_message);
        }
    }

    public void refreshScreen() {

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById(R.id.list);
        earthquakeListView.setEmptyView(findViewById(R.id.list_empty_view));

        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new EarthquakeAdaptor(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake currentEarthQuake = (Earthquake) mAdapter.getItem(position);
                Uri earthQuakeUri = Uri.parse(currentEarthQuake.getmUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, earthQuakeUri);
                startActivity(webIntent);
            }
        });

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            listLoadingMessageView.setText(R.string.no_internet_message);
            listLoadingMessageView.setVisibility(View.VISIBLE);
            listProgressBar.setVisibility(View.GONE);
        }

    }
}
