package pl.kuc_industries.warsawnavihelper.APIs.ZTM.Query;

import pl.kuc_industries.warsawnavihelper.APIs.ZTM.QueryResult.TramBus;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ITramBusAPI {
    @GET("busestrams_get/")
    Call<TramBus> getTrams(
            @Query("resource_id") String resource_id,
            @Query("apikey") String apikey,
            @Query("type") int type,
            @Query("line") String line,
            @Query("brigade") Integer brigade);
}
