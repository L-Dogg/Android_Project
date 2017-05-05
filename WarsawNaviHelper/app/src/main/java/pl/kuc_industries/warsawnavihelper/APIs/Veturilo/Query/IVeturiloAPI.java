package pl.kuc_industries.warsawnavihelper.APIs.Veturilo.Query;

import pl.kuc_industries.warsawnavihelper.APIs.Veturilo.QueryResult.VeturiloQueryResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by mateusz on 5/5/17.
 */

public interface IVeturiloAPI {
    @GET("networks/{network_id}")
    Call<VeturiloQueryResult> getVeturiloStations(
            @Path("network_id") String network_id
    );
}
