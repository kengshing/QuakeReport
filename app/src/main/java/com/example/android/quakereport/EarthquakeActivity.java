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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of earthquake locations.
       final ArrayList<Earthquake> earthquakes = new ArrayList<>();
        earthquakes.add(new Earthquake(1.0, "San Francisco", 1095292800));
        earthquakes.add(new Earthquake(1.1,"London", 1096292800));
        earthquakes.add(new Earthquake(2.0,"Tokyo", 1097292800));
        earthquakes.add(new Earthquake(3.0,"Mexico City", 1098292800));
        earthquakes.add(new Earthquake(4.0,"Moscow", 1099292800));
        earthquakes.add(new Earthquake(2.2,"Rio de Janeiro", 1100292800));
        earthquakes.add(new Earthquake(0.5,"Paris", 1101292800));

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        EarthquakeAdaptor  adapter = new EarthquakeAdaptor (this, R.layout.list_item, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
    }
}
