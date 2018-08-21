package hu.zsoltborza.gymfinderhun.activities;

import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import hu.zsoltborza.gymfinderhun.model.GymMarker;
import hu.zsoltborza.gymfinderhun.utils.Utils;
import hu.zsoltborza.gymfinderhun.model.GymListItem;
import hu.zsoltborza.gymfinderhun.network.RetrofitServiceFactory;
import hu.zsoltborza.gymfinderhun.network.service.GymSearchService;
import hu.zsoltborza.gymfinderhun.network.domain.MarkerResult;
import hu.zsoltborza.gymfinderhun.network.domain.MarkerSearch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zsolt Borza on 2018.01.30..
 */

public class MapActivity extends BaseMapActivity implements ClusterManager.OnClusterItemClickListener<GymMarker> {

    private ClusterManager<GymMarker> mClusterManager;

    LatLng currentLocation = new LatLng(47.548, 19.0719793);

    List<GymMarker> gymsList = new ArrayList<>();

    // offline list
    private List<GymListItem> gymListNew;


    @Override
    protected void start() {

        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.548, 19.0719793), 13));

        mClusterManager = new ClusterManager<GymMarker>(this, getMap());
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterItemClickListener(this);

        gymListNew = Utils.getDataFromFile(MapActivity.this);


//        getMarkers();

//        getCurrentGyms();

        offlineMarkers();

    }

    public synchronized void offlineMarkers(){



        for (int i = 0; i < gymListNew.size(); i++) {

            String title = gymListNew.get(i).getTitle();
            double lat = Double.parseDouble(gymListNew.get(i).getLatitude().replace(",","."));
            double lon = Double.parseDouble(gymListNew.get(i).getLongitude().replace(",","."));

            gymsList.add(new GymMarker(new LatLng(lat, lon), title));


        }

        mClusterManager.addItems(gymsList);
    }

    public synchronized void getMarkers() {

        Call<MarkerSearch> call;
        GymSearchService apiService =
                RetrofitServiceFactory.getClient().create(GymSearchService.class);

        call = apiService.getGymMarkers();
        call.enqueue(new Callback<MarkerSearch>() {


            @Override
            public void onResponse(Call<MarkerSearch> call, Response<MarkerSearch> response) {

//                response.body().getResults();
                List<MarkerResult> result = response.body().getResults();
                for (int i = 0; i < result.size(); i++) {

                    double lat = result.get(i).getGeometry().getLocation().getLat();
                    double lon = result.get(i).getGeometry().getLocation().getLng();
                    String name = result.get(i).getName();
                    gymsList.add(new GymMarker(new LatLng(lat, lon), name));
//                    result.get(i).getRating();

                }

                mClusterManager.addItems(gymsList);

            }

            @Override
            public void onFailure(Call<MarkerSearch> call, Throwable t) {

            }
        });

    }


    public synchronized void getCurrentGyms(double lat, double lon) {

        Call<MarkerSearch> call;
        GymSearchService apiService =
                RetrofitServiceFactory.getClient().create(GymSearchService.class);

       // call = apiService.getGymsByLocation("47.548, 19.0719793",5000);
        call = apiService.getGymsByLocation(String.valueOf(lat) + ", " +String.valueOf(lon),5000);
        call.enqueue(new Callback<MarkerSearch>() {


            @Override
            public void onResponse(Call<MarkerSearch> call, Response<MarkerSearch> response) {

                List<MarkerResult> result = response.body().getResults();
                for (int i = 0; i < result.size(); i++) {

                    double lat = result.get(i).getGeometry().getLocation().getLat();
                    double lon = result.get(i).getGeometry().getLocation().getLng();
                    String name = result.get(i).getName();
                    gymsList.add(new GymMarker(new LatLng(lat, lon), name));
//                    result.get(i).getRating();

                }

                mClusterManager.addItems(gymsList);

            }

            @Override
            public void onFailure(Call<MarkerSearch> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onClusterItemClick(GymMarker gymMarker) {
        Toast.makeText(this, gymMarker.getTitle(), Toast.LENGTH_SHORT).show();
        return false;
    }


//    @Override
//    public boolean onMarkerClick(Marker marker) {
//        Toast.makeText(this,marker.getTitle(),Toast.LENGTH_SHORT).show();
//        return false;
//    }
}
