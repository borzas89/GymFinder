package hu.zsoltborza.gymfinderhun.fragments.base;

/**
 * Created by Borzas on 2018. 08. 12..
 */

public interface LocationInterface {

    public void startLocationClick();
    public void initLocationServices();
    public void startLocationUpdates();
    public void updateLocationUI();
    public void openSettings();
}
