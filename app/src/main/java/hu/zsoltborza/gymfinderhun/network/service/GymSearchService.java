package hu.zsoltborza.gymfinderhun.network.service;

import hu.zsoltborza.gymfinderhun.network.domain.GymSearch;
import hu.zsoltborza.gymfinderhun.network.domain.MarkerSearch;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Borzas on 2018. 01. 27.. *
 * Google Places API calls service.
 */

public interface GymSearchService {

    @GET("textsearch/json?query=edzőterem+budapest&key=AIzaSyCyk1W3jHXffYh7sXdSaoFJRmcTRcyk9sg")
    Call<GymSearch> getGymsatBudapest();

    @GET("search/json?type=gym&key=AIzaSyCyk1W3jHXffYh7sXdSaoFJRmcTRcyk9sg")
    Call<MarkerSearch> getGymsByLocation(@Query("location") String location, @Query("radius") int radius);

    @GET("search/json?location=47.5350554,19.043856&radius=5000&type=gym&key=AIzaSyCyk1W3jHXffYh7sXdSaoFJRmcTRcyk9sg")
    Call<MarkerSearch> getGymMarkers();



}
