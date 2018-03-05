package hu.zsoltborza.gymfinderhun.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Borzas on 2018. 01. 27..
 */

public class RetrofitServiceFactory {
//    https://maps.googleapis.com/maps/api/place/search/json?location=47.5350554,19.043856&radius=5000&type=gym&key=AIzaSyCyk1W3jHXffYh7sXdSaoFJRmcTRcyk9sg
    //https://maps.googleapis.com/maps/api/place/textsearch/json?query=edz%C5%91terem+budapest&key=AIzaSyCyk1W3jHXffYh7sXdSaoFJRmcTRcyk9sg
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
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

}