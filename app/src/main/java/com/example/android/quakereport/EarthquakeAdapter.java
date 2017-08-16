package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.SimpleTimeZone;

/**
 * Created by EndUser on 8/6/2017.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> list){
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //assign the current view to a varible for manipulation
        View listItemView = convertView;
        if(listItemView == null){
            //if the view is empty we inflate it
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_textview,parent,false);
        }
        //get a reference to the current earthquake
        Earthquake currentEarthquake = getItem(position);
        //set the magnitude textview
        TextView magnitude = (TextView) listItemView.findViewById(R.id.magnitude_textview);
        //formated magnitude
        String formatedMagnitude = formatDecimal(currentEarthquake.getMagnitude());
        magnitude.setText(formatedMagnitude);
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        //Original string which contains the city and the range from the city. We have to split it
        String ogCityText = currentEarthquake.getLocation();
        String rangeText;
        String cityText;
        if(ogCityText.contains("of")) {
            int rangeEnd = ogCityText.indexOf("f");
             rangeText = ogCityText.substring(0, rangeEnd+1);
             cityText = ogCityText.substring(rangeEnd+2, ogCityText.length());
        }
        else{
            rangeText = "Near the";
            int cityBegin = ogCityText.indexOf("-");
            cityText = ogCityText.substring(cityBegin+1, ogCityText.length());

        }

        //range from city location
        TextView rangeFromCity = (TextView) listItemView.findViewById(R.id.distance_from_city);
        rangeFromCity.setText(rangeText);
        //city textview
        TextView city = (TextView) listItemView.findViewById(R.id.city_textview);
         city.setText(cityText);
        //third textview
        TextView date = (TextView) listItemView.findViewById(R.id.date_texview);
        Date dateObject = new Date(currentEarthquake.getDate());
        String formatedDate = formatDate(dateObject);
        date.setText(formatedDate);

        TextView time = (TextView) listItemView.findViewById(R.id.time_textview);
        String formatTime = formatTime(dateObject);
        time.setText(formatTime);










        return listItemView;
    }
    private String formatDate(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }
    private String formatTime(Date time){
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(time);
    }
    private String formatDecimal(double magnitude){
        DecimalFormat magFormat = new DecimalFormat("0.0");
        return magFormat.format(magnitude);
    }
    private int getMagnitudeColor(double magnitude){
        int magInt = (int) magnitude;
        int magnitudeColorResourceId;
        switch(magInt){
            case 1: magnitudeColorResourceId = R.color.magnitude1;
                    break;
            case 2: magnitudeColorResourceId =R.color.magnitude2;
                    break;
            case 3: magnitudeColorResourceId = R.color.magnitude3;
                    break;
            case 4: magnitudeColorResourceId = R.color.magnitude4;
                    break;
            case 5: magnitudeColorResourceId = R.color.magnitude5;
                    break;
            case 6: magnitudeColorResourceId = R.color.magnitude6;
                    break;
            case 7: magnitudeColorResourceId = R.color.magnitude7;
                    break;
            case 8: magnitudeColorResourceId = R.color.magnitude8;
                    break;
            case 9: magnitudeColorResourceId = R.color.magnitude9;
                    break;
            default:
                    magnitudeColorResourceId = R.color.magnitude10plus;
                    break;
        }
        return ContextCompat.getColor(getContext(),magnitudeColorResourceId);
    }
}
