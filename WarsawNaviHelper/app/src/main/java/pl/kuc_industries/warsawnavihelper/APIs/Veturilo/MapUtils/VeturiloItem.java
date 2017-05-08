package pl.kuc_industries.warsawnavihelper.APIs.Veturilo.MapUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by mateusz on 5/6/17.
 */

public class VeturiloItem implements ClusterItem {

    private String mStationName;
    private LatLng mPosition;
    private int mFreeBikesNumber;
    private int mSlotsNumber;

    public VeturiloItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public VeturiloItem(String stationName, double lat, double lng, int freeBikesNumber, int slotsNumber) {
        this.mStationName = stationName;
        this.mPosition = new LatLng(lat, lng);
        this.mFreeBikesNumber = freeBikesNumber;
        this.mSlotsNumber = slotsNumber;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mStationName;
    }

    @Override
    public String getSnippet() {
        return "Bikes: " + mFreeBikesNumber + "/" + mSlotsNumber;
    }

    public String getStationName() {
        return mStationName;
    }

    public int getFreeBikes() {
        return mFreeBikesNumber;
    }
}
