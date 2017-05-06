package pl.kuc_industries.warsawnavihelper;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import pl.kuc_industries.warsawnavihelper.StopsWithLocations.Stop;
import pl.kuc_industries.warsawnavihelper.StopsWithLocations.StopsWithLocations;

/**
 * Created by Szymek on 23.04.2017.
 */

public final class Constants {
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "pl.kuc_industries.warsawnavihelper";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    public static final int TRAM__LINES_PER_ROW = 6;
    public static final int BUS_LINES_PER_ROW = 5;
    // TODO: use ProGuard or other API keys storing method:
    public static final String UM_API_KEY = "b2404019-9b1d-4ff5-9969-af137b28c7da";
    public static final String PLACES_API_KEY = "AIzaSyAPoTqS0Kf17U-AvqA3lNMl18GjxqLEL5w";

    public static Map<String, LatLng> stopToLocationMap;

    public static void initializeStopToLocationMap(Context ctx) throws IOException, JSONException {
        stopToLocationMap = new HashMap<>();

        Gson gson = new Gson();
        InputStream is = ctx.getResources().openRawResource(R.raw.stops);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        String jsonString = writer.toString();
        StopsWithLocations swl = gson.fromJson(jsonString, StopsWithLocations.class);

        for (Stop stop : swl.getStops()) {
            stopToLocationMap.put(stop.getName(), new LatLng(stop.getLat(), stop.getLng()));
        }
    }

}