package hu.zsoltborza.gymfinderhun.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import hu.zsoltborza.gymfinderhun.network.cache.CacheProviders;
import hu.zsoltborza.gymfinderhun.network.domain.Gym;
import hu.zsoltborza.gymfinderhun.network.service.GymApiService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *  GymFinfer Api manager class, contatins several dependencies for fetching data.
 */
public class ApiManager {

    private static final String BASE_URL_API = "https://gymfinder-hun.herokuapp.com/api/";

    private static Retrofit retrofit;
    private OkHttpClient okHttpClient;
    private Gson gson;
    private HttpLoggingInterceptor interceptor;
    private GymApiService gymApiService;

    private CacheProviders cacheProviders;

   public ApiManager(Context context){

       interceptor = new HttpLoggingInterceptor();
       interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

       gson = new GsonBuilder()
               .setLenient()
               .create();

       // avoid connect timed out..
      okHttpClient = new OkHttpClient.Builder()
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

       cacheProviders = new RxCache.Builder()
               .setMaxMBPersistenceCache(5)
               .persistence(context.getFilesDir(), new GsonSpeaker())
               .using(CacheProviders.class);

       gymApiService = retrofit.create(GymApiService.class);

    }

    public Observable<List<Gym>> getGymsByRadiusAndCoordinate(int radius, double lat, double lon, String query, boolean update) {
       return cacheProviders.getCachedGymsByRadiusAndCoordinates(gymApiService
                .getGymsByRadiusAndCoordinate(radius,lat,lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()),new DynamicKey(query),new EvictDynamicKey(update));
    }
}
