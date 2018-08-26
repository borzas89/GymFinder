package hu.zsoltborza.gymfinderhun.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.zsoltborza.gymfinderhun.adapter.GymAdapter;
import hu.zsoltborza.gymfinderhun.R;
import hu.zsoltborza.gymfinderhun.network.ApiManager;
import hu.zsoltborza.gymfinderhun.network.domain.Gym;
import hu.zsoltborza.gymfinderhun.model.GymListItem;
import hu.zsoltborza.gymfinderhun.fragments.base.DrawerItemBaseFragment;
import hu.zsoltborza.gymfinderhun.fragments.base.ListDetailInterface;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Zsolt Borza on 2018.01.31..
 */

public class GymListFragment extends DrawerItemBaseFragment implements GymAdapter.OnItemClickListener,SearchView.OnQueryTextListener{

    public static final String TAG = "GymList";

    private ListDetailInterface listDetailInterface;

    private List<GymListItem> gymList;
    private GymAdapter adapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.so_swipe)
    SwipeRefreshLayout mSwipe;

    private MenuItem searchIconMenuItem;

    private ApiManager apiManager;

    private double testLatitude = 47.4013408;
    private double testLongitude = 19.0990398;
    boolean update = false;


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
        View rootView = inflater.inflate(R.layout.fragment_gym_list, parent, false);
        setHasOptionsMenu(true);

        apiManager = new ApiManager(rootView.getContext());
        ButterKnife.bind(this,rootView);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        searchIconMenuItem = menu.findItem( R.id.action_search);
        showSearchIcon(false);
        SearchView searchView = (SearchView) searchIconMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }


    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new GymAdapter(getContext(), gymList, this);

        recyclerView.setAdapter(adapter);

       /* DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(decoration);*/

        if(!(getActivity() instanceof ListDetailInterface)) {
            throw new ClassCastException("Hosting activity must implement GreetingsInterface");
        } else {
            listDetailInterface = (ListDetailInterface) getActivity();
        }

        Bundle args = getArguments();
        if(args != null){
            testLatitude = args.getDouble("lat");
            testLongitude = args.getDouble("lon");
            //refreshList(lat,lon);
        }

        adapter.setActucalPosition(new LatLng(testLatitude,testLongitude));

        mSwipe.setOnRefreshListener(this::refreshList);

        // if the coordinates are changed refresh list, fetch data etc
       //if(gymList == null  ){
            refreshList();
        //}

    }
    private void refreshList() {
        showSearchIcon(false);
        showRefresh(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                apiManager.getGymsByRadiusAndCoordinate(5000,getTestLatitude(),getTestLongitude(),"q",false)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<Gym>>() {

                            @Override
                            public void onNext(List<Gym> gyms) {
                                Log.d(TAG,"coordinates are: " + getTestLatitude() + " , " + getTestLongitude());
                                showRefresh(false);
                                adapter.updatingList(gyms);
                                Log.d(TAG,"gyms size: " + gyms.size());
                                showSearchIcon(true);
                               // Log.d(TAG,"gymList size: " + gymList.size());
                            }

                            @Override
                            public void onError(Throwable e) {
                                // handle the error case
                                showRefresh(false);
                                showSearchIcon(false);
                                Log.d(TAG,"failed to load");

                            }

                            @Override
                            public void onComplete() {
                                showRefresh(false);
                            }
                        }));
        // continue working and dispose all subscriptions when the values from the Single objects are not interesting any more
        //compositeDisposable.dispose();


    }


    @Override
    public void onItemClicked(final int position) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listDetailInterface.showListDetailItem(adapter.getAdapterList().get(position));
            }
        }, 0);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public boolean onQueryTextChange(String newText) {
        final List<GymListItem> filteredModelList = filter(adapter.getAdapterList(), newText);
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


    private void showRefresh(boolean show) {
        mSwipe.setRefreshing(show);
        int visibility = show ? View.GONE : View.VISIBLE;
        recyclerView.setVisibility(visibility);
    }

    private void showSearchIcon(boolean show){
        if(searchIconMenuItem != null){
            searchIconMenuItem.setVisible(show);
        }
    }

    public double getTestLatitude() {
        return testLatitude;
    }

    public void setTestLatitude(double testLatitude) {
        this.testLatitude = testLatitude;
    }

    public double getTestLongitude() {
        return testLongitude;
    }

    public void setTestLongitude(double testLongitude) {
        this.testLongitude = testLongitude;
    }

}
