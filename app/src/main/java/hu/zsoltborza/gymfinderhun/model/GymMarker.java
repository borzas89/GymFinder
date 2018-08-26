package hu.zsoltborza.gymfinderhun.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Zsolt Borza on 2018.01.30..
 */

public class GymMarker implements ClusterItem {

    private final LatLng mPosition;
    private String mTitle;
    private String mAddress;

    public GymMarker(LatLng mPosition, String mTitle) {
        this.mPosition = mPosition;
        this.mTitle = mTitle;
    }

    public GymMarker(double lat, double lng, String title) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
    }

    public GymMarker(LatLng mPosition, String mTitle, String mAddress) {
        this.mPosition = mPosition;
        this.mTitle = mTitle;
        this.mAddress = mAddress;
    }



    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }


    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }


}
