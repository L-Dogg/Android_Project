package pl.kuc_industries.warsawnavihelper.APIs.ATM.Query;

import pl.kuc_industries.warsawnavihelper.APIs.ATM.QueryResult.ATMQueryResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IAtmAPI {
    @GET("nearbysearch/{output}")
    Call<ATMQueryResult> getNearbyATMs(
            @Path("output") String output,
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key);

    @GET("nearbysearch/{output}")
    Call<ATMQueryResult> getNearbyFilteredATMs(
            @Path("output") String output,
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("keyword") String keyword,
            @Query("type") String type,
            @Query("key") String key);
}
