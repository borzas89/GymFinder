package hu.zsoltborza.gymfinderhun.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import hu.zsoltborza.gymfinderhun.model.GymMarker;
import hu.zsoltborza.gymfinderhun.R;
import hu.zsoltborza.gymfinderhun.network.service.GooglePlacesService;
import hu.zsoltborza.gymfinderhun.network.RetrofitServiceFactory;
import hu.zsoltborza.gymfinderhun.network.domain.MarkerResult;
import hu.zsoltborza.gymfinderhun.network.domain.MarkerSearch;
import hu.zsoltborza.gymfinderhun.utils.CustomUrlTileProvider;
import hu.zsoltborza.gymfinderhun.model.GymListItem;
import hu.zsoltborza.gymfinderhun.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Zsolt Borza on 2018.01.31..
 */

public class GymMapFragment extends Fragment implements OnMapReadyCallback,
        ClusterManager.OnClusterItemClickListener<GymMarker> {


    private GoogleMap mMap;
    private ClusterManager<GymMarker> mClusterManager;

    private BottomSheetBehavior mBottomSheetBehavior;

    private LatLng currentLocation;


    private List<GymMarker> gymsList = new ArrayList<>();

    // offline list
    private List<GymListItem> gymListNew;

    private DecimalFormat df = new DecimalFormat("#.00");

    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    private class MarkerRenderer extends DefaultClusterRenderer<GymMarker> {

        private final IconGenerator mIconGenerator = new IconGenerator(getActivity().getApplicationContext());

        public MarkerRenderer(Context context, GoogleMap map, ClusterManager<GymMarker> clusterManager) {
            super(context, map, clusterManager);
        }


        public MarkerRenderer() {
            super(getActivity().getApplicationContext(), getMap(), mClusterManager);


        }

        @Override
        protected void onBeforeClusterItemRendered(GymMarker item, MarkerOptions markerOptions) {
            super.onBeforeClusterItemRendered(item, markerOptions);

            Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(),
                    R.drawable.marker_green);
            BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(icon);
            markerOptions.icon(descriptor);


        }

        @Override
        protected void onBeforeClusterRendered(Cluster<GymMarker> cluster, MarkerOptions markerOptions) {
            super.onBeforeClusterRendered(cluster, markerOptions);

            // cluster....
//            Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(),
//                    R.drawable.marker);
//            BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(icon);
//            markerOptions.icon(descriptor);

        }
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View rootView = inflater.inflate(R.layout.map, parent, false);
//        ButterKnife.bind(this, rootView);

        //Find bottom Sheet ID
        View bottomSheet = rootView.findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        //By default set BottomSheet Behavior as Collapsed and Height 0
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setPeekHeight(0);


        //If you want to handle callback of Sheet Behavior you can use below code
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        return rootView;
    }


    public void openBottomSheetDialog(String title,String address, String distace) {
        MarkerBottomSheetDialogFragment markerBottomSheetDialogFragment = new MarkerBottomSheetDialogFragment();
        markerBottomSheetDialogFragment.newInstance(title,address,distace);
        markerBottomSheetDialogFragment.setTitle(title);
        markerBottomSheetDialogFragment.setAddress(address);
        markerBottomSheetDialogFragment.setDistance(distace);
        markerBottomSheetDialogFragment.show(getFragmentManager(), "marker dialog");
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Bundle args = getArguments();
        if (args != null) {
            double lat = args.getDouble("lat");
            double lon = args.getDouble("lon");
            currentLocation = new LatLng(lat, lon);
        } else {
            currentLocation = new LatLng(47.4544331, 19.633235);
        }


        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (mMap != null) {
            return;
        }
        mMap = googleMap;

        initMap();

    }

    private GoogleMap getMap() {
        return mMap;
    }

    private synchronized void initMap() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.latitude, currentLocation.longitude), 13));
        mClusterManager = new ClusterManager<GymMarker>(getContext(), getMap());
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        mClusterManager.setRenderer(new MarkerRenderer());
        mClusterManager.setOnClusterItemClickListener(this);

        // TODO check permissions...
        if (ActivityCompat.checkSelfPermission(getContext()
                , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setMyLocationButtonEnabled(true);

//        custom tile
        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
//        String overlayString = "http://tile.stamen.com/watercolor/{z}/{x}/{y}.jpg";
        String overlayString = "http://a.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png";
//                String overlayString = "http://tile.stamen.com/terrain/{z}/{x}/{y}.jpg";
//        String overlayString = "http://tile.stamen.com/toner/{z}/{x}/{y}.png";
        CustomUrlTileProvider mTileProvider = new CustomUrlTileProvider(
               256,
                256, overlayString);
//        TileOverlayOptions mSelectedTileOverlay = new TileOverlayOptions();
                mMap.addTileOverlay(
                new TileOverlayOptions().tileProvider(mTileProvider)
                        .zIndex(-1));

        gymListNew = Utils.getDataFromFile(getContext());

       // mClusterManager.setOnClusterItemInfoWindowClickListener(GymMapFragment.this);
        mMap.setOnInfoWindowClickListener(mClusterManager);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // getOnlineMarkersByLocation(currentLocation.latitude,currentLocation.longitude,5000);
                        offlineMarkers();
                    }
                });
            }
        });
        thread.start();


    }


    public synchronized void getOnlineMarkers() {

        Call<MarkerSearch> call;
        GooglePlacesService apiService =
                RetrofitServiceFactory.getClient().create(GooglePlacesService.class);

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

    public synchronized void getOnlineMarkersByLocation(double lat, double lon, int radius) {

        String location = String.valueOf(lat) + ", " + String.valueOf(lon);
        Call<MarkerSearch> call;
        GooglePlacesService apiService =
                RetrofitServiceFactory.getClient().create(GooglePlacesService.class);

        call = apiService.getGymsByLocation(location, radius);
        call.enqueue(new Callback<MarkerSearch>() {


            @Override
            public void onResponse(Call<MarkerSearch> call, Response<MarkerSearch> response) {

                List<MarkerResult> result = response.body().getResults();
                for (int i = 0; i < result.size(); i++) {

                    double lat = result.get(i).getGeometry().getLocation().getLat();
                    double lon = result.get(i).getGeometry().getLocation().getLng();
                    String name = result.get(i).getName();
                    gymsList.add(new GymMarker(new LatLng(lat, lon), name));


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

        LatLng gymPosition = gymMarker.getPosition();
        String distance = df.format(SphericalUtil.computeDistanceBetween(currentLocation,gymPosition)/1000) + " km-re";
        openBottomSheetDialog(gymMarker.getTitle(),gymMarker.getAddress(),distance);

       // Toast.makeText(getContext(), gymMarker.getTitle(), Toast.LENGTH_SHORT).show();

        return false;
    }

    /**
     * Offline markers from file, needs Utils.getDataFromFile(getContext())...
     */
    public synchronized void offlineMarkers(){

        for (int i = 0; i < gymListNew.size(); i++) {

            String title = gymListNew.get(i).getTitle();
            double lat = Double.parseDouble(gymListNew.get(i).getLatitude().replace(",","."));
            double lon = Double.parseDouble(gymListNew.get(i).getLongitude().replace(",","."));
            String address = gymListNew.get(i).getAddress();

            gymsList.add(new GymMarker(new LatLng(lat, lon), title,address));


        }

        mClusterManager.addItems(gymsList);
        mClusterManager.cluster();
    }

    /*@Override
    public void onClusterItemInfoWindowClick(GymMarker gymMarker) {
        LatLng gymPosition = gymMarker.getPosition();
        String distance = df.format(SphericalUtil.computeDistanceBetween(currentLocation,gymPosition)/1000) + " km-re";
        openBottomSheetDialog(gymMarker.getTitle(),gymMarker.getAddress(),distance);
    }*/



}
