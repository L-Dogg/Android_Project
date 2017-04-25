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

import pl.kuc_industries.warsawnavihelper.R;
import pl.kuc_industries.warsawnavihelper.ZTM.QueryResult.TramBus;
import pl.kuc_industries.warsawnavihelper.ZTM.QueryResult.TramBusQueryResult;
import pl.kuc_industries.warsawnavihelper.ZTM.ZTMController;

public class ZTM2MapProvider implements ZTM2ControllerProvider, ZTM2ViewProvider {
    private final static String TAG = "ZTM2MapProvider";

    private ZTMController ztmController;
    private GoogleMap map;

    public ZTM2MapProvider(GoogleMap map) {
        this.map = map;
        this.ztmController = new ZTMController(this);
        this.ztmController.start();
    }

    public void getTrams(int lineNumber) {
        ztmController.getTrams(lineNumber);
    }

    public void getBuses(int lineNumber) {
        ztmController.getBuses(lineNumber);
    }

    public void showOnMap(TramBus result) {
        Log.wtf(TAG, String.valueOf(result.getResult().size()));
        for (TramBusQueryResult vehicle : result.getResult()) {
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(vehicle.getLat(), vehicle.getLon()))
                    .icon(BitmapDescriptorFactory.fromBitmap(prepareMarkerBitmap(vehicle.getLines())))
                    .anchor(0.5f, 1)
            );
        }
    }

    private Bitmap prepareMarkerBitmap(String lineNumber) {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(120, 120, conf);
        Canvas canvas1 = new Canvas(bmp);

        // paint defines the text color, stroke width and size
        Paint color = new Paint();
        color.setTextSize(50);
        color.setColor(Color.RED);

        canvas1.drawText(lineNumber, 30, 40, color);

        return bmp;
    }
}
