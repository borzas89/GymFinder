package hu.zsoltborza.gymfinderhun.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import hu.zsoltborza.gymfinderhun.model.GymListItem;
import hu.zsoltborza.gymfinderhun.network.domain.Gym;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static final String BASE_URL_API = "https://gymfinder-hun.herokuapp.com/api/";
    private static Retrofit retrofit = null;

    private GymApiService gymApiService;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

   public ApiManager(Context context){
       HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
       interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

       Gson gson = new GsonBuilder()
               .setLenient()
               .create();


       // avoid connect timed out..
       OkHttpClient okHttpClient = new OkHttpClient.Builder()
               .connectTimeout(1, TimeUnit.MINUTES)
               .readTimeout(30, TimeUnit.SECONDS)
               .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

       retrofit = new Retrofit.Builder()
               .client(okHttpClient)
               .baseUrl(BASE_URL_API)
               .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
               .addConverterFactory(GsonConverterFactory.create(gson))
               .build();

       gymApiService = retrofit.create(GymApiService.class);

    }

    public Observable<List<Gym>> getGymsByRadiusAndCoordinate(int radius,double lat, double lon) {
        return gymApiService
                .getGymsByRadiusAndCoordinate(radius,lat,lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
