package pl.kuc_industries.warsawnavihelper.ZTM.Query;

import pl.kuc_industries.warsawnavihelper.ZTM.QueryResult.TramBus;
import pl.kuc_industries.warsawnavihelper.ZTM.ZTMVehicleType;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TramBusAPI {
    @GET("busestrams_get/")
    Call<TramBus> getTrams(
            @Query("resource_id") String resource_id,
            @Query("apikey") String apikey,
            @Query("type") int type,
            @Query("line") String line,
            @Query("brigade") Integer brigade);
}
