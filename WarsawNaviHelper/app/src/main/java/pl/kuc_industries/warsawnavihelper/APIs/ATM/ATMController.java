package pl.kuc_industries.warsawnavihelper.APIs.ATM;

import android.location.Location;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pl.kuc_industries.warsawnavihelper.APIs.ATM.Provider.IATM2ControllerProvider;
import pl.kuc_industries.warsawnavihelper.APIs.ATM.Query.IAtmAPI;
import pl.kuc_industries.warsawnavihelper.APIs.ATM.QueryResult.ATMQueryResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ATMController implements Callback<ATMQueryResult> {
    private static final String TAG = "ATMController";
    private static final String APIKEY = "AIzaSyAPoTqS0Kf17U-AvqA3lNMl18GjxqLEL5w";
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
    private static final String OUTPUT = "json";
    private static final String TYPE = "atm";

    private IAtmAPI mAtmAPI;
    private IATM2ControllerProvider mAtm2ControllerProvider;

    public ATMController(IATM2ControllerProvider iatm2ControllerProvider) {
        mAtm2ControllerProvider = iatm2ControllerProvider;
    }

    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mAtmAPI = retrofit.create(IAtmAPI.class);
    }

    @Override
    public void onResponse(Call<ATMQueryResult> call, Response<ATMQueryResult> response) {
        if(response.isSuccessful()) {
            Log.wtf(TAG, "Number of ATMs found: " + response.body().getATMs().size());
            if (response.body().getATMs() != null && response.body().getATMs().size() > 0)
                mAtm2ControllerProvider.ShowOnMap(response.body().getATMs());
        } else {
            Log.wtf(TAG, response.errorBody().toString());
        }
    }

    @Override
    public void onFailure(Call<ATMQueryResult> call, Throwable t) {
        t.printStackTrace();
        Log.wtf(TAG, t.getStackTrace().toString());
    }

    public void getATMs(Location location, int radius) {
        String strLoc = location.getLatitude() + "," + location.getLongitude();
        Call<ATMQueryResult> call =
                mAtmAPI.getNearbyATMs(OUTPUT, strLoc, radius, TYPE, APIKEY);
        call.enqueue(this);
    }

    public void getFilteredATMs(Location location, int radius, String filter) {
        String strLoc = location.getLatitude() + "," + location.getLongitude();
        Call<ATMQueryResult> call =
                mAtmAPI.getNearbyFilteredATMs(OUTPUT, strLoc, radius, filter, TYPE, APIKEY);
        call.enqueue(this);
    }
}
