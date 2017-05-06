
package pl.kuc_industries.warsawnavihelper.StopsWithLocations;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StopsWithLocations {

    @SerializedName("stops")
    @Expose
    private List<Stop> stops = null;

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

}
