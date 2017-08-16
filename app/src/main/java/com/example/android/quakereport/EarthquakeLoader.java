package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by EndUser on 8/15/2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private String mUrl;
    public EarthquakeLoader(Context con, String url){
        super(con);
        mUrl = url;
    }

    //decideds what the loader actually does
    @Override
    public List<Earthquake> loadInBackground() {
        //TODO implment this
        if(mUrl == null){
            return null;
        }
        List<Earthquake> result = QueryUtils.getArrayList(mUrl);
        return result;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

}