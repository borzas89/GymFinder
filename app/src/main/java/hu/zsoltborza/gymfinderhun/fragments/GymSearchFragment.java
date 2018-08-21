package hu.zsoltborza.gymfinderhun.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import hu.zsoltborza.gymfinderhun.R;
import hu.zsoltborza.gymfinderhun.activities.MainActivity;
import hu.zsoltborza.gymfinderhun.adapter.GymOnlineSearchAdapter;
import hu.zsoltborza.gymfinderhun.adapter.OnItemClickListener;
import hu.zsoltborza.gymfinderhun.model.GymItemDto;
import hu.zsoltborza.gymfinderhun.fragments.base.DrawerItemBaseFragment;
import hu.zsoltborza.gymfinderhun.fragments.base.ListDetailInterface;
import hu.zsoltborza.gymfinderhun.network.RetrofitServiceFactory;
import hu.zsoltborza.gymfinderhun.network.service.GymSearchService;
import hu.zsoltborza.gymfinderhun.network.domain.GymSearch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zsolt Borza on 2018.01.31..
 */

public class GymSearchFragment extends DrawerItemBaseFragment implements OnItemClickListener, GymOnlineSearchAdapter.OnItemClickListener {


    public static final String TAG = "GymSearchOnline";

    ListDetailInterface listDetailInterface;

    private List<GymItemDto> gymList = new ArrayList<>();
    private GymOnlineSearchAdapter adapter;

    RecyclerView recyclerView;

    DecimalFormat df = new DecimalFormat("#.00");

    //List<Listable> onlineList = new ArrayList<>();


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


        ((MainActivity)getActivity()).ShowTool();
        return inflater.inflate(R.layout.fragment_gym_search, parent, false);
    }



    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        adapter = new GymOnlineSearchAdapter(getContext(), gymList, this);
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
            double lat = args.getDouble("lat");
            double lon = args.getDouble("lon");
            getGymsFromBudapest(lat,lon);
        }else{
            getGymsFromBudapest(47.4544331,19.633235);
        }

    }




    public void getGymsFromBudapest(final double lat, final double lon){

        Call<GymSearch> call;
        final GymSearchService apiService =
                RetrofitServiceFactory.getClient().create(GymSearchService.class);

        call = apiService.getGymsatBudapest();
        call.enqueue(new Callback<GymSearch>() {
            
            @Override
            public void onResponse(Call<GymSearch> call, Response<GymSearch> response) {

                Log.d(TAG,response.message());

                if(response != null){
                    Log.d(TAG,response.message());
                    if(response.body() !=null){
                        for(int i= 0; i<response.body().getResults().size();i++){
                            GymItemDto item = new GymItemDto();
                            item.setFromOnline(true);
//                    item.setId(response.body().getResults().get(i).getId());
                            item.setTitle(response.body().getResults().get(i).getName());
                            item.setAddress(response.body().getResults().get(i).getFormattedAddress());
                            if(response.body().getResults().get(i).getPhotos() !=null){
                                item.setPhotoReference(response.body().getResults().get(i).getPhotos().get(0).getPhotoReference());
                            }else{
                                item.setPhotoReference("PHOTO_REFERENCE");
                            }

                          /*  String isOpen = "";
                            if(response.body().getResults().get(i).getOpeningHours() != null
                                    && response.body().getResults().get(i).getOpeningHours().getOpenNow()){
//                        isOpen = "Igen";
                                item.setOpen(true);

                            }else{
//                        isOpen = "Nem";
                                item.setOpen(false);
                            }*/

                            LatLng gymposition = new LatLng( response.body().getResults().get(i).getGeometry().getLocation().getLat()
                                    , response.body().getResults().get(i).getGeometry().getLocation().getLng());

                            //LatLng actucalPosition = new LatLng(47.5350554,19.043856);
                            LatLng actucalPosition = new LatLng(lat,lon);
                            //  if(currLoc !=null){
                           // actucalPosition = new LatLng(lat,lon);
                            // }
                            // LatLng actucalPosition = new LatLng(47.5350554,19.043856);


                            item.setRating( response.body().getResults().get(i).getRating());
                            item.setLatitude(gymposition.latitude);
                            item.setLongitude(gymposition.longitude);
                            item.setInfo(df.format(SphericalUtil.computeDistanceBetween(actucalPosition,gymposition)/1000) + " km");

//                    item.setInfo("Értékelés: " + response.body().getResults().get(i).getRating().toString()+ '\n' + "Jelenleg nyitva: " + isOpen
//                    +'\n' +  df.format(SphericalUtil.computeDistanceBetween(actucalPosition,gymposition)/1000) + " km-re");


                            gymList.add(item);
                        }

                    }

                }


                adapter.updateList(gymList);




            }

            @Override
            public void onFailure(Call<GymSearch> call, Throwable t) {
                Log.d(TAG,t.getMessage());
            }
        });

    }




    @Override
    public void onItemClicked(final int position) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listDetailInterface.showListDetail(gymList.get(position));
            }
        }, 0);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }




}
