package hu.zsoltborza.gymfinderhun.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hu.zsoltborza.gymfinderhun.activities.MainActivity;
import hu.zsoltborza.gymfinderhun.adapter.GymAdapter;
import hu.zsoltborza.gymfinderhun.R;
import hu.zsoltborza.gymfinderhun.network.GymApiService;
import hu.zsoltborza.gymfinderhun.network.RetrofitServiceFactory;
import hu.zsoltborza.gymfinderhun.network.domain.Gym;
import hu.zsoltborza.gymfinderhun.utils.Utils;
import hu.zsoltborza.gymfinderhun.model.GymListItem;
import hu.zsoltborza.gymfinderhun.fragments.base.DrawerItemBaseFragment;
import hu.zsoltborza.gymfinderhun.fragments.base.ListDetailInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zsolt Borza on 2018.01.31..
 */

public class GymListFragment extends DrawerItemBaseFragment implements GymAdapter.OnItemClickListener,SearchView.OnQueryTextListener {

//     SearchView.OnQueryTextListener
    public static final String TAG = "GymList";

    ListDetailInterface listDetailInterface;

    private List<GymListItem> gymList;
    private GymAdapter adapter;
    RecyclerView recyclerView;


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

        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.fragment_gym_list, parent, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }


    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

     //  gymList = Utils.getDataFromFile(getContext());

       
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        adapter = new GymAdapter(getContext(), gymList, this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(decoration);

        if(!(getActivity() instanceof ListDetailInterface)) {
            throw new ClassCastException("Hosting activity must implement GreetingsInterface");
        } else {
            listDetailInterface = (ListDetailInterface) getActivity();
        }


        Bundle args = getArguments();
        double lat = args.getDouble("lat");
        double lon = args.getDouble("lon");
        getGymsByRadiusAndCoordinate(lat,lon);

//        adapter = new GymAdapter(getContext(), getGymsByRadiusAndCoordinate(), this);
//        recyclerView.setAdapter(adapter);

    }


    public void  getGymsByRadiusAndCoordinate(double lat, double lon){



        Call<List<Gym>> call;
        final GymApiService apiService =
                RetrofitServiceFactory.getClientForGymFinderApi().create(GymApiService.class);

       // call = apiService.getGymsByRadiusAndCoordinate(1000,47.547141,19.076171);

        android.location.Location currLoc =((MainActivity) getActivity()).getmCurrentLocation();
        call = apiService.getGymsByRadiusAndCoordinate(5000,lat, lon);

        call.enqueue(new Callback<List<Gym>>() {
            @Override
            public void onResponse(Call<List<Gym>> call, Response<List<Gym>> response) {

                final List<GymListItem> reGymList = new ArrayList<>();

                List<Gym> fetchedGymList = response.body();



                for (Gym gymItem : fetchedGymList){

                    GymListItem gymListItem = new GymListItem();
                    gymListItem.setId(String.valueOf(gymItem.getId()));
                    gymListItem.setAddress(gymItem.getAddress().getAddress1() + ", " + gymItem.getAddress().getAddress2());
                    gymListItem.setAddress1(gymItem.getAddress().getAddress1());
                    gymListItem.setAddress2(gymItem.getAddress().getAddress2());
                    gymListItem.setInfo(gymItem.getInformation());
                    gymListItem.setLatitude(String.valueOf(gymItem.getAddress().getLatitude()));
                    gymListItem.setLongitude(String.valueOf(gymItem.getAddress().getLongitude()));
                    gymListItem.setTitle(gymItem.getTitle());
                    reGymList.add(gymListItem);
                }

                gymList = reGymList;
                adapter.setFilter(reGymList);


                Log.d("GYM",call.request().url().toString());

                String size = String.valueOf(fetchedGymList.size());

                Log.d("GYM",size);

                Log.d("GYM",fetchedGymList.get(0).getInformation());



            }

            @Override
            public void onFailure(Call<List<Gym>> call, Throwable t) {
                Log.d("GYM",t.getMessage());
                Log.d("GYM","FAILED AT " + call.request().url().toString());
            }
        });




    }



    @Override
    public void onItemClicked(final int position) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listDetailInterface.showListDetailItem(gymList.get(position));
            }
        }, 0);


    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public boolean onQueryTextChange(String newText) {
        final List<GymListItem> filteredModelList = filter(gymList, newText);
        adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<GymListItem> filter(List<GymListItem> items, String query) {
        query = query.toLowerCase();
        final List<GymListItem> filteredItemsList = new ArrayList<>();
        for (GymListItem item : items) {
            final String text = item.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredItemsList.add(item);
            }
        }
        return filteredItemsList;
    }
}
