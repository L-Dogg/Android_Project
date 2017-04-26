package pl.kuc_industries.warsawnavihelper.ZTM.Provider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.kuc_industries.warsawnavihelper.R;
import pl.kuc_industries.warsawnavihelper.ZTM.MapUtils.VehicleItem;
import pl.kuc_industries.warsawnavihelper.ZTM.MapUtils.VehicleType;
import pl.kuc_industries.warsawnavihelper.ZTM.QueryResult.TramBus;
import pl.kuc_industries.warsawnavihelper.ZTM.QueryResult.TramBusQueryResult;
import pl.kuc_industries.warsawnavihelper.ZTM.ZTMController;
import pl.kuc_industries.warsawnavihelper.adapter.ItemTypes;

public class ZTM2MapProvider implements ZTM2ControllerProvider, ZTM2ViewProvider {
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
    public void getTrams(int lineNumber) {
        ztmController.getTrams(lineNumber);
        currentItemType = VehicleType.Tram;
    }

    @Override
    public void getBuses(int lineNumber) {
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

    public void showOnMap(TramBus result) {
        Log.wtf(TAG, String.valueOf(result.getResult().size()));
        for (TramBusQueryResult vehicle : result.getResult()) {
            VehicleItem vehicleItem= new VehicleItem(vehicle.getLat(),
                    vehicle.getLon(),
                    vehicle.getLines(),
                    "",
                    currentItemType);
            mClusterManager.addItem(vehicleItem);
            mVehiclesVisibleOnMap.add(vehicleItem);
        }
    }
}
