package pl.kuc_industries.warsawnavihelper.APIs.Veturilo.MapUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by mateusz on 5/6/17.
 */

public class VeturiloItem implements ClusterItem {

    private LatLng mPosition;
    private String mTitle;
    private String mSnippet;


    public VeturiloItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public VeturiloItem(double lat, double lng, String mTitle, String mSnippet) {
        mPosition = new LatLng(lat, lng);
        this.mTitle = mTitle;
        this.mSnippet = mSnippet;
    }

    @Override
    public LatLng getPosition() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
