package pl.kuc_industries.warsawnavihelper.ZTM;

import java.util.TimerTask;

import pl.kuc_industries.warsawnavihelper.ZTM.Provider.IZTM2ViewProvider;


public class TramAndBusMapUpdater extends TimerTask {
    private IZTM2ViewProvider mProvider;

    public TramAndBusMapUpdater(IZTM2ViewProvider provider) { mProvider = provider; }

    @Override
    public void run() {
        if (mProvider.getLinesVisibleOnMapCount() > 0)
            mProvider.updateVehiclesPositionsOnMap();
    }
}
