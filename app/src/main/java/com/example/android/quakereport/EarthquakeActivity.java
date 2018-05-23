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

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    /** URL for earthquake data from the USGS dataset */
   // private static final String USGS_REQUEST_URL =
           //"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";



    private EarthquakeAdaptor mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String usgsRequestUrl = getString(R.string.usgs_request_url_since2000);

        setContentView(R.layout.earthquake_activity);

        // Create a fake list of earthquake locations.
         //ArrayList<Earthquake> earthquakes = QueryUtils.ExtractFeatureFromJson();


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
       // mAdapter = new EarthquakeAdaptor (this, R.layout.list_item, new ArrayList<Earthquake>());
        mAdapter = new EarthquakeAdaptor (this, new ArrayList<Earthquake>());

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

        //Start the AsyncTask to fatch the earthquake data
        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        //task.execute(USGS_REQUEST_URL);
        task.execute(usgsRequestUrl);

    }

    private class EarthquakeAsyncTask extends AsyncTask<String,Void, List<Earthquake>>{

        @Override
        protected List<Earthquake> doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Earthquake> result = QueryUtils.fetchEarthQuakeData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Earthquake> data) {
            //Clear the adapter of previous earthquake data
            mAdapter.clear();

            //If there is a valid list of Earthquakes, then add them to the adapter's
            //dataset. This will trigger the ListView update
            if (data != null && !data.isEmpty()){
                mAdapter.addAll(data);
            }

        }
    }
}
