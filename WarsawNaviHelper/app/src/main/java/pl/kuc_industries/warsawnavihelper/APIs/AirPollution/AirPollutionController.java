package pl.kuc_industries.warsawnavihelper.APIs.AirPollution;

import pl.kuc_industries.warsawnavihelper.APIs.AirPollution.Query.IAirPollutionAPI;
import pl.kuc_industries.warsawnavihelper.APIs.AirPollution.QueryResult.AirPollution;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AirPollutionController implements Callback<AirPollution> {
    private static final String TAG = "AirPollutionController";

    private static final String BASE_URL = "http://api.gios.gov.pl/pjp-api/rest/";

    private IAirPollutionAPI airPollutionAPI;

    public void start() {
        Gson gson = new GsonBuilder()
            .setLenient()
            .create();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        airPollutionAPI = retrofit.create(IAirPollutionAPI.class);

        Call<AirPollution> call = airPollutionAPI.getAirPollutionData(142);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<AirPollution> call, Response<AirPollution> response) {
        if(response.isSuccessful()) {
            Log.wtf(TAG, "ID: " + response.body().getId() + " Date: " + response.body().getStCalcDate());
        } else {
            Log.wtf(TAG, response.errorBody().toString());
        }
    }

    @Override
    public void onFailure(Call<AirPollution> call, Throwable t) {
        t.printStackTrace();
        Log.wtf(TAG, t.getStackTrace().toString());
    }

}

