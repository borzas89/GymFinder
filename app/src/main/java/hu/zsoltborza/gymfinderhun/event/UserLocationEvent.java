package hu.zsoltborza.gymfinderhun.event;

import com.google.android.gms.maps.model.LatLng;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Borzas on 2018. 08. 13..
 * Object which has the users current location.
 */

public class UserLocationEvent {

    private LatLng userLocation;

    public LatLng getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(LatLng userLocation) {
        this.userLocation = userLocation;
    }

}
