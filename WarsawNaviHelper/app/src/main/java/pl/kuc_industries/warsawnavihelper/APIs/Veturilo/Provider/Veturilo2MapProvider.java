package pl.kuc_industries.warsawnavihelper.APIs.Veturilo.Provider;

import android.location.Location;

import com.google.maps.android.clustering.ClusterManager;

import pl.kuc_industries.warsawnavihelper.APIs.Veturilo.MapUtils.VeturiloItem;
import pl.kuc_industries.warsawnavihelper.APIs.Veturilo.QueryResult.Network;
import pl.kuc_industries.warsawnavihelper.APIs.Veturilo.QueryResult.Station;
import pl.kuc_industries.warsawnavihelper.APIs.Veturilo.QueryResult.VeturiloQueryResult;
import pl.kuc_industries.warsawnavihelper.APIs.Veturilo.VeturiloController;

/**
 * Created by mateusz on 5/6/17.
 */

public class Veturilo2MapProvider implements IVeturilo2ViewProvider, IVeturilo2ControllerProvider {

    public static final String TAG = "Veturilo2MapProvider";
    private static final int MAX_DISTANCE = 2000; // in metres

    private VeturiloController mVeturiloController;
    private ClusterManager<VeturiloItem> mClusterManager;
    private boolean mEmptyStationsVisible;
    private boolean mStationsVisible;
    private Location mLocation;

    public void setLocation(Location location) { mLocation = location; }

    public Veturilo2MapProvider(ClusterManager<VeturiloItem> clusterManager, Location location) {
        this.mClusterManager = clusterManager;
        mVeturiloController = new VeturiloController(this);
        mVeturiloController.start();
        mStationsVisible = mEmptyStationsVisible = false;
        mLocation = location;
    }

    @Override
    public void getStations() {
        mVeturiloController.getVeturiloStations();
    }

    @Override
    public void updateStationsOnView() {
        mClusterManager.clearItems();
        getStations();
    }

    @Override
    public void removeStationsFromView() {
        mClusterManager.clearItems();
        mClusterManager.cluster();
    }

    @Override
    public void showOnMap(VeturiloQueryResult veturiloStations) {
        if (!mStationsVisible) {
            return;
        }

        Network currentNetwork = veturiloStations.getNetwork();
        if (currentNetwork != null) {
            for (Station station : currentNetwork.getStations()) {

                if (distance(station.getLatitude(), mLocation.getLatitude(),
                        station.getLongitude(), mLocation.getLongitude(), 0, 0) > MAX_DISTANCE)
                        continue;

                VeturiloItem item = new VeturiloItem(station.getName(),
                        station.getLatitude(),
                        station.getLongitude(),
                        station.getFreeBikes(),
                        station.getFreeBikes() + station.getEmptySlots());
                if (mEmptyStationsVisible || station.getFreeBikes() > 0) {
                    mClusterManager.addItem(item);
                }
            }
        }
        mClusterManager.cluster();
    }

    public boolean areEmptyStationsVisible() {
        return mEmptyStationsVisible;
    }

    public void setEmptyStationsVisible(boolean emptyStationsVisible) {
        this.mEmptyStationsVisible = emptyStationsVisible;
        this.updateStationsOnView();
    }

    public void setStationsVisible(boolean stationsVisible) {
        this.mStationsVisible = stationsVisible;
        if (mStationsVisible) {
            this.updateStationsOnView();
        } else {
            this.removeStationsFromView();
        }
    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    private static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
