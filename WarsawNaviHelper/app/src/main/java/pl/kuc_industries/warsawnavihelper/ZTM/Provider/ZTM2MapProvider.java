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

    public ZTM2MapProvider(ClusterManager<VehicleItem> cluster) {
        this.mClusterManager = cluster;
        this.ztmController = new ZTMController(this);
        this.ztmController.start();
    }

    public void getTrams(int lineNumber) {
        ztmController.getTrams(lineNumber);
        currentItemType = VehicleType.Tram;
    }

    public void getBuses(int lineNumber) {
        ztmController.getBuses(lineNumber);
        currentItemType = VehicleType.Bus;
    }

    public void showOnMap(TramBus result) {
        mClusterManager.clearItems();
        Log.wtf(TAG, String.valueOf(result.getResult().size()));
        for (TramBusQueryResult vehicle : result.getResult()) {
            mClusterManager.addItem(new VehicleItem(vehicle.getLat(),
                    vehicle.getLon(),
                    vehicle.getLines(),
                    "",
                    currentItemType));
        }

    }
}
