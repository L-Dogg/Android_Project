package pl.kuc_industries.warsawnavihelper.APIs.ZTM;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pl.kuc_industries.warsawnavihelper.Constants;
import pl.kuc_industries.warsawnavihelper.APIs.ZTM.Provider.IZTM2ControllerProvider;
import pl.kuc_industries.warsawnavihelper.APIs.ZTM.Query.TramBusAPI;
import pl.kuc_industries.warsawnavihelper.APIs.ZTM.QueryResult.TramBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ZTMController implements Callback<TramBus> {
    private static final String TAG = "ZTMController";

    private static final String BASE_URL = "https://api.um.warszawa.pl/api/action/";
    private static final String RESOURCE_ID = "f2e5503e-927d-4ad3-9500-4ab9e55deb59";

    private TramBusAPI tramBusAPI;
    private IZTM2ControllerProvider provider;

    public ZTMController(IZTM2ControllerProvider provider) {
        this.provider = provider;
    }

    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        tramBusAPI = retrofit.create(TramBusAPI.class);
    }

    @Override
    public void onResponse(Call<TramBus> call, Response<TramBus> response) {
        if(response.isSuccessful()) {
            provider.showOnMap(response.body());
        } else {
            Log.wtf(TAG, response.errorBody().toString());
        }
    }

    @Override
    public void onFailure(Call<TramBus> call, Throwable t) {
        t.printStackTrace();
        Log.wtf(TAG, t.getStackTrace().toString());
    }

    public void getTrams(String lineNumber) {
        Call<TramBus> call = tramBusAPI.getTrams(
                RESOURCE_ID,
                Constants.UM_API_KEY,
                ZTMVehicleType.Tram.getValue(),
                lineNumber,
                null);
        call.enqueue(this);
    }

    public void getBuses(String lineNumber) {
        Call<TramBus> call = tramBusAPI.getTrams(
                RESOURCE_ID,
                Constants.UM_API_KEY,
                ZTMVehicleType.Bus.getValue(),
                lineNumber,
                null);
        call.enqueue(this);
    }
}
