package pl.kuc_industries.warsawnavihelper.APIs.ATM;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        IAtmAPI atmAPI = retrofit.create(IAtmAPI.class);

        Call<ATMQueryResult> call = atmAPI.getNearbyATMs(OUTPUT,
                                                        "52.233333,21.016667",
                                                        1000,
                                                        "BPH",
                                                        TYPE,
                                                        APIKEY);
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<ATMQueryResult> call, Response<ATMQueryResult> response) {
        if(response.isSuccessful()) {
            Log.wtf(TAG, "Number of ATMs found: " + response.body().getATMs().size());
        } else {
            Log.wtf(TAG, response.errorBody().toString());
        }
    }

    @Override
    public void onFailure(Call<ATMQueryResult> call, Throwable t) {
        t.printStackTrace();
        Log.wtf(TAG, t.getStackTrace().toString());
    }
}
