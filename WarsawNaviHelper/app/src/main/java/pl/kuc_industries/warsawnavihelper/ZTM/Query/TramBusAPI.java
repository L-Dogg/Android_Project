package pl.kuc_industries.warsawnavihelper.ZTM.Query;

import pl.kuc_industries.warsawnavihelper.ZTM.QueryResult.TramBusQueryResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TramBusAPI {
    @GET("busestrams_get/")
    Call<TramBusQueryResult> getTrams(
            @Query("resource_id") String resource_id,
            @Query("api_key") String api_key,
            @Query("type") int type,
            @Query("line") int line,
            @Query("brigade") int brigade);
}
