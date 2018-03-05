package hu.zsoltborza.gymfinderhun.network.domain;

import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Borzas on 2018. 01. 27..
 */

public interface ApiInterface {

    @GET("textsearch/json?query=edzőterem+budapest&key=AIzaSyCyk1W3jHXffYh7sXdSaoFJRmcTRcyk9sg")
    public Call<GymSearch> getGymsatBudapest();

    @GET("search/json?type=gym&key=AIzaSyCyk1W3jHXffYh7sXdSaoFJRmcTRcyk9sg")
    Call<MarkerSearch> getGymsByLocation(@Query("location") String location, @Query("radius") int radius);

    @GET("search/json?location=47.5350554,19.043856&radius=5000&type=gym&key=AIzaSyCyk1W3jHXffYh7sXdSaoFJRmcTRcyk9sg")
    public Call<MarkerSearch> getGymMarkers();



}
