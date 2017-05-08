package pl.kuc_industries.warsawnavihelper.APIs.ZTM.Provider;

import android.util.Log;

import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.kuc_industries.warsawnavihelper.APIs.ZTM.MapUtils.VehicleItem;
import pl.kuc_industries.warsawnavihelper.APIs.ZTM.MapUtils.VehicleType;
import pl.kuc_industries.warsawnavihelper.APIs.ZTM.QueryResult.TramBus;
import pl.kuc_industries.warsawnavihelper.APIs.ZTM.QueryResult.TramBusQueryResult;
import pl.kuc_industries.warsawnavihelper.APIs.ZTM.ZTMController;

public class ZTM2MapProvider implements IZTM2ControllerProvider, IZTM2ViewProvider {
    private final static String TAG = "ZTM2MapProvider";

    private ZTMController ztmController;
    private ClusterManager<VehicleItem> mClusterManager;
    private VehicleType currentItemType;
    private List<VehicleItem> mVehiclesVisibleOnMap;

    public ZTM2MapProvider(ClusterManager<VehicleItem> cluster) {
        this.mClusterManager = cluster;
        this.ztmController = new ZTMController(this);
        mVehiclesVisibleOnMap = new ArrayList<>();
        this.ztmController.start();
    }

    @Override
    public void getTrams(String lineNumber) {
        ztmController.getTrams(lineNumber);
        currentItemType = VehicleType.Tram;
    }

    @Override
    public void getBuses(String lineNumber) {
        ztmController.getBuses(lineNumber);
        currentItemType = VehicleType.Bus;
    }

    //TODO: find a cleaner way to achieve the same effect:
    @Override
    public void removeVehiclesFromMap(final String lineNumber) {
        mClusterManager.clearItems();

        List<VehicleItem> currentItems = new LinkedList<>();
        for (VehicleItem vehicleItem : mVehiclesVisibleOnMap) {
            if (!vehicleItem.getTitle().equals(lineNumber))
                currentItems.add(vehicleItem);
        }

        mVehiclesVisibleOnMap = currentItems;
        mClusterManager.addItems(mVehiclesVisibleOnMap);
    }

    @Override
    public void updateVehiclesPositionsOnMap() {
        mClusterManager.clearItems();
        List<VehicleItem> tmp = new LinkedList<>(mVehiclesVisibleOnMap);
        mVehiclesVisibleOnMap.clear();

        for (VehicleItem vehicleItem : tmp) {
            currentItemType = vehicleItem.getVehicleType();
            if (currentItemType == VehicleType.Tram)
                ztmController.getTrams(vehicleItem.getTitle());
            else
                ztmController.getBuses(vehicleItem.getTitle());
        }
    }

    @Override
    public void showOnMap(TramBus result) {
        Log.wtf(TAG, "showOnMap, adding results: " + result.getResult().size());
        // todo: this is a hotfix, refactor later
        int idx = 0;
        for (TramBusQueryResult vehicle : result.getResult()) {
            VehicleItem vehicleItem= new VehicleItem(vehicle.getLat(),
                    vehicle.getLon(),
                    vehicle.getLines(),
                    "",
                    currentItemType);
            mClusterManager.addItem(vehicleItem);
            if (idx == 0)
                mVehiclesVisibleOnMap.add(vehicleItem);
            idx++;
        }
    }

    @Override
    public int getLinesVisibleOnMapCount() { return mVehiclesVisibleOnMap.size(); }

    @Override
    public void showAll() {
        updateVehiclesPositionsOnMap();
    }

    @Override
    public void hideAll() {
        mClusterManager.clearItems();
    }

}
