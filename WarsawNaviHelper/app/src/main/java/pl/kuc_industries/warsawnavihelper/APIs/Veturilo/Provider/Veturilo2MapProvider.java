package pl.kuc_industries.warsawnavihelper.APIs.Veturilo.Provider;

import android.util.Log;

import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

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

    private VeturiloController mVeturiloController;
    private ClusterManager<VeturiloItem> mClusterManager;

    public Veturilo2MapProvider(ClusterManager<VeturiloItem> clusterManager) {
        this.mClusterManager = clusterManager;
        mVeturiloController = new VeturiloController(this);
        mVeturiloController.start();
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
        Network currentNetwork = veturiloStations.getNetwork();
        if (currentNetwork != null) {
            for (Station station : currentNetwork.getStations()) {
                VeturiloItem item = new VeturiloItem(station.getName(),
                        station.getLatitude(),
                        station.getLongitude(),
                        station.getFreeBikes(),
                        station.getFreeBikes() + station.getEmptySlots());
                mClusterManager.addItem(item);
            }
        }
    }
}
