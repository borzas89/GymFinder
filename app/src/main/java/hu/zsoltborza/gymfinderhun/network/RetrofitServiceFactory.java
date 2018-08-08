package hu.zsoltborza.gymfinderhun.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Borzas on 2018. 01. 27..
 */

public class RetrofitServiceFactory {
//    https://maps.googleapis.com/maps/api/place/search/json?location=47.5350554,19.043856&radius=5000&type=gym&key=AIzaSyCyk1W3jHXffYh7sXdSaoFJRmcTRcyk9sg
    //https://maps.googleapis.com/maps/api/place/textsearch/json?query=edz%C5%91terem+budapest&key=AIzaSyCyk1W3jHXffYh7sXdSaoFJRmcTRcyk9sg
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
    private static final String BASE_URL_API = "https://gymfinder-hun.herokuapp.com/api/";
    private static final String BASE_URL_DEV_API = "";
//    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/textsearch/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientForGymFinderApi() {

        // avoid connect timed out..
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

}