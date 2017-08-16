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
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
 import android.content.Loader;
import android.net.Uri;
 import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>> {
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";
    private EarthquakeAdapter adapter;

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    //specify the ID of our loader which can be anything when we only have one loader
    private static final int EARTHQUAKE_LOADER_ID = 1;

    private TextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        emptyTextView = (TextView) findViewById(R.id.empty_view);

        // Create a fake list of earthquake locations.
        //final ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();

        // Find a reference to the {@link ListView} in the layout
        //Get a reference to our list view
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        //new adpater created (blank)
         adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        //


        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        //set an adapter for our listview
        earthquakeListView.setAdapter(adapter);
        earthquakeListView.setEmptyView(emptyTextView);
        //click listener for our views
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Earthquake currentQuake = adapter.getItem(i);

                String url = currentQuake.getURl();
                Uri webPage = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }
                //TODO get a JSON web url in order to make an intent to get more information from site
                //Intent intent = new Intent(this, )
            }
        });

        //LoaderAsyncTask instantiation
        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null,this);

    }
    //callsbacks do all of the work in terms of updating UI and etc
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int EARTHQUAKE_LOADER_ID, Bundle args) {
        Log.e("onCreateLoader","Loader created ");
        //TODO create a new loader for giver URL
        return new EarthquakeLoader(this,USGS_REQUEST_URL);
    }



    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        Log.e("onFinished", "Log done");
        // Clear the adapter of previous earthquake data
        adapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            adapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        //TODO loader reset, clear out our existing data
        Log.e("onLoaderReset", "Log done");

        adapter.clear();

    }


    //in class declaration of async
    public class EarthquakeASYNTask extends AsyncTask<String ,Void, List<Earthquake>>{

        //pass in the url of the API we wish to retrieve data from
        @Override
        protected List<Earthquake> doInBackground(String... strings) {
            if(strings.length <1 || strings[0] == null){
                return null;
            }
            List<Earthquake> result = QueryUtils.getArrayList(strings[0]);
            return result;
        }
        //update the UI
        @Override
        protected void onPostExecute(List<Earthquake> data) {
            adapter.clear();
            if(adapter != null ||!data.isEmpty()){
                adapter.addAll(data);
            }
        }
    }
}
