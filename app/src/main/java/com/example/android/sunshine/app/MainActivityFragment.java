package com.example.android.sunshine.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    //Constants
    private static final String API_KEY = "f8efb0eb0af423c6bdc88e557ae434eb";
    private static final String CITY_ZIP = "46506,us";
    private static final int FORECAST_DAYS = 7;

    //Instance variables
    private String httpApiCall = "http://api.openweathermap.org/data/2.5/forecast/daily?zip=" + CITY_ZIP + "&mode=json&units=metric&cnt=" + Integer.toString(FORECAST_DAYS) + "&appid=" + API_KEY;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        String[] forecastArray = {
                "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 70/46",
                "Wed - Cloudy - 72/63",
                "Thurs - Rainy - 64/51",
                "Fri - Foggy - 70/46",
                "Sat - Sunny - 76/68",
                "Sun - Sunny - 90/60"
        };

        List<String> forecast = new ArrayList<>(Arrays.asList(forecastArray));

        ArrayAdapter<String> mForecastAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, forecast);


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;


        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast

            URL url = new URL(httpApiCall);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

        return rootView;
    }

}