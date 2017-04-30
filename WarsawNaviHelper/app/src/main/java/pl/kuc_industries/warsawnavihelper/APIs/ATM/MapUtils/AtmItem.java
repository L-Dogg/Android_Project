package pl.kuc_industries.warsawnavihelper.APIs.ATM.MapUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import pl.kuc_industries.warsawnavihelper.APIs.ATM.QueryResult.Location;

public class AtmItem implements ClusterItem {
    private final String mTitle;
    private final LatLng mPosition;
    private final String mBankName;

    public AtmItem(String title, LatLng position, String bankName) {
        mTitle = title;
        mPosition = position;
        mBankName = bankName;
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
        return mBankName;
    }
}
