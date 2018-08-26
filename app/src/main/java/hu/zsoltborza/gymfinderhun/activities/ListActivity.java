package hu.zsoltborza.gymfinderhun.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import hu.zsoltborza.gymfinderhun.adapter.GymAdapter;
import hu.zsoltborza.gymfinderhun.R;
import hu.zsoltborza.gymfinderhun.model.GymListItem;
import hu.zsoltborza.gymfinderhun.network.RetrofitServiceFactory;
import hu.zsoltborza.gymfinderhun.network.service.GooglePlacesService;
import hu.zsoltborza.gymfinderhun.network.domain.GymSearch;
import hu.zsoltborza.gymfinderhun.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity implements GymAdapter.OnItemClickListener,SearchView.OnQueryTextListener {

    private List<GymListItem> gymList;
    private GymAdapter adapter;

// https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=47.401285,19.0969083&radius=5000&type=gym&key=AIzaSyCyk1W3jHXffYh7sXdSaoFJRmcTRcyk9sg

//    fotó https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CmRaAAAAvH90R7THyOqAw2ekasXCAMrRQ3pt-t5UoCr2kQdJNI4bPuPWl0HZIpSbvxpW0oMaiw13r8Cqux_ItZLS9JyDVJqtOcHsD3BxB6SLNHPqi85wx5SZAzfyoF0alr6R6fRjEhDPBp9Z1Qmip62-AiQNAMZuGhQCFI-b-a-W53zr8zJCjEl3eCG52w&key=AIzaSyCyk1W3jHXffYh7sXdSaoFJRmcTRcyk9sg
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        gymList = Utils.getDataFromFile(ListActivity.this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        adapter = new GymAdapter(this, gymList, this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(decoration);

//        getGymsFromBudapest();




    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.menu, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);


        return true;
    }

    public void getGymsFromBudapest(){

        Call<GymSearch> call;
        GooglePlacesService apiService =
                RetrofitServiceFactory.getClient().create(GooglePlacesService.class);

        call = apiService.getGymsatBudapest();
        call.enqueue(new Callback<GymSearch>() {


            @Override
            public void onResponse(Call<GymSearch> call, Response<GymSearch> response) {

                response.body().getResults();

            }

            @Override
            public void onFailure(Call<GymSearch> call, Throwable t) {

            }
        });

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

    @Override
    public void onItemClicked(int position) {

    }





}
