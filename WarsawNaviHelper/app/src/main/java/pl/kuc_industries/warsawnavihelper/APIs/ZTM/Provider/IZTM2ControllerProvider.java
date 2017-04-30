package pl.kuc_industries.warsawnavihelper.APIs.ZTM.Provider;

import pl.kuc_industries.warsawnavihelper.APIs.ZTM.QueryResult.TramBus;

public interface IZTM2ControllerProvider {
    void showOnMap(TramBus result);
}
