package pl.kuc_industries.warsawnavihelper.APIs.ATM.Provider;

import android.location.Location;

public interface IATM2ViewProvider {
    void getATMs(Location location, int radius);
    void getFileredATMs(Location location, int radius, String filter);
    void removeATMsFromMap();
}
