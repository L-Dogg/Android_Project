package pl.kuc_industries.warsawnavihelper.APIs.AirPollution.Query;

import pl.kuc_industries.warsawnavihelper.APIs.AirPollution.QueryResult.AirPollution;
import pl.kuc_industries.warsawnavihelper.APIs.ZTM.QueryResult.TramBus;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IAirPollutionAPI {
    @GET("aqindex/getIndex/{id}")
    Call<AirPollution> getAirPollutionData(@Path("id") int id);
}
