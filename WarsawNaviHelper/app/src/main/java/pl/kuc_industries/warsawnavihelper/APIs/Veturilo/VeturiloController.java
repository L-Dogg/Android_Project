package pl.kuc_industries.warsawnavihelper.APIs.Veturilo;

import android.nfc.Tag;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import pl.kuc_industries.warsawnavihelper.APIs.Veturilo.Provider.IVeturilo2ControllerProvider;
import pl.kuc_industries.warsawnavihelper.APIs.Veturilo.Provider.IVeturilo2ViewProvider;
import pl.kuc_industries.warsawnavihelper.APIs.Veturilo.Query.IVeturiloAPI;
import pl.kuc_industries.warsawnavihelper.APIs.Veturilo.QueryResult.VeturiloQueryResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mateusz on 5/6/17.
 */

public class VeturiloController implements Callback<VeturiloQueryResult> {
    public static final String TAG = "VeturiloController";

    private static final String BASE_URL = "https://api.citybik.es/v2/";
    private static final String[] NETWORK_IDS = {"veturilo", "stacje-sponsorskie-veturilo"};
    private IVeturiloAPI mVeturiloAPI;
    private IVeturilo2ControllerProvider mToControllerProvider;

    public VeturiloController(IVeturilo2ControllerProvider toControllerProvider) {
        this.mToControllerProvider = toControllerProvider;
    }

    public void start() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mVeturiloAPI = retrofit.create(IVeturiloAPI.class);
    }


    @Override
    public void onResponse(Call<VeturiloQueryResult> call, Response<VeturiloQueryResult> response) {
        if (response.isSuccessful()) {
            mToControllerProvider.showOnMap(response.body());
        } else {
            try {
                Log.wtf(TAG, "Response unsuccessful");
                Log.wtf(TAG, response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<VeturiloQueryResult> call, Throwable t) {
        Log.getStackTraceString(t);
    }

    public void getVeturiloStations() {
        for (String network_id : NETWORK_IDS) {
            Call<VeturiloQueryResult> call = mVeturiloAPI.getVeturiloStations(network_id);
            call.enqueue(this);
        }
    }
}
