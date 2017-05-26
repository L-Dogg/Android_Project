package pl.kuc_industries.warsawnavihelper.APIs.ATM.Provider;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import pl.kuc_industries.warsawnavihelper.APIs.ATM.ATMController;
import pl.kuc_industries.warsawnavihelper.APIs.ATM.MapUtils.AtmItem;
import pl.kuc_industries.warsawnavihelper.APIs.ATM.QueryResult.ATM;

public class ATM2MapProvider implements IATM2ControllerProvider, IATM2ViewProvider {
    private final static String TAG = "ATM2MapProvider";
    private final ClusterManager<AtmItem> mClusterManager;
    private final ATMController mAtmController;

    public ATM2MapProvider(ClusterManager<AtmItem> clusterManager) {
        mClusterManager = clusterManager;
        mAtmController = new ATMController(this);
        mAtmController.start();
    }

    @Override
    public void getATMs(Location location, int radius) {
        mAtmController.getATMs(location, radius);
    }

    @Override
    public void getFilteredATMs(Location location, int radius, String filter) {
        mAtmController.getFilteredATMs(location, radius, filter);
    }

    @Override
    public void removeATMsFromMap() {
        mClusterManager.clearItems();
        mClusterManager.cluster();
    }

    @Override
    public void ShowOnMap(List<ATM> atmList) {
        for (ATM atm : atmList) {
            pl.kuc_industries.warsawnavihelper.APIs.ATM.QueryResult.Location
                    loc = atm.getGeometry().getLocation();
            String snippet = atm.getOpeningHours() != null
                    ? atm.getOpeningHours().getOpenNow() ? "Open" : "Closed"
                    : "Unknown";
            mClusterManager.addItem(
                    new AtmItem(atm.getName(), new LatLng(loc.getLat(), loc.getLng()), snippet)
            );
        }
        mClusterManager.cluster();
    }
}
