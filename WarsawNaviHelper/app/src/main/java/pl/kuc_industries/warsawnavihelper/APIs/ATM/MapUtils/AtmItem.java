package pl.kuc_industries.warsawnavihelper.APIs.ATM.MapUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class AtmItem implements ClusterItem {
    private final String mTitle;
    private final LatLng mPosition;
    private final String isOpen;

    public AtmItem(String title, LatLng position, String opened) {
        mTitle = title;
        mPosition = position;
        isOpen = opened;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return isOpen;
    }
}
