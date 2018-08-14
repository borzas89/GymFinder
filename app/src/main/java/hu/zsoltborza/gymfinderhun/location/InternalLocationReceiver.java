package hu.zsoltborza.gymfinderhun.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

import hu.zsoltborza.gymfinderhun.activities.MainActivity;

 /*
 * Internal receiver used to get location updates for this activity.
 *
 */
public class InternalLocationReceiver extends BroadcastReceiver {

    private static final String TAG = "Location";

     public InternalLocationReceiver() {

     }

     private MainActivity mActivity;
    public InternalLocationReceiver(MainActivity activity){
        mActivity = activity;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        final MainActivity activity = mActivity;
        if(activity != null) {
            LocationResult result = intent.getParcelableExtra("result");
            double lat = result.getLastLocation().getLatitude();
            double lon = result.getLastLocation().getLongitude();
            Log.d(TAG,"Lat: " + lat + " , " + "Lon: " + lon);
            //Handle location update here
        }
    }
}

