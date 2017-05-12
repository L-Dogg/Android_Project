package pl.kuc_industries.warsawnavihelper.APIs.ZTM;

import android.util.Log;

import java.util.TimerTask;

import pl.kuc_industries.warsawnavihelper.APIs.ZTM.Provider.IZTM2ViewProvider;


public class TramAndBusMapUpdater extends TimerTask {
    private IZTM2ViewProvider mProvider;
    private final static String TAG = "TramAndBusMapUpdater";
    public TramAndBusMapUpdater(IZTM2ViewProvider provider) { mProvider = provider; }

    @Override
    public void run() {
        if (mProvider.getLinesVisibleOnMapCount() > 0 && mProvider.getVisibility()) {
            Log.wtf(TAG, "Update triggered!");
            mProvider.updateVehiclesPositionsOnMap();
        }
    }
}
