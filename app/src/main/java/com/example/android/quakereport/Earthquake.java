package com.example.android.quakereport;

import android.widget.ArrayAdapter;
import android.graphics.drawable.GradientDrawable;
 import android.support.v4.content.ContextCompat;

import java.util.Date;

/**
 * Created by EndUser on 8/6/2017.
 */

public class Earthquake{
    private String Url;
    private double magnitude;
    private String location;
    private long date;

    public Earthquake(){
    }
    public Earthquake(double mag,String loc,long date, String url){
        magnitude = mag;
        location = loc;
        this.date = date;
        Url = url;
    }

    public double getMagnitude(){
        return magnitude;
    }

    public String getLocation(){
        return location;
    }

    public long getDate(){
        return date;
    }
    public String getURl(){return Url;}
}
