package hu.zsoltborza.gymfinderhun.fragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import hu.zsoltborza.gymfinderhun.R;
import hu.zsoltborza.gymfinderhun.activities.MainActivity;
import hu.zsoltborza.gymfinderhun.adapter.GymOfflineAdapter;
import hu.zsoltborza.gymfinderhun.adapter.GymOnlineSearchAdapter;
import hu.zsoltborza.gymfinderhun.adapter.OnItemClickListener;
import hu.zsoltborza.gymfinderhun.database.AppDatabase;
import hu.zsoltborza.gymfinderhun.database.MarkerEntity;
import hu.zsoltborza.gymfinderhun.database.MarkerEntityDAO;
import hu.zsoltborza.gymfinderhun.model.GymItemDto;
import hu.zsoltborza.gymfinderhun.fragments.base.DrawerItemBaseFragment;
import hu.zsoltborza.gymfinderhun.fragments.base.ListDetailInterface;


/**
 * Created by Zsolt Borza on 2018.09.06..
 * Offline list using Room databse
 */

public class OfflineMarkerListFragment extends DrawerItemBaseFragment implements OnItemClickListener, GymOfflineAdapter.OnItemClickListener {


    public static final String TAG = "GymSearchOnline";
    ListDetailInterface listDetailInterface;
    private List<MarkerEntity> gymList = new ArrayList<>();
    private GymOfflineAdapter adapter;
    private RecyclerView recyclerView;

    LatLng currentLocation;

    DecimalFormat df = new DecimalFormat("#.00");

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment

        AppDatabase database = Room.databaseBuilder(getContext(), AppDatabase.class, "db-markers")
                .allowMainThreadQueries()   //Allows room to do operation on main thread
                .build();
        MarkerEntityDAO markerEntityDAO = database.getMarkerEntityDAO();

        gymList = markerEntityDAO.getMarkerEntitys();

        Bundle args = getArguments();
        if (args != null) {
            double lat = args.getDouble("lat");
            double lon = args.getDouble("lon");
            currentLocation = new LatLng(lat, lon);
        } else {
            currentLocation = new LatLng(47.4544331, 19.633235);
        }


        ((MainActivity)getActivity()).ShowTool();
        return inflater.inflate(R.layout.fragment_offline_list, parent, false);
    }



    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

//        currentLocation = new LatLng(47.4544331, 19.633235);
//        adapter.setActucalPosition(currentLocation);
        adapter = new GymOfflineAdapter(getContext(), gymList, this);
        recyclerView.setAdapter(adapter);

       /* DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(decoration);*/

        if(!(getActivity() instanceof ListDetailInterface)) {
            throw new ClassCastException("Hosting activity must implement GreetingsInterface");
        } else {
            listDetailInterface = (ListDetailInterface) getActivity();
        }



    }


    @Override
    public void onItemClicked(final int position) {

        MarkerEntity selectedMarker = gymList.get(position);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listDetailInterface.showMarkerDetailItem(selectedMarker);
            }
        }, 0);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }




}


