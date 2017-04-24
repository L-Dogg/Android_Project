package pl.kuc_industries.warsawnavihelper.ZTM;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pl.kuc_industries.warsawnavihelper.Constants;
import pl.kuc_industries.warsawnavihelper.ZTM.Query.TramBusAPI;
import pl.kuc_industries.warsawnavihelper.ZTM.QueryResult.TramBusQueryResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ZTMController implements Callback<TramBusQueryResult> {
    private static final String TAG = "ZTMController";

    private static final String BASE_URL = "https://api.um.warszawa.pl/api/action/";
    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        TramBusAPI tramBusAPI = retrofit.create(TramBusAPI.class);

        Call<TramBusQueryResult> call = tramBusAPI.getTrams(
                "resource_id=f2e5503e-927d-4ad3-9500-4ab9e55deb59",
                Constants.UM_API_KEY,
                1,
                208,
                1);
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<TramBusQueryResult> call, Response<TramBusQueryResult> response) {
        if(response.isSuccessful()) {
            TramBusQueryResult result = response.body();
            Log.wtf(TAG, "LAT: " + result.getLat() + " LON" + result.getLon());
            Log.wtf(TAG, "Time: " + result.getTime());
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<TramBusQueryResult> call, Throwable t) {
        t.printStackTrace();
        Log.wtf(TAG, t.getStackTrace().toString());
    }
}
