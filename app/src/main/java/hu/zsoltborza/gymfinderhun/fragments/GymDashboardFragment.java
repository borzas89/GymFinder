package hu.zsoltborza.gymfinderhun.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.zsoltborza.gymfinderhun.R;
import hu.zsoltborza.gymfinderhun.activities.MainActivity;
import hu.zsoltborza.gymfinderhun.fragments.base.DrawerItemBaseFragment;
import hu.zsoltborza.gymfinderhun.network.GymApiService;

import hu.zsoltborza.gymfinderhun.network.RetrofitServiceFactory;
import hu.zsoltborza.gymfinderhun.network.domain.Gym;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zsolt Borza on 2018.03.05..
 */

public class GymDashboardFragment extends DrawerItemBaseFragment {

    public static final String TAG = "GymDashboard";

    @BindView(R.id.firstButton)
    Button firstButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, rootView);

        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getGymsByRadiusAndCoordinate();
                getReactiveGymsByRadiusAndCoordinate();
            }
        });



        return rootView;
    }

    public void getGymsByRadiusAndCoordinate(){



        Call<List<Gym>> call;
        final GymApiService apiService =
                RetrofitServiceFactory.getClientForGymFinderApi().create(GymApiService.class);

        call = apiService.getGymsByRadiusAndCoordinate(1000,47.547141,19.076171);

        call.enqueue(new Callback<List<Gym>>() {
            @Override
            public void onResponse(Call<List<Gym>> call, Response<List<Gym>> response) {

                List<Gym> gyyy = response.body();

                Log.d("GYM",call.request().url().toString());

                String size = String.valueOf(gyyy.size());

                Log.d("GYM",size);

                Log.d("GYM",gyyy.get(0).getInformation());



            }

            @Override
            public void onFailure(Call<List<Gym>> call, Throwable t) {
                Log.d("GYM",t.getMessage());
                Log.d("GYM","FAILED AT " + call.request().url().toString());
            }
        });



    }

    public void getReactiveGymsByRadiusAndCoordinate(){

        Observable<List<Gym>> gymListaObservable;

        final GymApiService apiService =
                RetrofitServiceFactory.getClientForGymFinderApi().create(GymApiService.class);

        gymListaObservable = apiService.getReactiveGymsByRadiusAndCoordinate(1000,47.547141,19.076171);

        gymListaObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Gym>>() {
                                   @Override
                                   public void onNext(List<Gym> gyms) {

                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                       Log.d("GYM","error : " + e.getMessage().toLowerCase());
                                   }

                                   @Override
                                   public void onComplete() {
                                       Log.d("GYM","Completed");
                                   }
                               });



    }


    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
