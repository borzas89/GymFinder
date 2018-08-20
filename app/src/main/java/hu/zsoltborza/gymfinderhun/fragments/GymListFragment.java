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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.zsoltborza.gymfinderhun.activities.MainActivity;
import hu.zsoltborza.gymfinderhun.adapter.GymAdapter;
import hu.zsoltborza.gymfinderhun.R;
import hu.zsoltborza.gymfinderhun.converters.GymToGymItemConverter;
import hu.zsoltborza.gymfinderhun.network.ApiManager;
import hu.zsoltborza.gymfinderhun.network.GymApiService;
import hu.zsoltborza.gymfinderhun.network.RetrofitServiceFactory;
import hu.zsoltborza.gymfinderhun.network.domain.Gym;
import hu.zsoltborza.gymfinderhun.utils.Utils;
import hu.zsoltborza.gymfinderhun.model.GymListItem;
import hu.zsoltborza.gymfinderhun.fragments.base.DrawerItemBaseFragment;
import hu.zsoltborza.gymfinderhun.fragments.base.ListDetailInterface;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zsolt Borza on 2018.01.31..
 */

public class GymListFragment extends DrawerItemBaseFragment implements GymAdapter.OnItemClickListener,SearchView.OnQueryTextListener{

//     SearchView.OnQueryTextListener
    public static final String TAG = "GymList";

    ListDetailInterface listDetailInterface;

    private List<GymListItem> gymList;
    private GymAdapter adapter;
    RecyclerView recyclerView;

    @BindView(R.id.so_swipe)
    SwipeRefreshLayout mSwipe;

    private ApiManager apiManager;

    private  double testLatitude = 47.4013408;
    private  double testLongitude = 19.0990398;


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

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }


    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Sample data from file...
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
        if(args != null){
       //     double lat = args.getDouble("lat");
          //  double lon = args.getDouble("lon");
         //  getGymsByRadiusAndCoordinate(lat,lon);
            testLatitude = args.getDouble("lat");
            testLongitude = args.getDouble("lon");
            //refreshList(lat,lon);
        }else{
        //    getGymsByRadiusAndCoordinate(47.4544331,19.633235);
           // refreshList(47.4544331,19.633235);
        }

        mSwipe.setOnRefreshListener(this::refreshList);
        refreshList();


    }
    private void refreshList() {
        showRefresh(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                apiManager.getGymsByRadiusAndCoordinate(5000,testLatitude,testLongitude)
                        .subscribeWith(new DisposableObserver<List<Gym>>() {

                            @Override
                            public void onNext(List<Gym> gyms) {
                                showRefresh(false);
                                adapter.updatingList(gyms);
                            }

                            @Override
                            public void onError(Throwable e) {
                                // handle the error case
                                showRefresh(false);
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


    private void showRefresh(boolean show) {
        mSwipe.setRefreshing(show);
        int visibility = show ? View.GONE : View.VISIBLE;
        recyclerView.setVisibility(visibility);
    }

}
