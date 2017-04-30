package pl.kuc_industries.warsawnavihelper.APIs.AirPollution;

import pl.kuc_industries.warsawnavihelper.APIs.AirPollution.Provider.AirPollutionProvider;
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

    private IAirPollutionAPI mAirPollutionAPI;
    private final AirPollutionProvider mAirPollutionProvider;
    //TODO: station with id=142 is not located in Warsaw, but it proved to be working almost always
    // (unlike ALL stations from Warsaw)
    private final Integer[] stationsIds = {142, 141, 155, 147, 71, 63 };
    private int currentStationIdx = 0;

    public AirPollutionController(AirPollutionProvider airPollutionProvider){
          this.mAirPollutionProvider = airPollutionProvider;
    }

    public void start() {
        Gson gson = new GsonBuilder()
            .setLenient()
            .create();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        mAirPollutionAPI = retrofit.create(IAirPollutionAPI.class);
    }

    @Override
    public void onResponse(Call<AirPollution> call, Response<AirPollution> response) {
        if(response.isSuccessful()) {
            Log.wtf(TAG, "ID: " + response.body().getId() + " Date: " + response.body().getStCalcDate());
            mAirPollutionProvider.ModifyInterface(response.body());
        } else {
            Log.wtf(TAG, response.errorBody().toString());
            currentStationIdx++;
            if (currentStationIdx == stationsIds.length)
            {
                currentStationIdx = 0;
                return;
            }
            getAirPollutionData();
        }
    }

    @Override
    public void onFailure(Call<AirPollution> call, Throwable t) {
        t.printStackTrace();
        Log.wtf(TAG, t.getStackTrace().toString());
    }

    public void getAirPollutionData() {
        Call<AirPollution> call = mAirPollutionAPI.getAirPollutionData(stationsIds[currentStationIdx]);
        call.enqueue(this);
    }

}

