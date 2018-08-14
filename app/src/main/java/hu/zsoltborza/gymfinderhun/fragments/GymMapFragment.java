package hu.zsoltborza.gymfinderhun.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.zsoltborza.gymfinderhun.adapter.GymAdapter;
import hu.zsoltborza.gymfinderhun.model.GymMarker;
import hu.zsoltborza.gymfinderhun.R;
import hu.zsoltborza.gymfinderhun.utils.Utils;
import hu.zsoltborza.gymfinderhun.utils.CustomUrlTileProvider;
import hu.zsoltborza.gymfinderhun.model.GymListItem;


/**
 * Created by Zsolt Borza on 2018.01.31..
 */

public class GymMapFragment extends Fragment implements OnMapReadyCallback ,
        ClusterManager.OnClusterItemClickListener<GymMarker>, ClusterManager.OnClusterItemInfoWindowClickListener<GymMarker>{


    private GoogleMap mMap;
    private ClusterManager<GymMarker> mClusterManager;

    LatLng currentLocation;

    List<GymMarker> gymsList = new ArrayList<>();

    // offline list
    private List<GymListItem> gymListNew;

    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    private class MarkerRenderer extends DefaultClusterRenderer<GymMarker> {

        private final IconGenerator mIconGenerator = new IconGenerator(getActivity().getApplicationContext());

        public MarkerRenderer(Context context, GoogleMap map, ClusterManager<GymMarker> clusterManager) {
            super(context, map, clusterManager);
        }
        public MarkerRenderer(){
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
        return rootView;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

       Bundle args = getArguments();
       // double lat = args.getDouble("lat");
       // double lon = args.getDouble("lon");

       // currentLocation = new LatLng(lat, lon);
        //  = new LatLng(47.548, 19.0719793);

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

    private synchronized void initMap(){
       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.548, 19.0719793), 13));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.latitude, currentLocation.longitude), 13));
        mClusterManager = new ClusterManager<GymMarker>(getContext(), getMap());
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        mClusterManager.setRenderer(new MarkerRenderer());
        mClusterManager.setOnClusterItemClickListener(this);

        // TODO check permissions...
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

//        custom tile
//        mMap.addTileOverlay()
//        link
//        http://tile.stamen.com/watercolor/{z}/{x}/{y}.jpg

//        TileOverlayOptions mTileOverlayOptions = new TileOverlayOptions();
//        mTileOverlayOptions.
//        CustomTileProvider mTileProvider = new CustomTileProvider(
//                tile_width,
//                tile_height, overlayString);
//         mMap.addTileOverlay(
//                new TileOverlayOptions().tileProvider(mTileProvider)
//                        .zIndex(OTPApp.CUSTOM_MAP_TILE_Z_INDEX));

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

        mClusterManager.setOnClusterItemInfoWindowClickListener(GymMapFragment.this);
        mMap.setOnInfoWindowClickListener(mClusterManager);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        offlineMarkers();
                    }
                });
            }
        });
        thread.start();





    }

    @Override
    public boolean onClusterItemClick(GymMarker gymMarker) {

        Toast.makeText(getContext(), gymMarker.getTitle(), Toast.LENGTH_SHORT).show();





        return false;
    }


    public synchronized void offlineMarkers(){

        for (int i = 0; i < gymListNew.size(); i++) {

            String title = gymListNew.get(i).getTitle();
            double lat = Double.parseDouble(gymListNew.get(i).getLatitude().replace(",","."));
            double lon = Double.parseDouble(gymListNew.get(i).getLongitude().replace(",","."));

            gymsList.add(new GymMarker(new LatLng(lat, lon), title));


        }

        mClusterManager.addItems(gymsList);
        mClusterManager.cluster();
    }

    @Override
    public void onClusterItemInfoWindowClick(GymMarker gymMarker) {




    }



}
