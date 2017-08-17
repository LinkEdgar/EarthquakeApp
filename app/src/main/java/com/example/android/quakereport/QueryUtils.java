package com.example.android.quakereport;

import android.util.Log;
import android.util.StringBuilderPrinter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {


    /** Sample JSON response for a USGS query */
    private static final String SAMPLE_JSON_RESPONSE = "{\"type\":\"FeatureCollection\",\"metadata\":{\"generated\":1462295443000,\"url\":\"http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-01-31&minmag=6&limit=10\",\"title\":\"USGS Earthquakes\",\"status\":200,\"api\":\"1.5.2\",\"limit\":10,\"offset\":1,\"count\":10},\"features\":[{\"type\":\"Feature\",\"properties\":{\"mag\":7.2,\"place\":\"88km N of Yelizovo, Russia\",\"time\":1454124312220,\"updated\":1460674294040,\"tz\":720,\"url\":\"http://earthquake.usgs.gov/earthquakes/eventpage/us20004vvx\",\"detail\":\"http://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us20004vvx&format=geojson\",\"felt\":2,\"cdi\":3.4,\"mmi\":5.82,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":1,\"sig\":798,\"net\":\"us\",\"code\":\"20004vvx\",\"ids\":\",at00o1qxho,pt16030050,us20004vvx,gcmt20160130032510,\",\"sources\":\",at,pt,us,gcmt,\",\"types\":\",cap,dyfi,finite-fault,general-link,general-text,geoserve,impact-link,impact-text,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,tectonic-summary,\",\"nst\":null,\"dmin\":0.958,\"rms\":1.19,\"gap\":17,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 7.2 - 88km N of Yelizovo, Russia\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[158.5463,53.9776,177]},\"id\":\"us20004vvx\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":3.1,\"place\":\"94km SSE of Taron, Papua New Guinea\",\"time\":1453777820750,\"updated\":1460156775040,\"tz\":600,\"url\":\"http://earthquake.usgs.gov/earthquakes/eventpage/us20004uks\",\"detail\":\"http://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us20004uks&format=geojson\",\"felt\":null,\"cdi\":null,\"mmi\":4.1,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":1,\"sig\":572,\"net\":\"us\",\"code\":\"20004uks\",\"ids\":\",us20004uks,gcmt20160126031023,\",\"sources\":\",us,gcmt,\",\"types\":\",cap,geoserve,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,tectonic-summary,\",\"nst\":null,\"dmin\":1.537,\"rms\":0.74,\"gap\":25,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.1 - 94km SSE of Taron, Papua New Guinea\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[153.2454,-5.2952,26]},\"id\":\"us20004uks\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":6.3,\"place\":\"50km NNE of Al Hoceima, Morocco\",\"time\":1453695722730,\"updated\":1460156773040,\"tz\":0,\"url\":\"http://earthquake.usgs.gov/earthquakes/eventpage/us10004gy9\",\"detail\":\"http://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004gy9&format=geojson\",\"felt\":117,\"cdi\":7.2,\"mmi\":5.28,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":0,\"sig\":695,\"net\":\"us\",\"code\":\"10004gy9\",\"ids\":\",us10004gy9,gcmt20160125042203,\",\"sources\":\",us,gcmt,\",\"types\":\",cap,dyfi,geoserve,impact-text,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,tectonic-summary,\",\"nst\":null,\"dmin\":2.201,\"rms\":0.92,\"gap\":20,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.3 - 50km NNE of Al Hoceima, Morocco\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.6818,35.6493,12]},\"id\":\"us10004gy9\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":1.1,\"place\":\"86km E of Old Iliamna, Alaska\",\"time\":1453631430230,\"updated\":1460156770040,\"tz\":-540,\"url\":\"http://earthquake.usgs.gov/earthquakes/eventpage/us10004gqp\",\"detail\":\"http://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004gqp&format=geojson\",\"felt\":1816,\"cdi\":7.2,\"mmi\":6.6,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":1,\"sig\":1496,\"net\":\"us\",\"code\":\"10004gqp\",\"ids\":\",at00o1gd6r,us10004gqp,ak12496371,gcmt20160124103030,\",\"sources\":\",at,us,ak,gcmt,\",\"types\":\",cap,dyfi,finite-fault,general-link,general-text,geoserve,impact-link,impact-text,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,tectonic-summary,trump-origin,\",\"nst\":null,\"dmin\":0.72,\"rms\":2.11,\"gap\":19,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 7.1 - 86km E of Old Iliamna, Alaska\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-153.4051,59.6363,129]},\"id\":\"us10004gqp\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":4.6,\"place\":\"215km SW of Tomatlan, Mexico\",\"time\":1453399617650,\"updated\":1459963829040,\"tz\":-420,\"url\":\"http://earthquake.usgs.gov/earthquakes/eventpage/us10004g4l\",\"detail\":\"http://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004g4l&format=geojson\",\"felt\":11,\"cdi\":2.7,\"mmi\":3.92,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":1,\"sig\":673,\"net\":\"us\",\"code\":\"10004g4l\",\"ids\":\",at00o1bebo,pt16021050,us10004g4l,gcmt20160121180659,\",\"sources\":\",at,pt,us,gcmt,\",\"types\":\",cap,dyfi,geoserve,impact-link,impact-text,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,tectonic-summary,\",\"nst\":null,\"dmin\":2.413,\"rms\":0.98,\"gap\":74,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.6 - 215km SW of Tomatlan, Mexico\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-106.9337,18.8239,10]},\"id\":\"us10004g4l\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":6.7,\"place\":\"52km SE of Shizunai, Japan\",\"time\":1452741933640,\"updated\":1459304879040,\"tz\":540,\"url\":\"http://earthquake.usgs.gov/earthquakes/eventpage/us10004ebx\",\"detail\":\"http://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004ebx&format=geojson\",\"felt\":51,\"cdi\":5.8,\"mmi\":6.45,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":1,\"sig\":720,\"net\":\"us\",\"code\":\"10004ebx\",\"ids\":\",us10004ebx,pt16014050,at00o0xauk,gcmt20160114032534,\",\"sources\":\",us,pt,at,gcmt,\",\"types\":\",associate,cap,dyfi,geoserve,impact-link,impact-text,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,\",\"nst\":null,\"dmin\":0.281,\"rms\":0.98,\"gap\":22,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.7 - 52km SE of Shizunai, Japan\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[142.781,41.9723,46]},\"id\":\"us10004ebx\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":6.1,\"place\":\"12km WNW of Charagua, Bolivia\",\"time\":1452741928270,\"updated\":1459304879040,\"tz\":-240,\"url\":\"http://earthquake.usgs.gov/earthquakes/eventpage/us10004ebw\",\"detail\":\"http://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004ebw&format=geojson\",\"felt\":3,\"cdi\":2.2,\"mmi\":2.21,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":0,\"sig\":573,\"net\":\"us\",\"code\":\"10004ebw\",\"ids\":\",us10004ebw,gcmt20160114032528,\",\"sources\":\",us,gcmt,\",\"types\":\",cap,dyfi,geoserve,impact-text,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,tectonic-summary,\",\"nst\":null,\"dmin\":5.492,\"rms\":1.04,\"gap\":16,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.1 - 12km WNW of Charagua, Bolivia\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-63.3288,-19.7597,582.56]},\"id\":\"us10004ebw\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":8.2,\"place\":\"74km NW of Rumoi, Japan\",\"time\":1452532083920,\"updated\":1459304875040,\"tz\":540,\"url\":\"http://earthquake.usgs.gov/earthquakes/eventpage/us10004djn\",\"detail\":\"http://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004djn&format=geojson\",\"felt\":8,\"cdi\":3.4,\"mmi\":3.74,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":0,\"sig\":594,\"net\":\"us\",\"code\":\"10004djn\",\"ids\":\",us10004djn,gcmt20160111170803,\",\"sources\":\",us,gcmt,\",\"types\":\",cap,dyfi,geoserve,impact-text,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,tectonic-summary,\",\"nst\":null,\"dmin\":1.139,\"rms\":0.96,\"gap\":33,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.2 - 74km NW of Rumoi, Japan\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[141.0867,44.4761,238.81]},\"id\":\"us10004djn\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":6.5,\"place\":\"227km SE of Sarangani, Philippines\",\"time\":1452530285900,\"updated\":1459304874040,\"tz\":480,\"url\":\"http://earthquake.usgs.gov/earthquakes/eventpage/us10004dj5\",\"detail\":\"http://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004dj5&format=geojson\",\"felt\":1,\"cdi\":2.7,\"mmi\":7.5,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":1,\"sig\":650,\"net\":\"us\",\"code\":\"10004dj5\",\"ids\":\",at00o0srjp,pt16011050,us10004dj5,gcmt20160111163807,\",\"sources\":\",at,pt,us,gcmt,\",\"types\":\",cap,dyfi,geoserve,impact-link,impact-text,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,tectonic-summary,\",\"nst\":null,\"dmin\":3.144,\"rms\":0.72,\"gap\":22,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.5 - 227km SE of Sarangani, Philippines\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[126.8621,3.8965,13]},\"id\":\"us10004dj5\"},\n" +
            "{\"type\":\"Feature\",\"properties\":{\"mag\":6,\"place\":\"Pacific-Antarctic Ridge\",\"time\":1451986454620,\"updated\":1459202978040,\"tz\":-540,\"url\":\"http://earthquake.usgs.gov/earthquakes/eventpage/us10004bgk\",\"detail\":\"http://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004bgk&format=geojson\",\"felt\":0,\"cdi\":1,\"mmi\":0,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":0,\"sig\":554,\"net\":\"us\",\"code\":\"10004bgk\",\"ids\":\",us10004bgk,gcmt20160105093415,\",\"sources\":\",us,gcmt,\",\"types\":\",cap,dyfi,geoserve,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,\",\"nst\":null,\"dmin\":30.75,\"rms\":0.67,\"gap\":71,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.0 - Pacific-Antarctic Ridge\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-136.2603,-54.2906,10]},\"id\":\"us10004bgk\"}],\"bbox\":[-153.4051,-54.2906,10,158.5463,59.6363,582.56]}";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    //added a string parameter to accept the new JSON string
    public static ArrayList<Earthquake> extractEarthquakes(String extractURL) {
        //new line of code for this method
        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        //This try block is used to test the UI's progressbar loading animation
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            //parses the complete JSON string
            JSONObject earthquakeData = new JSONObject(extractURL);
            //We are interested in the feature array becuase this has the relevant earthquake info
            JSONArray feature = earthquakeData.getJSONArray("features");
            //we iterate through each feature
            for(int x = 0; x < feature.length(); x++){
                //we access each feature through the counter x
                JSONObject featureIterate = feature.getJSONObject(x);
                //we want to go deeper into the nesting of the JSON object because that's where the data we want lies
                JSONObject property = featureIterate.getJSONObject("properties");
                // we extract the data we want
                long time = property.getLong("time");
                String place = property.getString("place");
                double magnitude = property.getDouble("mag");
                String url = property.getString("url");
                //make a new object of our custom class and fill it with the extracted data
                Earthquake insertion = new Earthquake(magnitude,place,time, url);
                //finally we add it to the arrayList we have created
                earthquakes.add(insertion);

            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
    public static List<Earthquake> getArrayList(String stringUrl){
        // calls the method createUrl to turn the string url into a Url object
        URL newURL = createUrl(stringUrl);
        //make an HTTP connections to retrieve the desired data
         String JSONString= makeHTTPRequest(newURL);
        List earthquakes = extractEarthquakes(JSONString);
        return earthquakes;
    }

    public static URL createUrl(String stringUrl){
        URL url = null;
        try{
           url = new URL(stringUrl);
        }
        catch(MalformedURLException e){
            Log.e("createUrl in Util", "could not create a proper URL ");
        }
        return url;
    }
    public static String makeHTTPRequest(URL url){
        String JSON = "";
        if(url == null){
            return JSON;
        }
        InputStream input = null;
        HttpURLConnection httpURLConnection = null;
        try {
            //creates a url connection object from the url.openconnection object
            httpURLConnection = (HttpURLConnection) url.openConnection();
            //arbritrary read and connect timeout
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            //200 is success code
            if(httpURLConnection.getResponseCode() == 200){
                input = httpURLConnection.getInputStream();
                JSON = inputstreamtoString(input);
            }
        } catch (IOException e) {
            Log.e("makeHTTPRequest","Couldn't open the connection");
        }
        finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return JSON;
    }
    public  static String inputstreamtoString(InputStream inputStream) throws  IOException{
        // use a builder for efficiency sake
        StringBuilder builder = new StringBuilder();
        if(inputStream != null){
            // reads the inputStream after specifying to android what we're looking for which in this case is an UTF-8 inputstream
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while(line != null){
                builder.append(line);
                line = bufferedReader.readLine();
            }
        }



        return builder.toString();
    }


}